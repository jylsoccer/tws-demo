/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.scy.apidemo;

import com.ib.client.*;
import com.ib.controller.ApiController;
import com.scy.apidemo.util.TCombo;
import com.scy.apidemo.util.UpperField;
import com.scy.apidemo.util.VerticalPanel;

import javax.swing.*;
import java.awt.*;

public class NewSubmitOrderPanel extends VerticalPanel {

	private boolean m_editContract;

	private Contract m_contract;
	private Order m_order;

	private ContractPanel m_contractPanel;
	private OrderPanel m_orderPanel;


	public NewSubmitOrderPanel() {
		m_contract = new Contract();

		m_order = new Order();
		m_order.totalQuantity(100);
		m_order.lmtPrice(1);

		m_contractPanel = new ContractPanel( m_contract);
		m_orderPanel = new OrderPanel();

		JTabbedPane tabbedPanel = new JTabbedPane();

		tabbedPanel.addTab( "合约", m_contractPanel);
		tabbedPanel.addTab( "订单", m_orderPanel);

		JButton submitBut = new JButton( "下单");
		submitBut.addActionListener(
				(e) -> onTransmitOrder()
		);

		JButton resetBut = new JButton( "重置");
		resetBut.addActionListener(
				(e) -> onResetOrder()
		);

		JPanel butsPanel = new JPanel();
		butsPanel.add(submitBut);
		butsPanel.add(resetBut);

		butsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

		add(tabbedPanel);
		add(butsPanel);
	}

	private String getVal(Types.ComboParam param) {
		if (m_order.smartComboRoutingParams() != null) {
			for (TagValue tv : m_order.smartComboRoutingParams() ) {
				if (tv.m_tag.equals( param.toString() ) ) {
					return tv.m_value;
				}
			}
		}
		return "";
	}

	private void onTransmitOrder() {
		scrape();

		ApiDemo.INSTANCE.controller().placeOrModifyOrder( m_contract, m_order, new ApiController.IOrderHandler() {
			@Override public void orderState(OrderState orderState) {
				SwingUtilities.invokeLater( new Runnable() {
					@Override public void run() {
					}
				});
			}
			@Override public void orderStatus(OrderStatus status, double filled, double remaining, double avgFillPrice, long permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
			}
			@Override public void handle(int errorCode, final String errorMsg) {
				m_order.orderId( 0);
			}
		});
		m_order.orderId(0);
	}

	private void onResetOrder() {
		m_contract = new Contract();
		m_contractPanel.updateContractPanel(m_contract);

		m_order = new Order();
		m_order.totalQuantity(100);
		m_order.lmtPrice(1);
		updateOrderPanel(m_order);
	}

	private void scrape() {
		m_order.smartComboRoutingParams().clear();
		m_contractPanel.onOK();
		m_orderPanel.onOK();
	}

	class OrderPanel extends VerticalPanel {
		final TCombo<String> m_account = new TCombo<String>( ApiDemo.INSTANCE.accountList().get( 0) );
		final TCombo<Types.Action> m_action = new TCombo<Types.Action>( Types.Action.values() );
		final JTextField m_modelCode = new JTextField();
		final UpperField m_quantity = new UpperField( "100");
		final UpperField m_displaySize = new UpperField();
		final TCombo<OrderType> m_orderType = new TCombo<OrderType>( OrderType.values() );
		final UpperField m_lmtPrice = new UpperField( "200");
		final UpperField m_auxPrice = new UpperField();
		final TCombo<Types.TimeInForce> m_tif = new TCombo<Types.TimeInForce>( Types.TimeInForce.values() );
		final JCheckBox m_nonGuaranteed = new JCheckBox();
		final UpperField m_lmtPriceOffset = new UpperField();
		final UpperField m_triggerPrice = new UpperField();

		OrderPanel() {
			m_orderType.removeItemAt( 0); // remove None

			m_account.setSelectedItem( m_order.account() != null ? m_order.account() : ApiDemo.INSTANCE.accountList().get( 0) );
			m_modelCode.setText( m_order.modelCode() );
			m_action.setSelectedItem( m_order.action() );
			m_quantity.setText( m_order.totalQuantity());
			m_displaySize.setText( m_order.displaySize());
			m_orderType.setSelectedItem( m_order.orderType() );
			m_lmtPrice.setText( m_order.lmtPrice());
			m_auxPrice.setText( m_order.auxPrice());
			m_tif.setSelectedItem( m_order.tif());
			m_nonGuaranteed.setSelected( getVal( Types.ComboParam.NonGuaranteed).equals( "1") );
			m_lmtPriceOffset.setText(m_order.lmtPriceOffset());
			m_triggerPrice.setText(m_order.triggerPrice());

			add( "Account", m_account);
			m_modelCode.setColumns(7);
			add( "Model code", m_modelCode);
			add( "Action", m_action);
			add( "Quantity", m_quantity);
			add( "Display size", m_displaySize);
			add( "Order type", m_orderType);
			add( "Limit price", m_lmtPrice);
			add("Limit price offset", m_lmtPriceOffset);
			add("Trigger price", m_triggerPrice);
			add( "Aux price", m_auxPrice);
			add( "Time-in-force", m_tif);
			if (m_contract.isCombo() ) {
				add( "Non-guaranteed", m_nonGuaranteed);
			}
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

			if (m_contract.isCombo() ) {
				TagValue tv = new TagValue( Types.ComboParam.NonGuaranteed.toString(), m_nonGuaranteed.isSelected() ? "1" : "0");
				m_order.smartComboRoutingParams().add( tv);
			}
		}
	}

	public Contract getM_contract() {
		return m_contract;
	}

	public void setM_contract(Contract m_contract) {
		this.m_contract = m_contract;

		m_contractPanel.updateContractPanel(m_contract);
	}

	public Order getM_order() {
		return m_order;
	}

	public void setM_order(Order m_order) {
		this.m_order = m_order;

		updateOrderPanel(m_order);
	}

	private void updateOrderPanel(Order order) {
		m_orderPanel.m_orderType.removeItemAt( 0); // remove None
		m_orderPanel.m_account.setSelectedItem( order.account() != null ? order.account() : ApiDemo.INSTANCE.accountList().get( 0) );
		m_orderPanel.m_modelCode.setText( order.modelCode() );
		m_orderPanel.m_action.setSelectedItem( order.action() );
		m_orderPanel.m_quantity.setText( order.totalQuantity());
		m_orderPanel.m_displaySize.setText( order.displaySize());
		m_orderPanel.m_orderType.setSelectedItem( order.orderType() );
		m_orderPanel.m_lmtPrice.setText( order.lmtPrice());
		m_orderPanel.m_auxPrice.setText( order.auxPrice());
		m_orderPanel.m_tif.setSelectedItem( order.tif());
		m_orderPanel.m_nonGuaranteed.setSelected( getVal( Types.ComboParam.NonGuaranteed).equals( "1") );
		m_orderPanel.m_lmtPriceOffset.setText(order.lmtPriceOffset());
		m_orderPanel.m_triggerPrice.setText(order.triggerPrice());
	}
}