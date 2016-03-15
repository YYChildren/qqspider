package com.mingchao.snsspider.qq.common;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.mingchao.snsspider.util.BloomFilterUtil;

public class BaseParaments implements Paraments {

	public static final int POOL_SIZE = 1;
	
	protected String projectName="qqspider";
	protected String projectPath=null;
	protected String hadoopPath=null;
	protected int executorPoolSize=POOL_SIZE;
	protected Class<? extends RemoteWebDriver> webDriverClass;
	protected int webDriverPoolSize=POOL_SIZE;
	protected int cookiePoolSize=POOL_SIZE;
	protected long bloomExected= BloomFilterUtil.EXPECTEDENTRIES;
	protected double bloomFpp = BloomFilterUtil.DEFAULT_FPP;
	protected String bloomPath=null;
	protected String accountUser=null;
	protected String accountPassword=null;
	
	public String getProjectName() {
		return projectName;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public String getHadoopPath() {
		return hadoopPath;
	}

	public int getExecutorPoolSize() {
		return executorPoolSize;
	}

	public int getWebDriverPoolSize() {
		return webDriverPoolSize;
	}

	public int getCookiePoolSize() {
		return cookiePoolSize;
	}

	public long getBloomExected() {
		return bloomExected;
	}

	public double getBloomFpp() {
		return bloomFpp;
	}

	public String getBloomPath() {
		return bloomPath;
	}

	public String getAccountUser() {
		return accountUser;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	@Override
	public Class<? extends RemoteWebDriver> getWebDriverClass() {
		return webDriverClass;
	}
}
