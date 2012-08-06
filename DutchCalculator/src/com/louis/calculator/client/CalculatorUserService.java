package com.louis.calculator.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.louis.calculator.beans.DutchUser;
import com.louis.calculator.beans.DutchGroup;

@RemoteServiceRelativePath("calculatorUser")
public interface CalculatorUserService extends RemoteService {
	public DutchUser checkUserLogin();
	public Void logout();
	public Boolean ifGroupExist(String groupname);
	public Boolean addGroupToCurrentUser(DutchGroup newgroup);
}
