package com.louis.calculator.client;

import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.louis.calculator.beans.DutchBill;
import com.louis.calculator.beans.DutchGroup;
import com.louis.calculator.beans.DutchUser;
import com.louis.calculator.beans.GroupRelatedInfo;

public class GroupTab {
	DutchGroup currentGroup;
	DutchUser currentUser;
	List<DutchBill> currentBills;

	CalculatorUserServiceAsync calculatorUserService = CalculatorServerProxy.getCalculatorServer();

	GroupHomePanel homePanel = new GroupHomePanel();
	GroupBillPanel billPanel = new  GroupBillPanel(this);
	GroupAdminPanel adminPanel = new GroupAdminPanel(this);

	
	/*
	 * order of refresh: 
	 * Group -> Bills -> User
	 */
	
	public void RefreshGroup(String group, DutchUser currentUser) {
		this.currentUser = currentUser;
		calculatorUserService.getGroupAndBillsByName(group, currentUser.getUsername(), new AsyncCallback<GroupRelatedInfo>() {

			public void onSuccess(GroupRelatedInfo result) {
				currentGroup = result.getGroup();
				currentBills = result.getBills();
				refreshPanels();
			}

			public void onFailure(Throwable caught) {
				Window.alert("server connect error");
			}
		});
	}
	
	public void refreshUserAndGroup(final String username, final String groupname){
		calculatorUserService.getGroupAndBillsByName(groupname, username, new AsyncCallback<GroupRelatedInfo>() {

			public void onSuccess(GroupRelatedInfo result) {
				currentGroup = result.getGroup();
				currentBills = result.getBills();
				refreshUser(username);
			}

			public void onFailure(Throwable caught) {
				Window.alert("server connect error during refresh group");
			}
		});
	}

	protected void refreshUser(String username) {
		calculatorUserService.checkUserLogin(new AsyncCallback<DutchUser>() {
			
			public void onSuccess(DutchUser result) {
				currentUser = result;
				refreshPanels();
			}
			
			public void onFailure(Throwable caught) {
				Window.alert("server connect error during refresh user info");
			}
		});
	}

	protected void refreshPanels() {
		homePanel.RefreshGroup(currentGroup, currentUser, currentBills);
		billPanel.RefreshGroup(currentGroup, currentUser, currentBills);
		adminPanel.RefreshGroup(currentGroup, currentUser);
	}
}
