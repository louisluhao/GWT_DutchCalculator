package com.louis.calculator.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.louis.calculator.beans.DutchGroup;
import com.louis.calculator.beans.DutchUser;

public class GroupTab {
	DutchGroup currentGroup;
	DutchUser currentUser;

	CalculatorUserServiceAsync calculatorUserService = CalculatorServerProxy.getCalculatorServer();

	GroupHomePanel groupHomePanel = new GroupHomePanel();
	GroupAdminPanel adminPanel = new GroupAdminPanel(this);

	public void RefreshGroup(String group, DutchUser currentUser) {
		this.currentUser = currentUser;
		calculatorUserService.getGroupByName(group, currentUser.getUsername(), new AsyncCallback<DutchGroup>() {

			public void onSuccess(DutchGroup result) {
				currentGroup = result;
				refreshPanels();
			}

			public void onFailure(Throwable caught) {
				Window.alert("server connect error");
			}
		});
	}
	
	public void refreshUserAndGroup(final String username, String groupname){
		calculatorUserService.getGroupByName(groupname, username, new AsyncCallback<DutchGroup>() {

			public void onSuccess(DutchGroup result) {
				currentGroup = result;
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
		groupHomePanel.RefreshGroup(currentGroup, currentUser);
		adminPanel.RefreshGroup(currentGroup, currentUser);
	}
}
