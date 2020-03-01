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
		setPreferredSize(new Dimension(1000, 300));
	}
}
