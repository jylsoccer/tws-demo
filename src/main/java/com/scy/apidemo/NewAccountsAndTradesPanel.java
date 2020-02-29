/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.scy.apidemo;

import javax.swing.*;
import java.awt.*;

public class NewAccountsAndTradesPanel extends JPanel {

	NewAccountsAndTradesPanel() {
		NewAccountInfoPanel accountInfoPanel = new NewAccountInfoPanel();
		NewTradesPanel tradesPanel = new NewTradesPanel();

		JSplitPane vSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, accountInfoPanel, tradesPanel);

		add(vSplitPane);
//		setPreferredSize(new Dimension(1000, 200));
	}

	public static void main(String[] args) {
		JSplitPane vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new NewMarketDataPanel(), new NewOrdersPanel());
		vSplitPane.setDividerLocation(200);
		JSplitPane vSplitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, vSplitPane, new NewAccountsAndTradesPanel());
		vSplitPane2.setDividerLocation(580);

		JFrame f = new JFrame();
		f.add(vSplitPane2);

		f.pack();
		f.setVisible( true);
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
	}
}
