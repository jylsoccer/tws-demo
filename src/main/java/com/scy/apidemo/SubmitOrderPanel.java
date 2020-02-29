/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.scy.apidemo;

import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types;
import com.scy.apidemo.util.TCombo;
import com.scy.apidemo.util.UpperField;
import com.scy.apidemo.util.VerticalPanel;

import javax.swing.*;
import java.awt.*;

public class SubmitOrderPanel extends VerticalPanel {

	Order m_order = new Order();

	final TCombo<String> m_account = new TCombo<String>("jylsoc");
	final TCombo<Types.Action> m_action = new TCombo<Types.Action>( Types.Action.values() );
	final JTextField m_modelCode = new JTextField();
	final UpperField m_quantity = new UpperField( "100");
	final UpperField m_displaySize = new UpperField();
	final TCombo<OrderType> m_orderType = new TCombo<OrderType>( OrderType.values() );
	final UpperField m_lmtPrice = new UpperField( "200");
	final UpperField m_auxPrice = new UpperField();
	final TCombo<Types.TimeInForce> m_tif = new TCombo<Types.TimeInForce>( Types.TimeInForce.values() );
	final UpperField m_lmtPriceOffset = new UpperField();
	final UpperField m_triggerPrice = new UpperField();

	public SubmitOrderPanel() {
		VerticalPanel submitPanel = new VerticalPanel();
		m_orderType.removeItemAt( 0); // remove None

		m_account.setSelectedItem( m_order.account() != null ? m_order.account() : "jylsoc" );
		m_modelCode.setText( m_order.modelCode() );
		m_action.setSelectedItem( m_order.action() );
		m_quantity.setText( m_order.totalQuantity());
		m_displaySize.setText( m_order.displaySize());
		m_orderType.setSelectedItem( m_order.orderType() );
		m_lmtPrice.setText( m_order.lmtPrice());
		m_auxPrice.setText( m_order.auxPrice());
		m_tif.setSelectedItem( m_order.tif());
		m_lmtPriceOffset.setText(m_order.lmtPriceOffset());
		m_triggerPrice.setText(m_order.triggerPrice());

		submitPanel.add( "Account", m_account);
		m_modelCode.setColumns(7);
		submitPanel.add( "Model code", m_modelCode);
		submitPanel.add( "Action", m_action);
		submitPanel.add( "Quantity", m_quantity);
		submitPanel.add( "Display size", m_displaySize);
		submitPanel.add( "Order type", m_orderType);
		submitPanel.add( "Limit price", m_lmtPrice);
		submitPanel.add("Limit price offset", m_lmtPriceOffset);
		submitPanel.add("Trigger price", m_triggerPrice);
		submitPanel.add( "Aux price", m_auxPrice);
		submitPanel.add( "Time-in-force", m_tif);

		JButton submitBut = new JButton( "下单");
		JButton resetBut = new JButton( "重置");

		JPanel butsPanel = new JPanel();
		butsPanel.add(submitBut);
		butsPanel.add(resetBut);

		butsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

		add(submitPanel);
		add(butsPanel);
	}

	private void onOK() {
		m_order.account( m_account.getText().toUpperCase() );
		m_order.modelCode( m_modelCode.getText().trim() );
		m_order.action( m_action.getSelectedItem() );
		m_order.totalQuantity( m_quantity.getDouble() );
		m_order.displaySize( m_displaySize.getInt() );
		m_order.orderType( m_orderType.getSelectedItem() );
		m_order.lmtPrice( m_lmtPrice.getDouble() );
		m_order.auxPrice( m_auxPrice.getDouble() );
		m_order.tif( m_tif.getSelectedItem() );
		m_order.lmtPriceOffset(m_lmtPriceOffset.getDouble());
		m_order.triggerPrice(m_triggerPrice.getDouble());
	}

	public static void main(String[] args) {
		SubmitOrderPanel p = new SubmitOrderPanel();

		JFrame f = new JFrame();
		f.add( p);

		f.setSize( 280, 390);
		f.setVisible( true);
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
	}
}
