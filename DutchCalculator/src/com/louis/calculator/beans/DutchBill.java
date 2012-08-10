package com.louis.calculator.beans;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DutchBill implements IsSerializable{

	private String billNumber;

	private String billTitle;

	private Double billAmount;

	private String creatUser;

	private Long billDate;

	private Set<String> includePeoples = new HashSet<String>();

	private String billDetailNote;
	
	public DutchBill(){
		
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
		return new HashSet<String>(includePeoples);
	}

	public String getBillDetailNote() {
		return billDetailNote;
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

}
