package com.mingchao.snsspider.qq.common;

public class BaseParaments implements Paraments {

	private String pidFile=null;
	private String accountUser=null;
	private String accountPassword=null;
	

	public String getPidFile() {
		return pidFile;
	}

	@Override
	public String getAccountUser() {
		return accountUser;
	}

	@Override
	public String getAccountPassword() {
		return accountPassword;
	}
	

	public void setAccountUser(String accountUser) {
		this.accountUser = accountUser;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public void setPidFile(String pidFile) {
		this.pidFile = pidFile;
	}
}
