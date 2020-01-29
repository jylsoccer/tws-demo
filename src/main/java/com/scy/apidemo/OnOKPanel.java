package com.scy.apidemo;

import com.ib.client.OrderCondition;
import com.scy.apidemo.util.VerticalPanel;

public abstract class OnOKPanel extends VerticalPanel {
	public abstract OrderCondition onOK();
}