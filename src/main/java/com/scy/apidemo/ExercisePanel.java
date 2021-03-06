/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.scy.apidemo;

import com.scy.apidemo.AccountInfoPanel.PortfolioModel;
import com.scy.apidemo.util.NewTabbedPanel.INewTab;
import com.scy.apidemo.util.VerticalPanel.HorzPanel;
import com.ib.client.Types.ExerciseType;
import com.ib.client.Types.SecType;
import com.ib.controller.ApiController.IAccountHandler;
import com.ib.controller.Position;
import com.scy.apidemo.util.HtmlButton;
import com.scy.apidemo.util.TCombo;
import com.scy.apidemo.util.UpperField;
import com.scy.apidemo.util.VerticalPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ExercisePanel extends HorzPanel implements INewTab, IAccountHandler {
	private DefaultListModel m_acctList = new DefaultListModel();
	private JList m_accounts = new JList( m_acctList);
	private String m_selAcct = "";
	private PortfolioModel m_portfolioModel = new PortfolioModel();
	private JTable m_portTable = new JTable( m_portfolioModel);
	
	ExercisePanel() {
		JScrollPane acctsScroll = new JScrollPane( m_accounts);
		acctsScroll.setBorder( new TitledBorder( "Select account"));
		
		JScrollPane portScroll = new JScrollPane( m_portTable);
		portScroll.setBorder( new TitledBorder( "Select long option position"));
		
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS) );
		add( acctsScroll);
		add( portScroll);
		add( new ExPanel() );

		m_accounts.addListSelectionListener( new ListSelectionListener() {
			@Override public void valueChanged(ListSelectionEvent e) {
				onAcctChanged();
			}
		});
	}
	
	protected void onAcctChanged() {
		int i = m_accounts.getSelectedIndex();
		if (i != -1) {
			String selAcct = (String)m_acctList.get( i);
			if (!selAcct.equals( m_selAcct) ) {
				m_selAcct = selAcct;
				m_portfolioModel.clear();
				ApiDemo.INSTANCE.controller().reqAccountUpdates(true, m_selAcct, this);
			}
		}
	}

	class ExPanel extends VerticalPanel {
		TCombo<ExerciseType> m_combo = new TCombo<ExerciseType>( ExerciseType.values() );
		UpperField m_qty = new UpperField( "1");
		JCheckBox m_override = new JCheckBox();

		ExPanel() {
			HtmlButton but = new HtmlButton( "Go") {
				protected void actionPerformed() {
					onExercise();
				}
			};

			m_combo.removeItem( ExerciseType.None);

			add( "Action", m_combo);
			add( "Quantity", m_qty);
			add( "Override", m_override);
			add( but);
		}

		protected void onExercise() {
			String account = (String)m_accounts.getSelectedValue();
			int i = m_portTable.getSelectedRow();
			if (i != -1 && account != null) {
				Position position = m_portfolioModel.getPosition( i);
				ApiDemo.INSTANCE.controller().exerciseOption(account, position.contract(), m_combo.getSelectedItem(), m_qty.getInt(), m_override.isSelected() );
			}
		}
	}

	/** Show long option positions only. */
	@Override public void updatePortfolio(Position position) {
		if (position.account().equals( m_selAcct) && position.contract().secType() == SecType.OPT) {		
			m_portfolioModel.update( position);
		}
	}
		
	/** Called when the tab is first visited. */
	@Override public void activated() {
		for (String account : ApiDemo.INSTANCE.accountList() ) {
			m_acctList.addElement( account);
		}
	}
	
	/** Called when the tab is closed by clicking the X. */
	@Override public void closed() {
	}

	@Override public void accountValue(String account, String key, String value, String currency) {
	}

	@Override public void accountTime(String timeStamp) {
	}

	@Override public void accountDownloadEnd(String account) {
	}
}
