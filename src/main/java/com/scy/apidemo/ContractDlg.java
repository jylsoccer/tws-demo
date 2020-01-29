/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.scy.apidemo;

import com.ib.client.Contract;
import com.scy.apidemo.util.HtmlButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


class ContractDlg extends JDialog {
	ContractPanel m_contractPanel;
    
    ContractDlg( JFrame f, Contract c) {
    	super( f, true);
    	
    	m_contractPanel = new ContractPanel( c);
    	
    	setLayout( new BorderLayout() );
    	
    	
    	HtmlButton ok = new HtmlButton( "OK") {
			@Override public void actionPerformed() {
				onOK();
			}
		};
		ok.setHorizontalAlignment(SwingConstants.CENTER);

		m_contractPanel.addKeyListener( new KeyListener() {
			@Override public void keyTyped(KeyEvent e) {
				System.out.println( "lkj");
			}
			
			@Override public void keyReleased(KeyEvent e) {
			}
			
			@Override public void keyPressed(KeyEvent e) {
			}
		});
    	
    	add( m_contractPanel);
    	add( ok, BorderLayout.SOUTH);
    	pack();
    }

	public void onOK() {
		m_contractPanel.onOK();
		setVisible( false);
	}
}