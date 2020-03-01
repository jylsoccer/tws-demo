/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.scy.apidemo;

import com.ib.client.Contract;
import com.ib.client.TagValue;
import com.ib.client.TickType;
import com.ib.client.Types.MktDataType;
import com.ib.controller.ApiController.TopMktDataAdapter;
import com.ib.controller.Formats;
import com.scy.rx.model.MktDataRequest;
import com.scy.rx.service.MarketApi;
import com.scy.rx.service.impl.MarketApiImpl;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static com.ib.controller.Formats.*;

@Slf4j
class TopModel extends AbstractTableModel {
	private ArrayList<TopRow> m_rows = new ArrayList<TopRow>();

	void addRow( Contract contract) {
		TopRow row = new TopRow( this, contract.description() );
		if (isRowExists(row)) {
			log.info("row already exists: {}", row.m_description);
			return;
		}
		m_rows.add( row);
		ApiDemo.INSTANCE.controller().reqTopMktData(contract, "", false, row);
		fireTableRowsInserted( m_rows.size() - 1, m_rows.size() - 1);
	}

	private boolean isRowExists(TopRow row) {
		String desc = row.m_description;
		for (TopRow iRow : m_rows) {
			if (Objects.equals(iRow.m_description, desc)) {
				return true;
			}
		}
		return false;
	}

	public void desubscribe() {
		for (TopRow row : m_rows) {
			ApiDemo.INSTANCE.controller().cancelTopMktData( row);
		}
	}		

	@Override public int getRowCount() {
		return m_rows.size();
	}
	
	@Override public int getColumnCount() {
		return 9;
	}
	
	@Override public String getColumnName(int col) {
		switch( col) {
			case 0: return "合约";
			case 1: return "买量";
			case 2: return "买价";
			case 3: return "卖价";
			case 4: return "卖量";
			case 5: return "最新价";
			case 6: return "时间";
			case 7: return "涨跌";
			case 8: return "成交量";
			default: return null;
		}
	}

	@Override public Object getValueAt(int rowIn, int col) {
		TopRow row = m_rows.get( rowIn);
		switch( col) {
			case 0: return row.m_description;
			case 1: return row.m_bidSize;
			case 2: return fmt4( row.m_bid);
			case 3: return fmt4( row.m_ask);
			case 4: return row.m_askSize;
			case 5: return fmt( row.m_last);
			case 6: return fmtTime( row.m_lastTime);
			case 7: return row.change();
			case 8: return Formats.fmt0( row.m_volume);
			default: return null;
		}
	}
	
	public void color(TableCellRenderer rend, int rowIn, Color def) {
		TopRow row = m_rows.get( rowIn);
		Color c = row.m_frozen ? Color.gray : def;
		((JLabel)rend).setForeground( c);
	}

	public void cancel(int i) {
		if (i < 0) {
			return;
		}
		ApiDemo.INSTANCE.controller().cancelTopMktData( m_rows.remove(i) );
		fireTableDataChanged();
	}
	
	static class TopRow extends TopMktDataAdapter {
		AbstractTableModel m_model;
		String m_description;
		double m_bid;
		double m_ask;
		double m_last;
		long m_lastTime;
		int m_bidSize;
		int m_askSize;
		double m_close;
		int m_volume;
		boolean m_frozen;
		
		TopRow( AbstractTableModel model, String description) {
			m_model = model;
			m_description = description;
		}

		public String change() {
			return m_close == 0	? null : fmtPct( (m_last - m_close) / m_close);
		}

		@Override public void tickPrice( TickType tickType, double price, int canAutoExecute) {
			switch( tickType) {
				case BID:
				case DELAYED_BID:
					m_bid = price;
					break;
				case ASK:
				case DELAYED_ASK:
					m_ask = price;
					break;
				case LAST:
				case DELAYED_LAST:
					m_last = price;
					break;
				case CLOSE:
				case DELAYED_CLOSE:
					m_close = price;
					break;
				default: break;	
			}
			m_model.fireTableDataChanged(); // should use a timer to be more efficient
		}

		@Override public void tickSize( TickType tickType, int size) {
			switch( tickType) {
				case BID_SIZE:
					m_bidSize = size;
					break;
				case ASK_SIZE:
					m_askSize = size;
					break;
				case VOLUME:
					m_volume = size;
					break;
                default: break; 
			}
			m_model.fireTableDataChanged();
		}
		
		@Override public void tickString(TickType tickType, String value) {
			switch( tickType) {
				case LAST_TIMESTAMP:
					m_lastTime = Long.parseLong( value) * 1000;
					break;
                default: break; 
			}
		}
		
		@Override public void marketDataType(MktDataType marketDataType) {
			m_frozen = marketDataType == MktDataType.Frozen;
			m_model.fireTableDataChanged();
		}
	}
}
