package com.louis.calculator.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.louis.calculator.beans.DutchBill;
import com.louis.calculator.beans.DutchUser;
import com.louis.calculator.beans.DutchGroup;
import com.louis.calculator.beans.GroupRelatedInfo;

@RemoteServiceRelativePath("calculatorUser")
public interface CalculatorUserService extends RemoteService {
	public DutchUser checkUserLogin();
	public Void logout();
	public Boolean ifGroupExist(String groupname);
	public Boolean addGroupToCurrentUser(DutchGroup newgroup);
	public ArrayList<String> searchGroupByName(String groupName);
	public Boolean joinGroup(String groupName, String username);
	public GroupRelatedInfo getGroupAndBillsByName(String groupName, String username);
	public Void ConfirmApplyUser(String groupname, String username);
	public Void RejectApplyUser(String groupname, String username);
	public Void createBill(DutchBill bill, DutchGroup group);
	public List<DutchBill> getBills(List<String> billNames);
	public Void userVerifyBill(String username, DutchBill bill);
	public Void deteleBill(String billNumber, String groupName);
}
