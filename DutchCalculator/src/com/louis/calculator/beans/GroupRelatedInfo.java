package com.louis.calculator.beans;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GroupRelatedInfo implements IsSerializable {
	DutchGroup group;
	List<DutchBill> bills = new ArrayList<DutchBill>();

	public GroupRelatedInfo(DutchGroup group, List<DutchBill> bills) {
		super();
		this.group = group;
		this.bills = bills;
	}

	public GroupRelatedInfo() {
		super();
	}

	public DutchGroup getGroup() {
		return group;
	}

	public void setGroup(DutchGroup group) {
		this.group = group;
	}

	public List<DutchBill> getBills() {
		return bills;
	}

	public void setBills(List<DutchBill> bills) {
		this.bills = bills;
	}

	public void addBills(DutchBill bill) {
		if (!bills.contains(bill)) {
			bills.add(bill);
		}
	}

}
