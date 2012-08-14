package com.louis.calculator.client.algo;

import java.util.ArrayList;
import java.util.List;

import com.louis.calculator.beans.DutchBill;

public class BillAlgo {
	
	private static List<DutchBill> fillterOutDeletedBills(List<DutchBill> bills){
		List<DutchBill> list = new ArrayList<DutchBill>();
		for(DutchBill bill : bills){
			if(!bill.isDeleted()){
				list.add(bill);
			}
		}
		return list;
	}

	public static double getBalanceAll(String username, List<DutchBill> bills) {
		double balance = 0;

		for (DutchBill bill : fillterOutDeletedBills(bills)) {
			// debt
			if (bill.getCreatUser().equals(username)) {
				balance += bill.getBillAmount();
			}
			// credit
			if (bill.getIncludePeoples().contains(username)) {
				balance -= bill.getSharedPrice();
			}
		}

		return balance;
	}

	public static double getBalanceValid(String username, List<DutchBill> bills) {
		double balance = 0;

		for (DutchBill bill : fillterOutDeletedBills(bills)) {
			if (bill.isValid()) {
				// debt
				if (bill.getCreatUser().equals(username)) {
					balance += bill.getBillAmount();
				}
				// credit
				if (bill.getIncludePeoples().contains(username)) {
					balance -= bill.getSharedPrice();
				}
			}
		}

		return balance;
	}

	public static double formatDouble(double in) {
//		double up = (((int) (in * 1000)) % 10) >= 5 ? 0.01 : 0;
		return ((double) ((int) (in * 100))) / 100.0 + 0.01;
	}
}
