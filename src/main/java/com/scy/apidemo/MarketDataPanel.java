/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.scy.apidemo;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.ScannerSubscription;
import com.ib.client.Types.*;
import com.ib.controller.ApiController.*;
import com.ib.controller.Bar;
import com.ib.controller.Instrument;
import com.ib.controller.ScanCode;
import com.scy.apidemo.util.*;
import com.scy.apidemo.util.NewTabbedPanel.NewTabPanel;
import com.scy.apidemo.util.VerticalPanel.StackPanel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MarketDataPanel extends JPanel {
	private final Contract m_contract = new Contract();
	private TopResultsPanel m_topResultPanel;
	
	MarketDataPanel() {
		setLayout( new BorderLayout() );
		m_topResultPanel = new MarketDataPanel.TopResultsPanel();
		add(m_topResultPanel, BorderLayout.CENTER);
	}

	public void initSelected() {
		for (Contract contract : SelectedContractsConf.getContracts(this.getClass().getClassLoader().getResource("selectedContracts.json").getPath())) {
			m_topResultPanel.m_model.addRow(contract);
		}
		m_topResultPanel.m_tab.setPreferredSize(new Dimension(getWidth(), getHeight()));
}
	

	private class TopResultsPanel extends NewTabPanel {
		final TopModel m_model = new TopModel();
		final JTable m_tab = new TopTable( m_model);

		TopResultsPanel() {

			JScrollPane scroll = new JScrollPane( m_tab);

			HtmlButton addSub = new HtmlButton( "添加订阅") {
				@Override protected void actionPerformed() {
				}
			};

			VerticalPanel butPanel = new VerticalPanel();
			butPanel.add( addSub);
			
			setLayout( new BorderLayout() );
			add( butPanel, BorderLayout.NORTH);
			add( scroll);
		}
		
		/** Called when the tab is first visited. */
		@Override public void activated() {
		}

		/** Called when the tab is closed by clicking the X. */
		@Override public void closed() {
			m_model.desubscribe();
			m_topResultPanel = null;
		}

		class TopTable extends JTable {
			public TopTable(TopModel model) { super( model);}

			@Override public TableCellRenderer getCellRenderer(int rowIn, int column) {
				TableCellRenderer rend = super.getCellRenderer(rowIn, column);
				m_model.color( rend, rowIn, getForeground() );
				return rend;
			}
		}
	}

}
