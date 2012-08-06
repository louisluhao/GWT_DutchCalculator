package com.louis.calculator.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.louis.calculator.beans.DutchUser;
import com.louis.calculator.beans.DutchGroup;
import com.louis.calculator.ui.ListItem;

public class Calculator implements EntryPoint {

	private static String devModel = "?gwt.codesvr=127.0.0.1:9997";

	Label welcome = new Label();
	CalculatorUserServiceAsync calculatorUserService = GWT
			.create(CalculatorUserService.class);

	DutchUser currentUser;

	/*
	 * Group Control Panel
	 */
	List<ListItem> groupLiList = new ArrayList<ListItem>();
	Button deleteGroup;

	/*
	 * register new group panel
	 */
	Button addGroup;
	TextBox registerGroupNameBox = new TextBox();
	Label registerGroupNameTip = new Label();

	public void onModuleLoad() {
		checkLogIn();
	}

	private void afterLogin() {
		buildGroupBtns();
		buildRegisterGroupPanel();
		List<String> grouplist = currentUser.getGroupList();
		if (grouplist != null) {
			buildGroupList(grouplist);
		}
	}

	private void buildRegisterGroupPanel() {
		addGroup = new Button("Register");
		addGroup.setStyleName("btn btn-primary");
		RootPanel.get("registerNewGroupButton").add(addGroup);
		RootPanel.get("registerGroupName").add(registerGroupNameBox);
		RootPanel.get("registerGroupNameTip").add(registerGroupNameTip);

		addGroup.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				DutchGroup newgroup = new DutchGroup(registerGroupNameBox
						.getText(), currentUser.getUsername());
				calculatorUserService.addGroupToCurrentUser(newgroup,
						new AsyncCallback<Boolean>() {

							public void onSuccess(Boolean result) {
								clickElement(DOM
										.getElementById("closeRegisterGroup"));
								refreshGroupList();
							}

							public void onFailure(Throwable caught) {
								Window.alert("server connection fails");
								clickElement(DOM
										.getElementById("closeRegisterGroup"));
							}
						});
			}
		});
		
		registerGroupNameBox.addChangeHandler(new ChangeHandler() {
			
			public void onChange(ChangeEvent event) {
				registerGroupNameCheck();
			}
		});
	}
	
	private void registerGroupNameCheck(){
		if(registerGroupNameBox.getText().length()<1){
			registerGroupNameTip.setText("must input groupname");
			RootPanel.get("registerGroupnameGroup").setStyleName(
					"control-group error");
			return;
		}
		
		calculatorUserService.ifGroupExist(registerGroupNameBox.getText(), new AsyncCallback<Boolean>() {
			
			public void onSuccess(Boolean result) {
				if(result == true){
					registerGroupNameTip.setText("group name already exist");
					RootPanel.get("registerGroupnameGroup").setStyleName(
							"control-group error");
					return;
				}else{
					registerGroupNameTip.setText("");
					RootPanel.get("registerGroupnameGroup").setStyleName(
							"control-group success");
					return;
				}
			}
			
			public void onFailure(Throwable caught) {
				Window.alert("server connection error!");
				
			}
		});
	}

	private native void clickElement(Element elem) /*-{
		elem.click();
	}-*/;

	private void refreshGroupList() {
		calculatorUserService.checkUserLogin(new AsyncCallback<DutchUser>() {

			public void onSuccess(DutchUser result) {
				currentUser = result;
				buildGroupList(currentUser.getGroupList());
			}

			public void onFailure(Throwable caught) {
				Window.alert("server connection fails");
			}
		});

	}

	/*
	 * Build add and delete Buttons to group select panel
	 */
	private void buildGroupBtns() {
		deleteGroup = new Button("<i class=\"icon-search\"></i>");
		deleteGroup.setStyleName("btn");
		RootPanel.get("groupBtns").add(deleteGroup);
	}

	private void buildGroupList(List<String> grouplist) {
		groupLiList.clear();
		RootPanel.get("groupList").clear();
		for (final String group : grouplist) {
			ListItem item = new ListItem();
			Anchor grouplink = new Anchor();
			grouplink.setText(group);
			grouplink.setHref("#");
			item.add(grouplink);
			grouplink.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					clearGroupListActive();
					((Anchor) event.getSource()).getParent().setStyleName(
							"active");
				}
			});
			RootPanel.get("groupList").add(item);
			groupLiList.add(item);
		}

	}

	protected void clearGroupListActive() {
		for (ListItem item : groupLiList) {
			item.setStyleName("");
		}
	}

	private void checkLogIn() {
		calculatorUserService.checkUserLogin(new AsyncCallback<DutchUser>() {

			public void onSuccess(DutchUser result) {
				if (!result.isUserExist()) {
					Window.alert("please log in");
					Window.Location.replace(GWT.getHostPageBaseURL()
							+ "?gwt.codesvr=127.0.0.1:9997");
				} else {
					RootPanel.get("welcome").add(welcome);
					welcome.setText("log out" + result.getUsername());
					currentUser = result;
					afterLogin();
				}
			}

			public void onFailure(Throwable caught) {
				Window.alert("server connection error");
				Window.Location.replace(GWT.getHostPageBaseURL()
						+ "?gwt.codesvr=127.0.0.1:9997");
			}
		});
		welcome.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				calculatorUserService.logout(new AsyncCallback<Void>() {

					public void onSuccess(Void result) {
						Window.Location.replace(GWT.getHostPageBaseURL()
								+ devModel);
					}

					public void onFailure(Throwable caught) {
						Window.alert("Server connection error");
						Window.Location.replace(GWT.getHostPageBaseURL()
								+ devModel);
					}
				});
			}
		});
	}

}
