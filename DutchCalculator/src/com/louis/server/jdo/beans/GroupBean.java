package com.louis.server.jdo.beans;

import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.louis.calculator.beans.DutchGroup;

@PersistenceCapable
public class GroupBean {
	
	@PrimaryKey
	@Persistent
	private String groupName;
	
	@Persistent
	private ArrayList<String> userList = new ArrayList<String>();
	
	@Persistent
	private ArrayList<String> billList = new ArrayList<String>();
	
	@Persistent
	private ArrayList<String> adminUserList = new ArrayList<String>();

	public GroupBean(String groupName) {
		super();
		this.groupName = groupName;
	}
	
	public GroupBean(DutchGroup group){
		this.groupName = group.getGroupName();
		this.userList.addAll(group.getUserList());
		this.billList.addAll(group.getBillList());
		this.adminUserList.addAll(group.getAdminUserList());
	}

	public String getGroupName() {
		return groupName;
	}

	public ArrayList<String> getUserList() {
		return new ArrayList<String>(userList);
	}

	public ArrayList<String> getBillList() {
		return new ArrayList<String>(billList);
	}

	public ArrayList<String> getAdminUserList() {
		return new ArrayList<String>(adminUserList);
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
	
}
