/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.scy.apidemo;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.Execution;
import com.ib.client.ExecutionFilter;
import com.ib.controller.ApiController.ITradeReportHandler;
import com.scy.apidemo.util.HtmlButton;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TradesPanel extends JPanel implements ITradeReportHandler {
	private ArrayList<FullExec> m_trades = new ArrayList<FullExec>();
	private HashMap<String,FullExec> m_map = new HashMap<String,FullExec>();
	private Model m_model = new Model();
	
	TradesPanel() {
		JTable table = new JTable( m_model);
		JScrollPane scroll = new JScrollPane( table);
		scroll.setBorder( new TitledBorder( "Trade Log"));
		
		HtmlButton but = new HtmlButton( "Refresh") {
			@Override public void actionPerformed() {
				onRefresh();
			}
		};

		JPanel p = new JPanel( new FlowLayout( FlowLayout.RIGHT));
		p.add( but);
		
		setLayout( new BorderLayout() );
		add( scroll);
		add( p, BorderLayout.SOUTH);
	}

	public void activated() {
		onRefresh();
	}
	
	private void onRefresh() {
		ApiDemo.INSTANCE.controller().reqExecutions( new ExecutionFilter(), this);
	}

	@Override public void tradeReport(String tradeKey, Contract contract, Execution trade) {
		FullExec full = m_map.get( tradeKey);
		
		if (full != null) {
			full.m_trade = trade;
		}
		else {
			full = new FullExec( contract, trade);
			m_trades.add( full);
			m_map.put( tradeKey, full);
		}
		
		m_model.fireTableDataChanged();
	}
	
	@Override public void tradeReportEnd() {
	}
	
	@Override public void commissionReport(String tradeKey, CommissionReport commissionReport) {
		FullExec full = m_map.get( tradeKey);
		if (full != null) {
			full.m_commissionReport = commissionReport;
		}
	}

	private class Model extends AbstractTableModel {
		@Override public int getRowCount() {
			return m_trades.size();
		}

		@Override public int getColumnCount() {
			return 8;
		}
		
		@Override public String getColumnName(int col) {
			switch( col) {
				case 0: return "Date/Time";
				case 1: return "Account";
				case 2: return "Model Code";
				case 3: return "Action";
				case 4: return "Quantity";
				case 5: return "Description";
				case 6: return "Price";
				case 7: return "Commission";
				default: return null;
			}
		}

		@Override public Object getValueAt(int row, int col) {
			FullExec full = m_trades.get( row);
			
			switch( col) {
				case 0: return full.m_trade.time();
				case 1: return full.m_trade.acctNumber();
				case 2: return full.m_trade.modelCode();
				case 3: return full.m_trade.side();
				case 4: return full.m_trade.shares();
				case 5: return full.m_contract.description();
				case 6: return full.m_trade.price();
				case 7: return full.m_commissionReport != null ? full.m_commissionReport.m_commission : null;
				default: return null;
			}
		}
	}

	static class FullExec {
		Contract m_contract;
		Execution m_trade;
		CommissionReport m_commissionReport;
		
		FullExec( Contract contract, Execution trade) {
			m_contract = contract;
			m_trade = trade;
		}
	}
}
