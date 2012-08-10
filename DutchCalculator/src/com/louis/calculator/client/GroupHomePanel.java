package com.louis.calculator.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.louis.calculator.beans.DutchGroup;
import com.louis.calculator.beans.DutchUser;

public class GroupHomePanel {
	DutchGroup currentGroup;
	DutchUser currentUser;

	CalculatorUserServiceAsync calculatorUserService = CalculatorServerProxy.getCalculatorServer();
	/*
	 * no group show
	 */

	/*
	 * Group Home Panel
	 */
	Label groupTitle = new Label();
	FlexTable groupUserListTable = new FlexTable();
	Label groupUserListTableHeader = new Label("Group User List");

	public GroupHomePanel() {
		groupUserListTable.setStyleName("table table-striped table-bordered");
		groupUserListTableHeader.setStyleName("listheader");
		RootPanel.get("groupTitle").add(groupTitle);
	}

	public void RefreshGroup(DutchGroup currentGroup, DutchUser currentUser) {
		this.currentGroup = currentGroup;
		this.currentUser = currentUser;
		refreshPanel();
	}

	protected void refreshPanel() {
		if (currentGroup != null && currentGroup.getUserList() != null) {
			setupGroupUserListTable();
		} else {
			RootPanel.get("groupUserListTable").clear();
		}
	}

	private void setupGroupUserListTable() {

		groupTitle.setText(currentGroup.getGroupName());
		groupUserListTable.removeAllRows();
		groupUserListTable.setWidget(0, 0, groupUserListTableHeader);
		List<String> userList = currentGroup.getUserList();
		int row = 1;
		for (String user : userList) {
			groupUserListTable.setText(row, 0, user);
			row++;
		}
		RootPanel.get("groupUserListTable").add(groupUserListTable);

	}

}
