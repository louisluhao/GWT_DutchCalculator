package com.louis.login.beans;

import java.io.Serializable;

public class DutchUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String psw;

	public DutchUser() {
		super();
	}

	public DutchUser(String username, String psw) {
		super();
		this.username = username;
		this.psw = psw;
	}

	public String getUsername() {
		return username;
	}

	public String getPsw() {
		return psw;
	}
	
}
