package com.louis.calculator.client;

import static com.louis.calculator.client.ui.component.ListHeader.getTableHeader;
import static com.louis.calculator.client.algo.BillAlgo.*;

import java.util.List;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.louis.calculator.beans.DutchBill;
import com.louis.calculator.beans.DutchGroup;
import com.louis.calculator.beans.DutchUser;

public class GroupHomePanel {
	DutchGroup currentGroup;
	DutchUser currentUser;
	List<DutchBill> currentBills;

	CalculatorUserServiceAsync calculatorUserService = CalculatorServerProxy.getCalculatorServer();

	Anchor homePanelLink;

	/*
	 * Group Home Panel
	 */
	Label groupTitle = new Label();
	FlexTable groupUserListTable = new FlexTable();

	public GroupHomePanel() {
		groupUserListTable.setStyleName("table table-striped table-bordered");
		homePanelLink = getHomePanelLink();
		RootPanel.get("homePanelLink").add(homePanelLink);
		RootPanel.get("groupTitle").add(groupTitle);
	}

	private Anchor getHomePanelLink() {
		Anchor anchor = new Anchor();
		anchor.getElement().setAttribute("href", "#groupHomePanel");
		anchor.getElement().setAttribute("data-toggle", "tab");
		anchor.setHTML("<i class=\"icon-star\"></i> Welcome");
		return anchor;
	}

	public void RefreshGroup(DutchGroup currentGroup, DutchUser currentUser,
			List<DutchBill> currentBills) {
		this.currentGroup = currentGroup;
		this.currentUser = currentUser;
		this.currentBills = currentBills;
		setupHomePanelLink();
		refreshPanel();
	}

	private void setupHomePanelLink() {
		homePanelLink.setHTML("<i class=\"icon-home\"></i> Group Home");
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
		createHeaders();
		createRowsByGroup();
		RootPanel.get("groupUserListTable").add(groupUserListTable);

	}

	private void createHeaders() {
		groupUserListTable.setWidget(0, 0, getTableHeader("Username"));
		groupUserListTable.setWidget(0, 1, getTableHeader("Current Balance"));
		groupUserListTable.setWidget(0, 2, getTableHeader("Potential Balance"));
	}

	private void createRowsByGroup() {
		List<String> userList = currentGroup.getUserList();
		int row = 1;
		for (String user : userList) {
			groupUserListTable.setText(row, 0, user);
			double currentBalance = formatDouble(getBalanceValid(user, currentBills));
			groupUserListTable.setWidget(row, 1, getBalanceFormater(currentBalance));
			double potentialBalance = formatDouble(getBalanceAll(user, currentBills));
			groupUserListTable.setWidget(row, 2, getBalanceFormater(potentialBalance));
			row++;
		}
	}

	private Label getBalanceFormater(double balance) {
		Label bl = new Label();
		bl.setText("$" + NumberFormat.getFormat("#.00").format(balance));
		if (balance > 0) {
			bl.setStyleName(" positiveNumber");
		} else if (balance < 0) {
			bl.setStyleName(" negativeNumber");
		}
		return bl;
	}

}
