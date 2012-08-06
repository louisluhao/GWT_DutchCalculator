package com.louis.calculator.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DutchUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	boolean userExist;
	private String username;
	private ArrayList<String> groupList;

	public DutchUser() {
		super();
	}

	public DutchUser(boolean userExist, String username) {
		super();
		this.userExist = userExist;
		this.username = username;
	}
	
	public DutchUser(boolean userExist, String username,
			ArrayList<String> groupList) {
		super();
		this.userExist = userExist;
		this.username = username;
		this.groupList = groupList;
	}

	public boolean isUserExist() {
		return userExist;
	}

	public String getUsername() {
		return username;
	}

	public List<String> getGroupList() {
		return groupList;
	}

}
