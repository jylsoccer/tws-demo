/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.scy.apidemo;

import com.scy.apidemo.util.HtmlButton;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class NewMarketDataPanel extends JPanel {

	final TopModel m_model = new TopModel();

	public NewMarketDataPanel() {
		// header
		HtmlButton addBut = new HtmlButton( "添加") {
			@Override public void actionPerformed() {
				// TODO: 2020/2/29
			}
		};

		HtmlButton delBut = new HtmlButton( "删除") {
			@Override public void actionPerformed() {
				// TODO: 2020/2/29
			}
		};

		JPanel p = new JPanel( new FlowLayout( FlowLayout.LEADING, 10, 10));
		p.add( addBut);
		p.add( delBut);


		// 行情列表
		JTable table = new JTable( m_model);
		JScrollPane scroll = new JScrollPane( table);
		scroll.setBorder( new TitledBorder( "行情"));

        // 布局
		setLayout( new BorderLayout() );
		add( p, BorderLayout.NORTH);
		add( scroll);
		setPreferredSize(new Dimension(1000, 200));
	}

	public static void main(String[] args) {
		NewMarketDataPanel p = new NewMarketDataPanel();

		JFrame f = new JFrame();
		f.add( p);
		f.pack();
		f.setVisible( true);
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
	}
}
