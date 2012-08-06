package com.louis.login.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.louis.login.beans.DutchUser;
import com.louis.login.beans.LoginInfo;

@RemoteServiceRelativePath("userserver")
public interface DutchUserService extends RemoteService {
	/**
	 * check if the user is exist in datastore
	 * @param username
	 * @return true - user exist <p>false - user is not exist
	 */
	public Boolean CheckUsernameExist(String username);
	
	/**
	 * Register a new User
	 * Will throw UserExistException if the user is already in the database
	 * @return true - user success registered <p>false - user already exist
	 */
	public Boolean RegisterUser(DutchUser newUser);
	
	public LoginInfo login(DutchUser user);
}
