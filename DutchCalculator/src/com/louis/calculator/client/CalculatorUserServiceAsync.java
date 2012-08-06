package com.louis.calculator.client;

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
}
