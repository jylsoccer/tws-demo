/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.scy.apidemo;

import com.scy.apidemo.util.VerticalPanel;

import javax.swing.*;

public class NewAccountsAndTradesPanel extends VerticalPanel.HorzPanel {

	NewAccountsAndTradesPanel() {
		NewAccountInfoPanel accountInfoPanel = new NewAccountInfoPanel();
		NewTradesPanel tradesPanel = new NewTradesPanel();

		add(accountInfoPanel);
		add(tradesPanel);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.add(new NewAccountsAndTradesPanel());

		f.pack();
		f.setVisible( true);
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
	}
}
