package com.louis.server;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.louis.calculator.beans.DutchUser;
import com.louis.calculator.beans.DutchGroup;
import com.louis.calculator.client.CalculatorUserService;
import com.louis.server.jdo.beans.GroupBean;
import com.louis.server.jdo.beans.UserBean;

public class CalculatorUserServiceImpl extends RemoteServiceServlet implements
		CalculatorUserService {

	private static final long serialVersionUID = 1L;

	public DutchUser checkUserLogin() {
		DutchUser checkUserInfo;
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		if (username == null) {
			checkUserInfo = new DutchUser(false, "");
			return checkUserInfo;
		}
		UserBean user = DBHelper.getUserByUsername(username);
		if (user != null) {
			checkUserInfo = new DutchUser(true, user.getUsername(),
					user.getGroupIDs());
			return checkUserInfo;
		} else {
			checkUserInfo = new DutchUser(false, "");
			return checkUserInfo;
		}
	}

	public Void logout() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("username");
		return null;
	}

	public Boolean ifGroupExist(String groupname) {
		if (DBHelper.getGroupByGroupname(groupname) != null) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean addGroupToCurrentUser(DutchGroup newgroup) {
		String username = newgroup.getAdminUserList().get(0);
		GroupBean newGroupBean = new GroupBean(newgroup);
		if (DBHelper.UserAddGroup(username, newgroup.getGroupName())
				&& DBHelper.addNewGroup(newGroupBean)) {
			return true;
		} else {
			return false;
		}
	}

}
