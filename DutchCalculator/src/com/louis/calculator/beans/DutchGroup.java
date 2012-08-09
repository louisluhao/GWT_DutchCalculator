package com.louis.calculator.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;

public class DutchGroup implements IsSerializable {

	private static final long serialVersionUID = 1L;

	private String groupName;
	private ArrayList<String> userList = new ArrayList<String>();
	private ArrayList<String> billList = new ArrayList<String>();
	private ArrayList<String> adminUserList = new ArrayList<String>();
	private ArrayList<String> applyUserlist = new ArrayList<String>();

	public DutchGroup() {
		super();
	}

	public DutchGroup(String groupName) {
		super();
		this.groupName = groupName;
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

	public ArrayList<String> getApplyUserlist() {
		return applyUserlist;
	}

	public void setApplyUserlist(ArrayList<String> applyUserlist) {
		this.applyUserlist = applyUserlist;
	}

	public void setUserList(ArrayList<String> userList) {
		this.userList = userList;
	}

	public void setBillList(ArrayList<String> billList) {
		this.billList = billList;
	}

	public void setAdminUserList(ArrayList<String> adminUserList) {
		this.adminUserList = adminUserList;
	}
	
	public boolean isAdminByUsername(String username){
		return adminUserList.contains(username);
	}

}
