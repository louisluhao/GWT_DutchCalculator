package com.louis.calculator.client;

import com.google.gwt.core.client.GWT;

public class CalculatorServerProxy {
	private static CalculatorUserServiceAsync calculateService = GWT.create(CalculatorUserService.class);
	
	public static CalculatorUserServiceAsync getCalculatorServer(){
		return calculateService;
	}
}
