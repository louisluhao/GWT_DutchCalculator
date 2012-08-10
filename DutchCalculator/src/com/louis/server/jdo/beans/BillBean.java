package com.louis.server.jdo.beans;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.louis.calculator.beans.DutchBill;

@PersistenceCapable
public class BillBean {
	
	@PrimaryKey
	@Persistent
	private String billNumber;
	
	@Persistent
	private String billTitle;
	
	@Persistent
	private Double billAmount;
	
	@Persistent
	private String creatUser;
	
	@Persistent
	private Long billDate;

	@Persistent
	private Set<String> includePeoples = new HashSet<String>();
	
	@Persistent
	private String billDetailNote;
	
	@Persistent
	private Boolean isValid = true;
	
	public BillBean(String billNumber){
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
	
	public DutchBill toDutchBill(){
		DutchBill bill =new DutchBill(billNumber);
		bill.setBillAmount(billAmount);
		bill.setBillDate(billDate);
		bill.setBillDetailNote(billDetailNote);
		bill.setBillTitle(billTitle);
		bill.setCreatUser(creatUser);
		bill.setIncludePeoples(getIncludePeoples());
		return bill;
	}
	
}
