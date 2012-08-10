package com.louis.calculator.client;

import static com.louis.calculator.client.ui.component.ListHeader.getTableHeader;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
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

	Anchor billPanelLink;
	boolean isLinkVisible = false;

	VerticalPanel billPanel = new VerticalPanel();
	Anchor createBill;
	FlexTable billsTable = new FlexTable();

	BillPopUpPanel billPopUpPanel = new BillPopUpPanel(this);
	
	CalculatorUserServiceAsync calculatorUserService = CalculatorServerProxy.getCalculatorServer();

	public GroupBillPanel(GroupTab parent) {
		this.parent = parent;

		billPanelLink = getBillPanelLink();
		buildBillPanel();

		RootPanel.get("billPanel").add(billPanel);
	}

	private void buildBillPanel() {
		createBillBtn();
		setupBillsTable();
		billPanel.add(createBill);
		billPanel.add(billsTable);
	}

	private Anchor getBillPanelLink() {
		Anchor anchor = new Anchor();
		anchor.getElement().setAttribute("href", "#billPanel");
		anchor.getElement().setAttribute("data-toggle", "tab");
		anchor.setHTML("<i class=\"icon-tags\"></i> Bills");
		return anchor;
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
		showTabLink();
		buildBillListTable();
	}

	private void showTabLink() {
		if (!isLinkVisible) {
			RootPanel.get("billPanelLink").add(billPanelLink);
			isLinkVisible = true;
		}
	}

	private void buildBillListTable() {
		billsTable.removeAllRows();
		if (currentBills.size() > 0) {
			buildBillTableHeader();
			int row = 1;
			for (int i = currentBills.size() -1; i >=0 ; i--) {
				DutchBill bill = currentBills.get(i);
				billsTable.setWidget(row, 0, getValidStatusLabel(bill.isValid()));
				billsTable.setText(row, 1, bill.getBillTitle());
				billsTable.setText(row, 2, "$" + bill.getBillAmount());
				billsTable.setText(row, 3, bill.getCreatUser());
				billsTable.setText(row, 4, bill.getBillDate().toString());
				billsTable.setText(row, 5, bill.getIncludePeoplesString());
				billsTable.setText(row, 6, bill.getBillDetailNote());
				billsTable.setWidget(row, 7, getBillVerifyBtn(bill));
				billsTable.setText(row, 8, bill.getUnverifyUsersString());
				row++;
			}
		} else {
			billsTable.setWidget(0, 0, getTableHeader("Current No Bills"));
		}
	}

	private void buildBillTableHeader() {
		billsTable.setWidget(0, 0, getTableHeader("Valid Status"));
		billsTable.setWidget(0, 1, getTableHeader("Bill Title"));
		billsTable.setWidget(0, 2, getTableHeader("Amount"));
		billsTable.setWidget(0, 3, getTableHeader("Paid by"));
		billsTable.setWidget(0, 4, getTableHeader("Date"));
		billsTable.setWidget(0, 5, getTableHeader("Related User"));
		billsTable.setWidget(0, 6, getTableHeader("Bill Note"));
		billsTable.setWidget(0, 7, getTableHeader("Verify Bill"));
		billsTable.setWidget(0, 8, getTableHeader("Unverified User"));
	}
	
	private Button getBillVerifyBtn(DutchBill bill) {
		String username = currentUser.getUsername();
		if(!bill.getIncludePeoples().contains(username)){
			Button noRelativeBillBtn = new Button("Inrelevant");
			noRelativeBillBtn.setStyleName("btn disabled btn-mini");
			return noRelativeBillBtn;
		}else{
			if(bill.getVerifidPeoples().contains(username)){
				Button varifiedBtn = new Button("Verified");
				varifiedBtn.setStyleName("btn btn-success disabled btn-mini");
				return varifiedBtn;
			}else{
				return createVerifyBtn(bill);
			}
		}
	}

	private Button createVerifyBtn(DutchBill bill) {
		Button verifyBtn = new Button("Verify");
		verifyBtn.setStyleName("btn btn-success btn-mini");
		addListenerToVeriyBillBtn(verifyBtn, bill);
		return verifyBtn;
	}


	private void addListenerToVeriyBillBtn(Button verifyBtn, final DutchBill bill) {
		verifyBtn.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				calculatorUserService.userVerifyBill(currentUser.getUsername(), bill, new AsyncCallback<Void>() {
					
					public void onSuccess(Void result) {
						refreshGroupTabPanel();
					}
					
					public void onFailure(Throwable caught) {
						Window.alert("server connect error when verify bill");
					}
				});
			}
		});
	}

	private Widget getValidStatusLabel(boolean valid) {
		if (valid) {
			return createValidLabel();
		} else {
			return createInValidLabel();
		}
	}

	private Widget createValidLabel() {
		Label valid = new Label("valid");
		valid.setStyleName("label label-success");
		return valid;
	}

	private Widget createInValidLabel() {
		Label invalid = new Label("Invalid");
		invalid.setStyleName("label label-important");
		return invalid;
	}

	public void refreshGroupTabPanel() {
		parent.refreshUserAndGroup(currentUser.getUsername(), currentGroup.getGroupName());
	}
}
