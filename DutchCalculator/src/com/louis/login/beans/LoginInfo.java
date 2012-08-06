package com.louis.login.beans;

import java.io.Serializable;

public class LoginInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum LoginState{
		Success,
		UserNameNotExist,
		PassWordWrong
	};
	
	LoginState loginState;

	public LoginInfo() {
		super();
	}

	public LoginInfo(LoginState loginState) {
		super();
		this.loginState = loginState;
	}

	public LoginState getLoginState() {
		return loginState;
	}

	public void setLoginState(LoginState loginState) {
		this.loginState = loginState;
	}
	
	
}
