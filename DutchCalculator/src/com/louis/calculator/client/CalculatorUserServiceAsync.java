package com.louis.calculator.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.louis.calculator.beans.DutchUser;
import com.louis.calculator.beans.DutchGroup;

public interface CalculatorUserServiceAsync {
	public void checkUserLogin(AsyncCallback<DutchUser> async);
	public void logout(AsyncCallback<Void> async);
	public void ifGroupExist(String groupname, AsyncCallback<Boolean> async);
	
	/**
	 * add new group to current user;
	 * !!!new Group should just has one user in "user list" and "admin user list" !!!
	 * @param newGroup
	 * @param async
	 */
	public void addGroupToCurrentUser(DutchGroup newGroup, AsyncCallback<Boolean> async);
	
	public void searchGroupByName(String groupName, AsyncCallback<ArrayList<String>> async);
	
	public void joinGroup(String groupName, String username, AsyncCallback<Boolean> async);
	
	public void getGroupByName(String groupName,String username, AsyncCallback<DutchGroup> async);

	public void ConfirmApplyUser(String groupname, String username, AsyncCallback<Void> async);
}
