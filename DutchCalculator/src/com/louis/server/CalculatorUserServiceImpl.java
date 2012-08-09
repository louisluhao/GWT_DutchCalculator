package com.louis.server;


import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.louis.calculator.beans.DutchUser;
import com.louis.calculator.beans.DutchGroup;
import com.louis.calculator.client.CalculatorUserService;
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
		try{
			pm.getObjectById(GroupBean.class, groupname);
			return true;
		}catch(JDOObjectNotFoundException e){
			return false;
		}finally{
			pm.close();
		}
	}

	public Boolean addGroupToCurrentUser(DutchGroup newgroup) {
		String username = newgroup.getAdminUserList().get(0);
		if (DBHelper.UserAddGroup(username, newgroup.getGroupName())
				&& DBHelper.addNewGroup(newgroup)) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<String> searchGroupByName(String groupName) {
		PersistenceManager pm= PMF.get().getPersistenceManager();
		Query query = pm.newQuery("select groupName from " + GroupBean.class.getName());
		List<String> groupNames = (List<String>) query.execute();
		ArrayList<String> returnList = new ArrayList<String>();
		for(String name : groupNames){
			if(name.contains(groupName)){
				returnList.add(name);
			}
		}
		return returnList;
	}

	public Boolean joinGroup(String groupName, String username) {
		PersistenceManager pm= PMF.get().getPersistenceManager();
		try{
			GroupBean group = pm.getObjectById(GroupBean.class, groupName);
			UserBean user = pm.getObjectById(UserBean.class, username);
			if(group.addApplyUser(username) && user.addApplyGroup(groupName)){
				return true;
			}else{
				return false;
			}
		}
		finally{
			pm.close();
		}
	}

	public DutchGroup getGroupByName(String groupName, String username) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			GroupBean group = pm.getObjectById(GroupBean.class, groupName);
			if(group.getUserList().contains(username)){
				DutchGroup returnGroup = new DutchGroup(group.getGroupName());
				returnGroup.setAdminUserList(group.getAdminUserList());
				returnGroup.setApplyUserlist(group.getApplyUserList());
				returnGroup.setBillList(group.getBillList());
				returnGroup.setUserList(group.getUserList());
				return returnGroup;
			}else{
				return null;
			}	
		}
		catch (JDOObjectNotFoundException e) {
			return new DutchGroup();
		}finally{
			pm.close();
		}
		
	}

	public Void ConfirmApplyUser(String groupname, String username) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			GroupBean group = pm.getObjectById(GroupBean.class,groupname);
			UserBean user = pm.getObjectById(UserBean.class, username);

			group.confirmApplyUser(username);
			user.applyGroupConfirm(groupname);
			return null;
		}finally{
			pm.close();
		}
	}



}
