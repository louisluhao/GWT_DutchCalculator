package com.louis.calculator.client;

import static com.louis.calculator.client.ui.component.ListHeader.getTableHeader;

import java.util.Calendar;
import java.util.Date;
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
import com.google.gwt.user.client.ui.TextArea;
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
	boolean isShowVerifiedBill = false;

	VerticalPanel billPanel = new VerticalPanel();
	HorizontalPanel btnPanel = new HorizontalPanel();
	Anchor createBill;
	Anchor switchShowValidBill = new Anchor();
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
		setupSwitchShowValidBill();
		addListenerToSwitchValidBill();
		setupBillsTable();
		billPanel.add(btnPanel);
		btnPanel.add(createBill);
		btnPanel.add(switchShowValidBill);
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

	private void setupSwitchShowValidBill() {
		if (isShowVerifiedBill) {
			switchShowValidBill.setText("Only Show Unvalid Bill");
			switchShowValidBill.setStyleName("btn btn-success");
		} else {
			switchShowValidBill.setText("Show Valid Bill");
			switchShowValidBill.setStyleName("btn btn-success");
		}
	}

	private void addListenerToSwitchValidBill() {
		switchShowValidBill.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				isShowVerifiedBill = !isShowVerifiedBill;
				setupSwitchShowValidBill();
				buildBillListTable();
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
		billsTable.setWidget(0, 9, getTableHeader("Modify Bill"));
	}

	private void buildBillListTable() {
		billsTable.removeAllRows();
		if (currentBills.size() > 0) {
			buildBillTableHeader();
			int row = 1;
			for (int i = currentBills.size() - 1; i >= 0; i--) {
				DutchBill bill = currentBills.get(i);
				if (!bill.getIncludePeoples().contains(currentUser.getUsername())
						&& !bill.getCreatUser().equals(currentUser.getUsername())) {
					continue;
				}
				if (!isShowVerifiedBill && bill.isValid()) {
					continue;
				}
				if(bill.isDeleted()){
					continue;
				}
				billsTable.setWidget(row, 0, getValidStatusLabel(bill.isValid()));
				billsTable.setText(row, 1, bill.getBillTitle());
				billsTable.setText(row, 2, "$" + bill.getBillAmount());
				billsTable.setText(row, 3, bill.getCreatUser());
				Date date = new Date(bill.getBillDate());
				billsTable.setText(row, 4, (date.getMonth() + 1) + "/" + date.getDate() + "/"
						+ (date.getYear() + 1900));
				billsTable.setText(row, 5, bill.getIncludePeoplesString());
				billsTable.setWidget(row, 6, getBillNoteWrapper(bill));
				billsTable.setWidget(row, 7, getBillVerifyBtn(bill));
				billsTable.setText(row, 8, bill.getUnverifyUsersString());
				billsTable.setWidget(row, 9, getDeleteBtn(bill));
				row++;
			}
		} else {
			billsTable.setWidget(0, 0, getTableHeader("Current No Bills"));
		}
	}

	private Widget getDeleteBtn(final DutchBill bill) {
		if (bill.getCreatUser().equals(currentUser.getUsername())) {
			Anchor delete = new Anchor("Delete");
			delete.setStyleName("btn btn-danger btn-mini");
			delete.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					boolean isSure = Window.confirm("Do you really want to delete this bill?");
					if (isSure) {
						deleteBill(bill.getBillNumber());
					}
				}
			});
			return delete;
		}
		return new Label("");
	}

	protected void deleteBill(final String billNumber) {
		calculatorUserService.deteleBill(billNumber, currentGroup.getGroupName(),
				new AsyncCallback<Void>() {

					public void onSuccess(Void result) {
						refreshGroupTabPanel();
					}

					public void onFailure(Throwable caught) {
						Window.alert("server connect error when delete");
					}
				});
	}

	private Widget getBillNoteWrapper(DutchBill bill) {
		final String note = bill.getBillDetailNote();
		if (note == null || note.length() == 0) {
			return new Label("");
		}
		if (note.length() < 20) {
			return new Label(note);
		} else {
			Anchor showNoteBtn = new Anchor("Show Detail Note");
			showNoteBtn.setStyleName("btn btn-mini");
			showNoteBtn.getElement().setAttribute("data-toggle", "modal");
			showNoteBtn.getElement().setAttribute("href", "#showBillNote");
			showNoteBtn.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					Label noteLabel = new Label(note);
					RootPanel.get("billNoteBody").clear();
					RootPanel.get("billNoteBody").add(noteLabel);
				}
			});

			return showNoteBtn;
		}

	}

	private Button getBillVerifyBtn(DutchBill bill) {
		String username = currentUser.getUsername();
		if (!bill.getIncludePeoples().contains(username)) {
			Button noRelativeBillBtn = new Button("Inrelevant");
			noRelativeBillBtn.setStyleName("btn disabled btn-mini");
			return noRelativeBillBtn;
		} else {
			if (bill.getVerifidPeoples().contains(username)) {
				Button varifiedBtn = new Button("Verified");
				varifiedBtn.setStyleName("btn btn-success disabled btn-mini");
				return varifiedBtn;
			} else {
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
				calculatorUserService.userVerifyBill(currentUser.getUsername(), bill,
						new AsyncCallback<Void>() {

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
