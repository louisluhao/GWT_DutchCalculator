package com.louis.calculator.beans;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DutchBill implements IsSerializable {

	private String billNumber;

	private String billTitle;

	private Double billAmount;

	private String creatUser;

	private Long billDate;

	private Set<String> includePeoples = new HashSet<String>();

	private Set<String> verifidPeoples = new HashSet<String>();

	private String billDetailNote;
	
	private Boolean isDeleted;

	public DutchBill() {

	}

	public DutchBill(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public String getBillTitle() {
		return billTitle;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public String getCreatUser() {
		return creatUser;
	}

	public Long getBillDate() {
		return billDate;
	}

	public Set<String> getIncludePeoples() {
		return includePeoples;
	}

	public String getBillDetailNote() {
		return billDetailNote;
	}

	public Set<String> getVerifidPeoples() {
		return verifidPeoples;
	}

	public void setVerifidPeoples(Set<String> verifidPeoples) {
		this.verifidPeoples.clear();
		this.verifidPeoples.addAll(verifidPeoples);
	}

	public void setBillTitle(String billTitle) {
		this.billTitle = billTitle;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public void setCreatUser(String creatUser) {
		this.creatUser = creatUser;
	}

	public void setBillDate(Long billDate) {
		this.billDate = billDate;
	}

	public void setIncludePeoples(Set<String> includePeoples) {
		this.includePeoples.clear();
		this.includePeoples.addAll(includePeoples);
	}

	public void setBillDetailNote(String billDetailNote) {
		this.billDetailNote = billDetailNote;
	}

	public String getIncludePeoplesString() {
		String r = "";
		int count = 1;
		for (String user : includePeoples) {
			if (count != includePeoples.size()) {
				r += user + ", ";
			} else {
				r += user;
			}
			count++;
		}
		return r;
	}

	public String getUnverifyUsersString() {
		String unverify = "";
		boolean first = true;
		for (String user : getUnverifiedUsers()) {
			if (first) {
				unverify += user;
				first = false;
			} else {
				unverify += ", " + user;
			}
		}
		return unverify;
	}

	private Set<String> getUnverifiedUsers() {
		Set<String> unverified = new HashSet<String>();
		for (String user : includePeoples) {
			if (!verifidPeoples.contains(user)) {
				unverified.add(user);
			}
		}
		return unverified;
	}

	public double getSharedPrice() {
		return billAmount / includePeoples.size();
	}

	public boolean isValid() {
		return includePeoples.size() == verifidPeoples.size();
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
