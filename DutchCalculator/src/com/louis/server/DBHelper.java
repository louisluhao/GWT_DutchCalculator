package com.louis.server;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import com.louis.server.jdo.beans.GroupBean;
import com.louis.server.jdo.beans.PMF;
import com.louis.server.jdo.beans.UserBean;

public class DBHelper {
	/**
	 * get user by username, if user not exist will return null
	 * @param username
	 * @return UserBean 
	 */
	public static UserBean getUserByUsername(String username){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			UserBean user = pm.getObjectById(UserBean.class, username);
			return user;
		} catch (JDOObjectNotFoundException e) {
			return null;
		} finally{
			pm.close();
		}
	}
	
	
	/**
	 *  get group by username, if user not exist will return null
	 * @param group name
	 * @return GroupBean of giving groupname, will be null if group not exist
	 */
	public static GroupBean getGroupByGroupname(String groupname){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			GroupBean group = pm.getObjectById(GroupBean.class, groupname);
			return group;
		}catch(JDOObjectNotFoundException e){
			return null;
		}finally{
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
	
	public static boolean addNewGroup(GroupBean newGroup){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(newGroup);
			return true;
		} catch (Exception e) {
			return false;
		} finally{
			pm.close();
		}
	}
}
