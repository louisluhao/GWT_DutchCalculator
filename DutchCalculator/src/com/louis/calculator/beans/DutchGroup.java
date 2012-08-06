package com.louis.calculator.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class DutchGroup implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String groupName;
	private ArrayList<String> userList = new ArrayList<String>();
	private ArrayList<String> billList = new ArrayList<String>();
	private ArrayList<String> adminUserList = new ArrayList<String>();
	
	public DutchGroup() {
		super();
	}

	public DutchGroup(String groupName, String createUser) {
		super();
		this.groupName = groupName;
		this.userList.add(createUser);
		this.adminUserList.add(createUser);
	}

	public String getGroupName() {
		return groupName;
	}

	public ArrayList<String> getUserList() {
		return userList;
	}

	public ArrayList<String> getBillList() {
		return billList;
	}

	public ArrayList<String> getAdminUserList() {
		return adminUserList;
	}
	

	
}
