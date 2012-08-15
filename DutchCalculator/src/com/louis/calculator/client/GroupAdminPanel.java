package com.louis.calculator.client;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	
	/*
	 * user manage list
	 */
	VerticalPanel userListPanel;
	Label userListTitle;
	FlexTable userListTable;
	Label userListHeader;

	public GroupAdminPanel(GroupTab tab) {
		this.groupTab = tab;

		adminLink = getAdminLink();
		buildApplyListPanel();
		buildUserListPanel();

		RootPanel.get("groupApplyListTable").add(applyListPanel);
		RootPanel.get("groupUserManagementPanel").add(userListPanel);
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
		applyListPanel.setStyleName("paddingPanel");
		applyListPanel.add(applyListTitle);
		applyListPanel.add(applyListTable);
	}
	
	private void buildUserListPanel() {
		userListTitle = new Label();
		userListTitle.getElement().setInnerHTML("<h2>User Management</h2>");
		
		userListTable = new FlexTable();
		userListHeader = new Label("Username");
		userListTable.setStyleName("table table-striped table-bordered");
		userListHeader.setStyleName("listheader");
		
		userListPanel = new VerticalPanel();
		userListPanel.setStyleName("paddingPanel");
		userListPanel.add(userListTitle);
		userListPanel.add(userListTable);
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
		refreshUserListTable();
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
		} else {
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

				calculatorUserService.ConfirmApplyUser(currentGroup.getGroupName(), confirmUsername,
						new AsyncCallback<Void>() {
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

	private void addClickHandlerToRejectButton(Button rejectButton,final String rejectUsername) {
		rejectButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				calculatorUserService.RejectApplyUser(currentGroup.getGroupName(),
						rejectUsername, new AsyncCallback<Void>() {

							public void onSuccess(Void result) {
								groupTab.refreshUserAndGroup(currentUser.getUsername(), currentGroup.getGroupName());
							}

							public void onFailure(Throwable caught) {
								Window.alert("SERVER CONNECT ERROR WHEN REJECT");
							}
						});
			}
		});
	}

	private void refreshUserListTable() {
		userListTable.removeAllRows();
		buildUserListTableHeader();
		buildUserListTableByCurrentGroup();
		
	}

	private void buildUserListTableHeader() {
		userListTable.setWidget(0, 0, userListHeader);
		userListTable.setText(0, 1, "");
	}

	private void buildUserListTableByCurrentGroup() {
		int row = 1;
		for(String username : currentGroup.getUserList()){
			userListTable.setText(row, 0, username);
			userListTable.setWidget(row, 1, getDeleteUserBtn(username));
			row ++;
		}
	}

	private Widget getDeleteUserBtn(String username) {
		if(username.equals(currentUser.getUsername())){
			return null;
		}
		Button btn = new Button("Delete User");
		btn.setStyleName("btn btn-mini btn-danger");
		addClickListenerToDeleteUserBtn(btn , username);
		return btn;
	}

	private void addClickListenerToDeleteUserBtn(Button btn,final String username) {
		btn.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				if(Window.confirm("Do you really want to delete user "+ username + " ?")){
					deleteUser(username, currentGroup.getGroupName());
				}
			}
		});
	}

	protected void deleteUser(final String username,final String groupName) {
		calculatorUserService.deleteUser(username, groupName, new AsyncCallback<Void>() {
			
			public void onSuccess(Void result) {
				groupTab.refreshUserAndGroup(username, groupName);
			}
			
			public void onFailure(Throwable caught) {
				Window.alert("server connect error when delete user " + username);
			}
		});
	}
}
