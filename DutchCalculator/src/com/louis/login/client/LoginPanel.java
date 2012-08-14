package com.louis.login.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.louis.login.beans.DutchUser;
import com.louis.login.beans.LoginInfo;
import com.louis.login.beans.LoginInfo.LoginState;

public class LoginPanel implements EntryPoint {

//	private String devModel = "?gwt.codesvr=127.0.0.1:9997";
	 private String devModel = "";

	TextBox usernameBox = new TextBox();
	PasswordTextBox passwordBox = new PasswordTextBox();
	Button loginButton = new Button("Log In");

	/*
	 * register panel
	 */
	TextBox registerUsernameBox = new TextBox();
	Label registerUsernameTip = new Label();
	PasswordTextBox registerPswBox = new PasswordTextBox();
	Label registerPswTip = new Label();
	PasswordTextBox registerPswCopyBox = new PasswordTextBox();
	Label registerPswCopyTip = new Label();
	Button registerButton = new Button("Register");
	Button closeRegisterButton = new Button("Close");
	Boolean registerFlag = false;

	/*
	 * service
	 */
	DutchUserServiceAsync userServiceAsync = GWT.create(DutchUserService.class);

	public void onModuleLoad() {

		RootPanel.get("usernameField").add(usernameBox);
		RootPanel.get("pswField").add(passwordBox);
		RootPanel.get("loginButton").add(loginButton);

		/*
		 * register panel
		 */
		RootPanel.get("registerUsername").add(registerUsernameBox);
		RootPanel.get("registerUsernameTip").add(registerUsernameTip);
		RootPanel.get("registerPsw").add(registerPswBox);
		RootPanel.get("registerPswTip").add(registerPswTip);
		RootPanel.get("registerPswCopy").add(registerPswCopyBox);
		RootPanel.get("registerPswCopyTip").add(registerPswCopyTip);
		RootPanel.get("registerButton").add(registerButton);
		RootPanel.get("closeRegisterButton").add(closeRegisterButton);

		attachStyle();
		attachActionHandler();

	}

	private void attachStyle() {
		loginButton.setStyleName("btn btn-success");
		registerButton.setStyleName("btn btn-primary");
		closeRegisterButton.setStyleName("btn");

	}

	private void attachActionHandler() {

		registerUsernameBox.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				registerUsernameBoxCheckIsRigister(false);
			}
		});

		registerPswBox.addKeyUpHandler(new KeyUpHandler() {

			public void onKeyUp(KeyUpEvent event) {
				registerPswBoxCheck();
			}
		});

		registerPswCopyBox.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				registerPswCopyBoxCheck();
			}
		});

		registerPswCopyBox.addKeyPressHandler(new KeyPressHandler() {

			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					registerFlag = true;
					registerCheck();
				}
			}
		});

		registerButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				registerFlag = true;
				registerCheck();
			}
		});

		closeRegisterButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				clearRegisterPanel();
			}
		});

		loginButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				loginUser();
			}
		});

		passwordBox.addKeyPressHandler(new KeyPressHandler() {

			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					loginUser();
				}
			}
		});
	}

	protected void loginUser() {
		DutchUser user = new DutchUser(usernameBox.getText(), passwordBox.getText());
		userServiceAsync.login(user, new AsyncCallback<LoginInfo>() {

			public void onSuccess(LoginInfo result) {
				if (result.getLoginState() == LoginState.UserNameNotExist) {
					Window.alert("username not exist!");
				} else if (result.getLoginState() == LoginState.PassWordWrong) {
					Window.alert("password wrong!");
				} else if (result.getLoginState() == LoginState.Success) {
					Window.Location.replace(GWT.getHostPageBaseURL() + "calculator.html" + devModel);
				}
			}

			public void onFailure(Throwable caught) {
				Window.alert("server connect fail, please try again");
			}
		});
	}

	protected void registerUser() {
		DutchUser newUser = new DutchUser(registerUsernameBox.getText(), registerPswBox.getText());
		userServiceAsync.RegisterUser(newUser, new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				clickElement(DOM.getElementById("closeRegisterButton"));
				usernameBox.setText(registerUsernameBox.getText());
				passwordBox.setText(registerPswBox.getText());
				clearRegisterPanel();
				loginButton.click();
			}

			public void onFailure(Throwable caught) {
				Window.alert("server connect fail, please try again!");
			}
		});
	}

	private void clearRegisterPanel() {
		registerPswBox.setText("");
		registerPswCopyBox.setText("");
		registerUsernameBox.setText("");
		registerPswTip.setText("");
		registerPswCopyTip.setText("");
		registerUsernameTip.setText("");
		RootPanel.get("registerUsernameGroup").setStyleName("control-group");
		RootPanel.get("registerPswGroup").setStyleName("control-group");
		RootPanel.get("registerPswCopyGroup").setStyleName("control-group");
	}

	private native void clickElement(Element elem) /*-{
		elem.click();
	}-*/;

	private void registerCheck() {
		registerFlag = true;
		registerPswBoxCheck();
		registerPswCopyBoxCheck();
		registerUsernameBoxCheckIsRigister(true);
	}

	protected void registerPswCopyBoxCheck() {
		if (!registerPswCopyBox.getText().equals(registerPswBox.getText())) {
			registerPswCopyTip.setText("The re-entered password is not same as password");
			RootPanel.get("registerPswCopyGroup").setStyleName("control-group error");
			registerFlag = false;
		} else {
			registerPswCopyTip.setText("");
			RootPanel.get("registerPswCopyGroup").setStyleName("control-group success");
		}
	}

	protected void registerPswBoxCheck() {
		if (registerPswBox.getText().length() < 6) {
			registerPswTip.setText("password length must bigger than 6");
			RootPanel.get("registerPswGroup").setStyleName("control-group error");
			registerFlag = false;
		} else {
			registerPswTip.setText("");
			RootPanel.get("registerPswGroup").setStyleName("control-group success");
		}
	}

	protected void registerUsernameBoxCheckIsRigister(final boolean isRigister) {
		if (registerUsernameBox.getText().length() == 0) {
			registerUsernameTip.setText("must input username");
			RootPanel.get("registerUsernameGroup").setStyleName("control-group error");
			registerFlag = false;
			return;
		}

		userServiceAsync.CheckUsernameExist(registerUsernameBox.getText(),
				new AsyncCallback<Boolean>() {

					public void onSuccess(Boolean result) {
						if (result == true) {
							registerUsernameTip.setText("username already exist, please try other name");
							RootPanel.get("registerUsernameGroup").setStyleName("control-group error");
							registerFlag = false;
							return;
						} else {
							registerUsernameTip.setText("");
							RootPanel.get("registerUsernameGroup").setStyleName("control-group success");
							if (registerFlag && isRigister) {
								registerUser();
							}
						}
					}

					public void onFailure(Throwable caught) {
						Window.alert("server connect fail, please try again!");
						registerFlag = false;
						return;
					}
				});

	}

}
