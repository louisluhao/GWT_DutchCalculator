package com.louis.login.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.louis.login.beans.DutchUser;
import com.louis.login.beans.LoginInfo;

public interface DutchUserServiceAsync {
	/**
	 * check if the user is exist in datastore
	 * @param username
	 * @return true - user exist <p>false - user is not exist
	 */	
	public void CheckUsernameExist(String username, AsyncCallback<Boolean> async);
	
	/**
	 * Register a new User
	 * Will throw UserExistException if the user is already in the database
	 */
	public void RegisterUser(DutchUser newUser, AsyncCallback<Boolean> async);
	
	public void login(DutchUser user, AsyncCallback<LoginInfo> async);
}
