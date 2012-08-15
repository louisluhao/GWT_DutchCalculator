package com.louis.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.louis.calculator.beans.DutchBill;
import com.louis.calculator.beans.DutchUser;
import com.louis.calculator.beans.DutchGroup;
import com.louis.calculator.beans.GroupRelatedInfo;
import com.louis.calculator.client.CalculatorUserService;
import com.louis.server.jdo.beans.BillBean;
import com.louis.server.jdo.beans.GroupBean;
import com.louis.server.jdo.beans.PMF;
import com.louis.server.jdo.beans.UserBean;

public class CalculatorUserServiceImpl extends RemoteServiceServlet implements
		CalculatorUserService {

	private static final long serialVersionUID = 1L;

	public DutchUser checkUserLogin() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		if (username == null) {
			return new DutchUser(false, "");
		}
		DutchUser user = DBHelper.getUserByUsername(username);
		if (user != null) {
			return user;
		} else {
			return new DutchUser(false, "");
		}
	}

	public Void logout() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("username");
		return null;
	}

	public Boolean ifGroupExist(String groupname) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.getObjectById(GroupBean.class, groupname);
			return true;
		} catch (JDOObjectNotFoundException e) {
			return false;
		} finally {
			pm.close();
		}
	}

	public Boolean addGroupToCurrentUser(DutchGroup newgroup) {
		String username = newgroup.getAdminUserList().get(0);
		if (DBHelper.UserAddGroup(username, newgroup.getGroupName()) && DBHelper.addNewGroup(newgroup)) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<String> searchGroupByName(String groupName) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery("select groupName from " + GroupBean.class.getName());
		List<String> groupNames = (List<String>) query.execute();
		ArrayList<String> returnList = new ArrayList<String>();
		for (String name : groupNames) {
			if (name.contains(groupName)) {
				returnList.add(name);
			}
		}
		return returnList;
	}

	public Boolean joinGroup(String groupName, String username) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			GroupBean group = pm.getObjectById(GroupBean.class, groupName);
			UserBean user = pm.getObjectById(UserBean.class, username);
			if (group.addApplyUser(username) && user.addApplyGroup(groupName)) {
				return true;
			} else {
				return false;
			}
		} finally {
			pm.close();
		}
	}

	public GroupRelatedInfo getGroupAndBillsByName(String groupName, String username) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			GroupRelatedInfo returnInfo = new GroupRelatedInfo();
			GroupBean group = pm.getObjectById(GroupBean.class, groupName);
			if (group.getUserList().contains(username)) {
				DutchGroup dutchGroup = new DutchGroup(group.getGroupName());
				dutchGroup.setAdminUserList(group.getAdminUserList());
				dutchGroup.setApplyUserlist(group.getApplyUserList());
				dutchGroup.setBillList(group.getBillList());
				dutchGroup.setUserList(group.getUserList());
				returnInfo.setGroup(dutchGroup);

				for (String billName : dutchGroup.getBillList()) {
					BillBean bill = pm.getObjectById(BillBean.class, billName);
					returnInfo.addBills(bill.toDutchBill());
				}

				return returnInfo;
			} else {
				return null;
			}
		} catch (JDOObjectNotFoundException e) {
			return null;
		} finally {
			pm.close();
		}

	}

	public Void ConfirmApplyUser(String groupname, String username) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			GroupBean group = pm.getObjectById(GroupBean.class, groupname);
			UserBean user = pm.getObjectById(UserBean.class, username);

			group.confirmApplyUser(username);
			user.applyGroupConfirm(groupname);
			return null;
		} finally {
			pm.close();
		}
	}
	
	public Void RejectApplyUser(String groupname, String username) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			GroupBean group = pm.getObjectById(GroupBean.class, groupname);
			UserBean user = pm.getObjectById(UserBean.class, username);

			group.rejectApplyUser(username);
			user.applyGroupReject(groupname);
			return null;
		} finally {
			pm.close();
		}
	}

	public Void createBill(DutchBill bill, DutchGroup group) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			BillBean newBill = new BillBean(group.getGroupName() + "-"
					+ String.valueOf(group.getBillList().size() + 1));
			newBill.setBillAmount(bill.getBillAmount());
			newBill.setBillDate(bill.getBillDate());
			newBill.setBillDetailNote(bill.getBillDetailNote());
			newBill.setCreatUser(bill.getCreatUser());
			newBill.setIncludePeoples(bill.getIncludePeoples());
			newBill.setBillTitle(bill.getBillTitle());
			GroupBean updateGroup = pm.getObjectById(GroupBean.class, group.getGroupName());
			updateGroup.addBill(newBill.getBillNumber());
			pm.makePersistent(newBill);
			return null;
		} finally {
			pm.close();
		}
	}

	public List<DutchBill> getBills(List<String> billNames) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<DutchBill> dutchBills = new ArrayList<DutchBill>();
		try{
			for(String billName : billNames){
				BillBean bill = pm.getObjectById(BillBean.class, billName);
				dutchBills.add(bill.toDutchBill());
			}
			return dutchBills;
		}finally{
			pm.close();
		}
	}

	public Void userVerifyBill(String username, DutchBill bill) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			BillBean verifiedBill = pm.getObjectById(BillBean.class, bill.getBillNumber());
			verifiedBill.userVerify(username);
			return null;
		}finally{
			pm.close();
		}
	}

	public Void deteleBill(String billNumber, String groupName) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			BillBean bill = pm.getObjectById(BillBean.class, billNumber);
			bill.setIsDeleted(true);
			return null;
		}finally{
			pm.close();
		}
	}

	public Void deleteUser(String username, String groupName) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			GroupBean group = pm.getObjectById(GroupBean.class, groupName);
			group.deleteUser(username);
			return null;
		}finally{
			pm.close();
		}
	}


}
