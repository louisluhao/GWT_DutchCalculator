package com.louis.server.jdo.beans;

import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


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
	
	@Persistent
	private ArrayList<String> applyUserList = new ArrayList<String>();

	public GroupBean(String groupName) {
		super();
		this.groupName = groupName;
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
	
	public ArrayList<String> getApplyUserList() {
		return new ArrayList<String>(applyUserList);
	}

	public void setUserList(ArrayList<String> userList) {
		this.userList.clear();
		this.userList.addAll(userList);
	}

	public void setBillList(ArrayList<String> billList) {
		this.billList.clear();
		this.billList.addAll(billList);
	}

	public void setAdminUserList(ArrayList<String> adminUserList) {
		this.adminUserList.clear();
		this.adminUserList.addAll(adminUserList);
	}
	
	public void setApplyUserList(ArrayList<String> applyUserList) {
		this.applyUserList.clear();
		this.applyUserList.addAll(applyUserList);
	}
	
	public boolean addApplyUser(String username){
		if(this.userList.contains(username) || this.applyUserList.contains(username)){
			return false;
		}else{
			this.applyUserList.add(username);
			return true;
		}
	}

	public boolean confirmApplyUser(String username){
		if(applyUserList.contains(username)){
			applyUserList.remove(username);
			userList.add(username);
			return true;
		}else{
			return false;
		}
	}
}
