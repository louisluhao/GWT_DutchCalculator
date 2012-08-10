package com.louis.calculator.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.louis.calculator.beans.DutchBill;
import com.louis.calculator.beans.DutchGroup;

public class BillPopUpPanel {
	GroupBillPanel parent;
	
	DutchBill bill;
	DutchGroup group;
	String username;

	CalculatorUserServiceAsync calculatorUserService = CalculatorServerProxy.getCalculatorServer();

	boolean isEditable;

	Label billPopUpTitle = new Label();

	VerticalPanel billPanel = new VerticalPanel();
	TextBox billTitle = new TextBox();
	TextBox billAmount = new TextBox();
	TextBox billDate = new TextBox();
	TextArea billDetailNote = new TextArea();
	FlexTable includeUserTable = new FlexTable();

	List<CheckBox> includeUserCheckBoxList = new ArrayList<CheckBox>();

	Anchor actionButton = new Anchor();

	public BillPopUpPanel(GroupBillPanel parent) {
		this.parent = parent;
		
		buildPanel();
		attachToRootPanel();

		addCheckListner();
	}

	private void buildPanel() {
		billPanel.add(new Label("Title"));
		billPanel.add(billTitle);
		billPanel.add(new Label("Amount"));
		billPanel.add(billAmount);
		billPanel.add(new Label("Bill Date"));
		billPanel.add(billDate);
		billPanel.add(new Label("Relative User"));

		HorizontalPanel includeUserTablePanel = new HorizontalPanel();
		includeUserTablePanel.add(includeUserTable);
		billPanel.add(includeUserTablePanel);
		includeUserTable.setStyleName("table table-striped");

		billPanel.add(new Label("Note"));
		billPanel.add(billDetailNote);

	}

	private void attachToRootPanel() {
		RootPanel.get("billPopUpTitle").add(billPopUpTitle);
		RootPanel.get("billPopUpPanel").add(billPanel);
		RootPanel.get("billPopUpFooter").add(actionButton);
	}

	public void createPopUpCreateBill(String username, DutchGroup group) {
		this.isEditable = true;
		this.username = username;
		this.group = group;

		buildCreateMode();
	}

	private void buildCreateMode() {
		billPopUpTitle.setText("Create Bill");
		actionButton.setText("Create");
		actionButton.setStyleName("btn btn-primary");
		buildIncludeUserList();
	}

	private void buildIncludeUserList() {
		includeUserTable.removeAllRows();
		int row = 1;
		for (String username : group.getUserList()) {
			CheckBox userCheckBox = new CheckBox(username);
			includeUserCheckBoxList.add(userCheckBox);
			includeUserTable.setWidget(row, 0, userCheckBox);
			row++;
		}
	}

	public void createPopUp(String username, DutchBill bill, boolean isEditable) {
		this.bill = bill;
		this.isEditable = isEditable;
	}

	private void addCheckListner() {
		addCheckAmountListenr();
		addActionBtnListener();
	}

	private void addCheckAmountListenr() {
		billAmount.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				if (!isCheckBillAmountBoxPass()) {
					Window.alert("must input number!");
				}
			}
		});
	}

	protected boolean isCheckBillAmountBoxPass() {
		try {
			Double.valueOf(billAmount.getText());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private void addActionBtnListener() {
		actionButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Anchor actionBtn = (Anchor) event.getSource();
				if (actionBtn.getText().equals("Create")) {
					if (checkBeforeSave()) {
						createBill();
					} else {
						Window.alert("input data error!");
					}
				}
			}
		});
	}

	protected boolean checkBeforeSave() {
		if (billTitle.getText().length() == 0) {
			return false;
		}
		if (!isCheckBillAmountBoxPass()) {
			return false;
		}
		if (billDate.getText().length() == 0) {
			return false;
		}
		if (getSelectedUsers().size() == 0) {
			return false;
		}
		return true;
	}

	private Set<String> getSelectedUsers() {
		Set<String> selectedUsers = new HashSet<String>();
		for (CheckBox checkbox : includeUserCheckBoxList) {
			if (checkbox.getValue() == true) {
				selectedUsers.add(checkbox.getText());
			}
		}
		return selectedUsers;
	}

	protected void createBill() {
		DutchBill bill = new DutchBill();
		bill.setBillTitle(billTitle.getText());
		bill.setBillAmount(Double.valueOf(billAmount.getText()));
		bill.setBillDate((long) 0);// TODO:Add date picker!
		bill.setCreatUser(username);
		bill.setIncludePeoples(getSelectedUsers());
		bill.setBillDetailNote(billDetailNote.getText());
		calculatorUserService.createBill(bill, group, new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				clearPopUpPanel();
				fade();
				refreshTab();
			}

			public void onFailure(Throwable caught) {
				Window.alert("insert bill false");
			}
		});
	}

	protected void refreshTab() {
		parent.refreshGroupTabPanel();
	}

	protected void clearPopUpPanel() {
		billTitle.setText("");
		billAmount.setText("");
		billDate.setText("");
		billDetailNote.setText("");
		billPopUpTitle.setText("");
		includeUserTable.removeAllRows();
	}
	
	private void fade() {
		clickElement(DOM.getElementById("closeBillPopUp"));
	}


	private native void clickElement(Element elem) /*-{
		elem.click();
	}-*/;

}
