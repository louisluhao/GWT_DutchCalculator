package com.louis.server;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.louis.login.beans.DutchUser;
import com.louis.login.beans.LoginInfo;
import com.louis.login.beans.LoginInfo.LoginState;
import com.louis.login.client.DutchUserService;
import com.louis.server.jdo.beans.PMF;
import com.louis.server.jdo.beans.UserBean;

@SuppressWarnings("serial")
public class DutchUserServiceImpl extends RemoteServiceServlet implements
		DutchUserService {

	public Boolean CheckUsernameExist(String username) {
		if (username == null || username.equals("")) {
			return true;
		}
		return ifUserExist(username);
	}

	private boolean ifUserExist(String username) {
		// DatastoreService datastore =
		// DatastoreServiceFactory.getDatastoreService();
		// Key userKey = KeyFactory.createKey("user", username);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.getObjectById(UserBean.class, username);
		} catch (JDOObjectNotFoundException e) {
			return false;
		} finally {
			pm.close();
		}
		return true;
	}

	public Boolean RegisterUser(DutchUser newUser) {
		if (ifUserExist(newUser.getUsername())) {
			return false;
		} else {
			// DatastoreService datastore =
			// DatastoreServiceFactory.getDatastoreService();
			// Entity user = new Entity("user", newUser.getUsername());
			// user.setProperty("psw", newUser.getPsw());
			// datastore.put(user);
			PersistenceManager pm = PMF.get().getPersistenceManager();
			UserBean user = new UserBean(newUser.getUsername(),
					newUser.getPsw());
			try {
				pm.makePersistent(user);
			} finally {
				pm.close();
			}
			return true;
		}
	}

	public LoginInfo login(DutchUser requestUser) {
		LoginInfo loginInfo = new LoginInfo();

		if (requestUser.getUsername() == null
				|| requestUser.getUsername().equals("")) {
			loginInfo.setLoginState(LoginState.UserNameNotExist);
			return loginInfo;
		}
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			UserBean user = pm.getObjectById(UserBean.class, requestUser.getUsername());
			if (!user.getPassword().equals(requestUser.getPsw())) {
				loginInfo.setLoginState(LoginState.PassWordWrong);
				return loginInfo;
			} else {
				writeUserSession(user);
				loginInfo.setLoginState(LoginState.Success);
				return loginInfo;
			}
		} catch (JDOObjectNotFoundException e) {
			loginInfo.setLoginState(LoginState.UserNameNotExist);
			return loginInfo;
		}finally{
			pm.close();
		}

	}

	private void writeUserSession(UserBean user) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		session.setAttribute("username", user.getUsername());
	}

}
