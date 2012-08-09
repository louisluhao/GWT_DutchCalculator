package com.louis.server;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import com.louis.calculator.beans.DutchGroup;
import com.louis.calculator.beans.DutchUser;
import com.louis.server.jdo.beans.GroupBean;
import com.louis.server.jdo.beans.PMF;
import com.louis.server.jdo.beans.UserBean;

public class DBHelper {
	/**
	 * get user by username, if user not exist will return null
	 * @param username
	 * @return UserBean 
	 */
	public static DutchUser getUserByUsername(String username){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			
			UserBean user = pm.getObjectById(UserBean.class, username);
			DutchUser returnUser = new DutchUser(true, username, user.getGroupIDs(), user.getApplyGroupIDs());
			return returnUser;
		} catch (JDOObjectNotFoundException e) {
			return null;
		} finally{
			pm.close();
		}
	}

	
	/**
	 * add group to user
	 * @param username
	 * @param groupname
	 * @return true if success, false if faild
	 */
	public static boolean UserAddGroup(String username, String groupname){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			UserBean user = pm.getObjectById(UserBean.class, username);
			if(user.addGroup(groupname)){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}finally{
			pm.close();
		}
	}
	
	public static boolean UserApplyGroup(String username, String groupname){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			UserBean user = pm.getObjectById(UserBean.class, username);
			if(user.addApplyGroup(groupname)){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}finally{
			pm.close();
		}
	}
	
	public static boolean addNewGroup(DutchGroup newgroup){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GroupBean newGroupBean = new GroupBean(newgroup.getGroupName());
		newGroupBean.setAdminUserList(newgroup.getAdminUserList());
		newGroupBean.setApplyUserList(newgroup.getApplyUserlist());
		newGroupBean.setBillList(newgroup.getBillList());
		newGroupBean.setUserList(newgroup.getUserList());
		try {
			pm.makePersistent(newGroupBean);
			return true;
		} catch (Exception e) {
			return false;
		} finally{
			pm.close();
		}
	}
}
