package com.louis.calculator.client;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.louis.calculator.beans.DutchBill;
import com.louis.calculator.beans.DutchGroup;
import com.louis.calculator.beans.DutchUser;

public class GroupBillPanel {
	GroupTab parent;

	DutchGroup currentGroup;
	DutchUser currentUser;
	List<DutchBill> currentBills;

	VerticalPanel billPanel = new VerticalPanel();
	Anchor createBill;
	FlexTable billsTable = new FlexTable();

	BillPopUpPanel billPopUpPanel = new BillPopUpPanel(this);

	public GroupBillPanel(GroupTab parent) {
		this.parent = parent;

		buildBillPanel();

		RootPanel.get("billPanel").add(billPanel);
	}

	private void buildBillPanel() {
		createBillBtn();
		setupBillsTable();
		billPanel.add(createBill);
		billPanel.add(billsTable);
	}

	private void createBillBtn() {
		createBill = new Anchor();
		createBill.setHTML("<i class=\"icon-tag icon-white\"></i>Greate Bill");
		createBill.setStyleName("btn btn-primary");
		createBill.getElement().setAttribute("data-toggle", "modal");
		createBill.getElement().setAttribute("href", "#billPopUp");

		createBill.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				billPopUpPanel.createPopUpCreateBill(currentUser.getUsername(), currentGroup);
			}
		});
	}
	
	private void setupBillsTable() {
		billsTable.setStyleName("table table-striped table-bordered");
	}

	public void RefreshGroup(DutchGroup currentGroup, DutchUser currentUser,
			List<DutchBill> currentBills) {
		this.currentGroup = currentGroup;
		this.currentUser = currentUser;
		this.currentBills = currentBills;
		buildBillListTable();
	}

	private void buildBillListTable() {
		buildBillTableHeader();
		if (currentBills.size() > 0) {
			int row = 1;
			for (DutchBill bill : currentBills) {
				billsTable.setText(row, 0, bill.getBillTitle());
				billsTable.setText(row, 1, "$" + bill.getBillAmount());
				billsTable.setText(row, 2, bill.getCreatUser());
				billsTable.setText(row, 3, bill.getBillDate().toString());
				row++;
			}
		}
	}

	private void buildBillTableHeader() {
		billsTable.setWidget(0, 0, getBillStringHeader("Bill Title"));
		billsTable.setWidget(0, 1, getBillStringHeader("Amount"));
		billsTable.setWidget(0, 2, getBillStringHeader("Founder"));
		billsTable.setWidget(0, 3, getBillStringHeader("Date"));	
		
	}

	private Label getBillStringHeader(String string) {
		Label header = new Label(string);
		header.setStyleName("listheader");
		return header;
	}
	
	public void refreshGroupTabPanel(){
		parent.refreshUserAndGroup(currentUser.getUsername(), currentGroup.getGroupName());
	}
}
