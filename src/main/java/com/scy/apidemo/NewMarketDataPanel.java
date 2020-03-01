/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.scy.apidemo;

import com.ib.client.Contract;
import com.scy.apidemo.util.HtmlButton;
import com.scy.apidemo.util.SelectedContractsConf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class NewMarketDataPanel extends JPanel {

	public final static TopModel m_model = new TopModel();
	private JTable table;

	public NewMarketDataPanel() {
		JButton delBut = new JButton( "删除收藏");
		delBut.addActionListener((e) -> {
			m_model.cancel(getSelectedOrder());
		});

		Box p=Box.createVerticalBox();
		p.add(Box.createVerticalGlue());
		p.add( delBut);
		p.add(Box.createVerticalGlue());


		// 行情列表
		table = new JTable( m_model);
		table.setPreferredSize(new Dimension(700, 180));
		JScrollPane scroll = new JScrollPane( table);
		scroll.setBorder( new TitledBorder( "行情"));
		scroll.setPreferredSize(new Dimension(700, 180));

        // 布局
		setLayout( new BorderLayout() );
		add( p, BorderLayout.EAST);
		add( scroll, BorderLayout.CENTER);
		setPreferredSize(new Dimension(1000, 180));
	}

	public void initSelected() {
		for (Contract contract : SelectedContractsConf.getContracts(this.getClass().getClassLoader().getResource("selectedContracts.json").getPath())) {
			m_model.addRow(contract);
		}
	}

	private int getSelectedOrder() {
		return table.getSelectedRow();
	}
}
