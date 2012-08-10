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
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.louis.calculator.beans.DutchUser;
import com.louis.calculator.beans.DutchGroup;
import com.louis.calculator.ui.ListItem;

public class Calculator implements EntryPoint {

	 private static String devModel = "?gwt.codesvr=127.0.0.1:9997";
//	private static String devModel = "";

	Label welcome = new Label();
	CalculatorUserServiceAsync calculatorUserService = CalculatorServerProxy.getCalculatorServer();

	DutchUser currentUser;

	/*
	 * Group Control Panel
	 */
	List<ListItem> groupLiList = new ArrayList<ListItem>();

	/*
	 * register new group panel
	 */
	Button addGroup;
	TextBox registerGroupNameBox = new TextBox();
	Label registerGroupNameTip = new Label();

	/*
	 * search group panel
	 */
	VerticalPanel searchPanel = new VerticalPanel();
	HorizontalPanel searchBar = new HorizontalPanel();
	TextBox searchGroupBox = new TextBox();
	Button searchGroupBtn = new Button("Search");
	Label searchResultTableHeaderGroupName = new Label("Group Name");
	FlexTable searchResultTable = new FlexTable();
	ArrayList<String> searchResultTableModule = new ArrayList<String>();

	/*
	 * groupTab
	 */
	GroupTab groupTab = new GroupTab();

	public void onModuleLoad() {
		checkLogIn();
	}

	private void checkLogIn() {
		calculatorUserService.checkUserLogin(new AsyncCallback<DutchUser>() {

			public void onSuccess(DutchUser result) {
				if (!result.isUserExist()) {
					Window.alert("please log in");
					redirectToLoginPage();
				} else {
					currentUser = result;
					afterLogin();
				}
			}

			public void onFailure(Throwable caught) {
				Window.alert("server connection error when check log in");
				redirectToLoginPage();
			}
		});
	}

	private void redirectToLoginPage() {
		Window.Location.replace(GWT.getHostPageBaseURL() + devModel);
	}

	private void afterLogin() {
		buildNavBar();
		buildGroupBtns();
		buildRegisterGroupPanel();
		buildSearchGroupPanel();
		List<String> grouplist = currentUser.getGroupList();
		if (grouplist != null) {
			buildGroupList(grouplist);
		}
	}

	private void buildNavBar() {
		RootPanel.get("nav-user").getElement()
				.setInnerHTML("<i class=\"icon-user icon-white\"></i>" + currentUser.getUsername());

		RootPanel.get("welcome").add(welcome);
		welcome.getElement().setInnerHTML("<i class=\"icon-off icon-white\"></i>Log out");

		welcome.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				calculatorUserService.logout(new AsyncCallback<Void>() {

					public void onSuccess(Void result) {
						redirectToLoginPage();
					}

					public void onFailure(Throwable caught) {
						Window.alert("Server connection error, cannot log out");
					}
				});
			}
		});
	}

	private void buildGroupBtns() {
	}

	private void buildSearchGroupPanel() {
		RootPanel.get("searchGroupPanel").add(searchPanel);

		// init search bar
		searchBar.add(searchGroupBox);
		searchBar.add(searchGroupBtn);

		searchGroupBtn.setStyleName("btn btn-primary");
		searchResultTable.setStyleName("table table-striped");
		searchResultTableHeaderGroupName.setStyleName("listheader");

		searchPanel.add(searchBar);
		searchPanel.add(searchResultTable);

		searchGroupBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				searchGroupBtnShowLoading();
				searchGroupAndShow();
			}
		});

		searchGroupBox.addKeyPressHandler(new KeyPressHandler() {

			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					searchGroupBtnShowLoading();
					searchGroupAndShow();
				}
			}
		});
	}

	protected void searchGroupBtnRecover() {
		searchGroupBtn.setText("search");
		searchGroupBtn.setEnabled(true);
		searchGroupBtn.setStyleName("btn btn-primary");
	}

	protected void searchGroupBtnShowLoading() {
		searchGroupBtn.setText("loading group...");
		searchGroupBtn.setEnabled(false);
		searchGroupBtn.setStyleName("btn btn-primary disabled");
	}

	protected void searchGroupAndShow() {
		calculatorUserService.searchGroupByName(searchGroupBox.getText(),
				new AsyncCallback<ArrayList<String>>() {

					public void onSuccess(ArrayList<String> result) {
						searchResultTableModule = result;
						buildSearchGroupResultTableByModule();
						searchGroupBtnRecover();
					}

					public void onFailure(Throwable caught) {
						Window.alert("server connect error, try again");
						searchGroupBtnRecover();
					}
				});
	}

	private void buildSearchGroupResultTableByModule() {
		clearSearchGroupResultTable();
		searchResultTable.setWidget(0, 0, searchResultTableHeaderGroupName);
		searchResultTable.setText(0, 1, "");
		int row = 1;
		for (String groupName : searchResultTableModule) {
			searchResultTable.setText(row, 0, groupName);
			searchResultTable.setWidget(row, 1, creatJoinGroupButton(groupName));
			row++;
		}
	}

	private void setButtonToApplied(Button btn) {
		btn.setText("Wait to be approved...");
		btn.setEnabled(false);
		btn.setStyleName("btn btn-primary btn-mini disabled");
	}

	private void setButtonToJoined(Button btn) {
		btn.setText("Joined");
		btn.setEnabled(false);
		btn.setStyleName("btn btn-success btn-mini disabled");
	}

	private void setButtonToApply(Button btn) {
		btn.setText("Join");
		btn.setEnabled(true);
		btn.setStyleName("btn btn-primary btn-mini");
	}

	private Button getButtonForSearchGroupTable(String groupName) {
		Button btn = new Button();
		if (currentUser.getGroupList().contains(groupName)) {
			setButtonToJoined(btn);
		} else if (currentUser.getApplyList().contains(groupName)) {
			setButtonToApplied(btn);
		} else {
			setButtonToApply(btn);
		}
		return btn;
	}

	private Button creatJoinGroupButton(final String groupName) {
		Button joinBtn = getButtonForSearchGroupTable(groupName);
		joinBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (!(event.getSource() instanceof Button)) {
					return;
				}
				setButtonToApplied((Button) event.getSource());
				applyGroupSendServerMsg(groupName);
			}

			private void applyGroupSendServerMsg(String groupName) {
				calculatorUserService.joinGroup(groupName, currentUser.getUsername(),
						new AsyncCallback<Boolean>() {

							public void onSuccess(Boolean result) {
								if (result) {
									refreshUser();
								}
							}

							public void onFailure(Throwable caught) {
								Window.alert("server connect error, try again");
							}
						});
			}
		});
		return joinBtn;
	}

	private void buildRegisterGroupPanel() {
		addGroup = new Button("Register");
		addGroup.setStyleName("btn btn-primary");
		RootPanel.get("registerNewGroupButton").add(addGroup);
		RootPanel.get("registerGroupName").add(registerGroupNameBox);
		RootPanel.get("registerGroupNameTip").add(registerGroupNameTip);

		addGroup.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				registerGroupNameCheck(true);
			}
		});

		registerGroupNameBox.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				registerGroupNameCheck(false);
			}
		});
	}

	private void registerGroupNameCheck(final boolean isRegister) {
		if (registerGroupNameBox.getText().length() < 1) {
			registerGroupNameTip.setText("must input groupname");
			RootPanel.get("registerGroupnameGroup").setStyleName("control-group error");
			return;
		}

		calculatorUserService.ifGroupExist(registerGroupNameBox.getText(),
				new AsyncCallback<Boolean>() {

					public void onSuccess(Boolean result) {
						if (result == true) {
							registerGroupNameTip.setText("group name already exist");
							RootPanel.get("registerGroupnameGroup").setStyleName("control-group error");
							return;
						} else {
							registerGroupNameTip.setText("");
							RootPanel.get("registerGroupnameGroup").setStyleName("control-group success");
							if (isRegister) {
								registerNewGroup();
							}
							return;
						}
					}

					public void onFailure(Throwable caught) {
						Window.alert("server connection error when check if group exist : " + caught.toString());
					}
				});
	}

	private void clearSearchGroupResultTable() {
		searchResultTable.removeAllRows();
	}

	private void registerNewGroup() {
		DutchGroup newgroup = new DutchGroup(registerGroupNameBox.getText(), currentUser.getUsername());
		calculatorUserService.addGroupToCurrentUser(newgroup, new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				registerGroupNameRecover();
				clickElement(DOM.getElementById("closeRegisterGroup"));
				refreshGroupList();
			}

			public void onFailure(Throwable caught) {
				registerGroupNameRecover();
				clickElement(DOM.getElementById("closeRegisterGroup"));
				Window.alert("server connection fails when try to register new group");
			}
		});
	}

	private void registerGroupNameRecover() {
		registerGroupNameBox.setText("");
		registerGroupNameTip.setText("");
		RootPanel.get("registerGroupnameGroup").setStyleName("control-group");
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
					((Anchor) event.getSource()).getParent().setStyleName("active");
					groupTab.RefreshGroup(group, currentUser);
					clickElement(groupTab.homePanel.homePanelLink.getElement());
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

	/*
	 * refresh Function
	 */
	private void refreshUser() {
		calculatorUserService.checkUserLogin(new AsyncCallback<DutchUser>() {

			public void onSuccess(DutchUser result) {
				currentUser = result;
			}

			public void onFailure(Throwable caught) {

			}
		});
	}

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

	private native void clickElement(Element elem) /*-{
		elem.click();
	}-*/;

}
