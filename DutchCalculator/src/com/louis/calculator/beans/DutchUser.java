package com.louis.calculator.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.List;

public class DutchUser implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	boolean userExist;
	private String username;
	private ArrayList<String> groupList;
	private ArrayList<String> applyGroupList;

	public DutchUser() {
		super();
	}

	public DutchUser(boolean userExist, String username) {
		super();
		this.userExist = userExist;
		this.username = username;
	}
	
	public DutchUser(boolean userExist, String username,
			ArrayList<String> groupList, ArrayList<String> applyGroupList) {
		super();
		this.userExist = userExist;
		this.username = username;
		this.groupList = groupList;
		this.applyGroupList = applyGroupList;
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

	public ArrayList<String> getApplyList() {
		return applyGroupList;
	}	


}
