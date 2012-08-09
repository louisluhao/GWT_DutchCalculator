package com.louis.calculator.client;

import java.util.ArrayList;

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
	public ArrayList<String> searchGroupByName(String groupName);
	public Boolean joinGroup(String groupName, String username);
	public DutchGroup getGroupByName(String groupName, String username);
	public Void ConfirmApplyUser(String groupname, String username);
}
