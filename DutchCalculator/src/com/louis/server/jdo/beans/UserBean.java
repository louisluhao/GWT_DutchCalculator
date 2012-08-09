package com.louis.server.jdo.beans;

import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable
public class UserBean {
	
	@PrimaryKey
	@Persistent
	private String username;
	
	@Persistent
	private String password;
	
	@Persistent
	private ArrayList<String> groupIDs = new ArrayList<String>();
	
	@Persistent
	private ArrayList<String> applyGroupIDs = new ArrayList<String>();

	public UserBean(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public ArrayList<String> getGroupIDs() {
		return new ArrayList<String>(groupIDs);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * add group to current user, false if group already exist
	 * @param newGroupName
	 * @return
	 */
	public boolean addGroup(String newGroupName){
		if(this.groupIDs.contains(newGroupName)){
			return false;
		}else{
			this.groupIDs.add(newGroupName);
			return true;
		}

	}
	
	public boolean addApplyGroup(String newGroupName){
		if(this.applyGroupIDs.contains(newGroupName) || this.groupIDs.contains(newGroupName)){
			return false;
		}else{
			this.applyGroupIDs.add(newGroupName);
			return true;
		}
	}

	public ArrayList<String> getApplyGroupIDs() {
		return new ArrayList<String>(applyGroupIDs);
	}
	
	public boolean applyGroupConfirm(String group){
		if(applyGroupIDs.contains(group)){
			applyGroupIDs.remove(group);
			groupIDs.add(group);
			return true;
		}else{
			return false;
		}
	}
}
