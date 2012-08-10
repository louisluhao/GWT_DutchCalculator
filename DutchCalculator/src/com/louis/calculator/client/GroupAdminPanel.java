package com.louis.calculator.client;

import java.util.List;

import org.w3c.dom.Element;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.louis.calculator.beans.DutchGroup;
import com.louis.calculator.beans.DutchUser;

public class GroupAdminPanel {
	GroupTab groupTab;

	DutchGroup currentGroup;
	DutchUser currentUser;

	boolean isVisible = false;

	Anchor adminLink;

	CalculatorUserServiceAsync calculatorUserService = CalculatorServerProxy.getCalculatorServer();

	/*
	 * apply list
	 */
	VerticalPanel applyListPanel;
	Label applyListTitle;
	FlexTable applyListTable;
	Label applyListHeader;

	public GroupAdminPanel(GroupTab tab) {
		this.groupTab = tab;

		adminLink = getAdminLink();
		buildApplyListPanel();

		RootPanel.get("groupApplyListTable").add(applyListPanel);
	}

	private Anchor getAdminLink() {
		Anchor anchor = new Anchor();
		anchor.getElement().setAttribute("href", "#adminPanel");
		anchor.getElement().setAttribute("class", "dropdown-toggle");
		anchor.getElement().setAttribute("data-toggle", "tab");
		anchor.setHTML("<i class=\"icon-eye-open\"></i> Admin");
		return anchor;
	}

	private void buildApplyListPanel() {
		applyListTitle = new Label();
		applyListTitle.getElement().setInnerHTML("<h2>Applied Users</h2>");

		applyListTable = new FlexTable();
		applyListHeader = new Label("Username");
		applyListTable.setStyleName("table table-striped table-bordered");
		applyListHeader.setStyleName("listheader");

		applyListPanel = new VerticalPanel();
		applyListPanel.add(applyListTitle);
		applyListPanel.add(applyListTable);
	}

	public void RefreshGroup(DutchGroup currentGroup, DutchUser currentUser) {
		this.currentGroup = currentGroup;
		this.currentUser = currentUser;
		if (currentGroup.isAdminByUsername(currentUser.getUsername())) {
			showAdminPanel();
		} else {
			hideAdminPanel();
		}
	}

	private void hideAdminPanel() {
		if (isVisible) {
			RootPanel.get("adminPanelLink").remove(adminLink);
			isVisible = false;
		}
	}

	private void showAdminPanel() {
		addTabLinkToHtml();
		refreshApplyListTable();
	}

	private void addTabLinkToHtml() {
		if (!isVisible) {
			RootPanel.get("adminPanelLink").add(adminLink);
			isVisible = true;
		}
	}

	private void refreshApplyListTable() {
		applyListTable.removeAllRows();

		if (currentGroup.getApplyUserlist().size() > 0) {
			buildApplyListTableHeader();
			buildApplyListTableByCurrentGroup();
		}else{
			applyListTable.setWidget(0, 0, applyListHeader);
			applyListTable.setText(1, 0, "Current No Applied User");			
		}
	}

	private void buildApplyListTableHeader() {
		applyListTable.setWidget(0, 0, applyListHeader);
		applyListTable.setText(0, 1, "");
		applyListTable.setText(0, 2, "");
	}

	private void buildApplyListTableByCurrentGroup() {
		List<String> applyUsers = currentGroup.getApplyUserlist();
		int row = 1;
		for (String username : applyUsers) {
			applyListTable.setText(row, 0, username);
			applyListTable.setWidget(row, 1, getConfirmApplyBtn(row, username));
			applyListTable.setWidget(row, 2, getRejectApplyBtn(row, username));
			row++;
		}
	}

	private Button getConfirmApplyBtn(final int row, final String confirmUsername) {
		Button confirmBtn = new Button("Confirm");
		confirmBtn.setStyleName("btn btn-success");

		confirmBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				calculatorUserService.ConfirmApplyUser(currentGroup.getGroupName(),
						confirmUsername, new AsyncCallback<Void>() {
							public void onSuccess(Void result) {
								groupTab.refreshUserAndGroup(currentUser.getUsername(), currentGroup.getGroupName());
							}

							public void onFailure(Throwable caught) {
								Window.alert("SERVER CONNECT ERROR WHEN CONFIRM");
							}
						});
			}
		});

		return confirmBtn;
	}

	private Widget getRejectApplyBtn(final int row, final String rejectUsername) {
		Button rejectButton = new Button("Reject");
		rejectButton.setStyleName("btn btn-danger");
		addClickHandlerToRejectButton(rejectButton, rejectUsername);
		return rejectButton;
	}

	private void addClickHandlerToRejectButton(Button rejectButton, String rejectUsername) {
		// TODO Auto-generated method stub
		
	}

}
