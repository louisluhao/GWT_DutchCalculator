package com.louis.calculator.client.ui.component;

import com.google.gwt.user.client.ui.Label;

public class ListHeader {
	public static Label getTableHeader(String string) {
		Label header = new Label(string);
		header.setStyleName("listheader");
		return header;
	}
}
