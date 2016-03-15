package com.mingchao.snsspider.qq.common;
import org.openqa.selenium.remote.RemoteWebDriver;


public interface Paraments {
	public String getProjectName();

	public String getProjectPath();

	public String getHadoopPath();

	public int getExecutorPoolSize();

	public Class<? extends RemoteWebDriver> getWebDriverClass();
	
	public int getWebDriverPoolSize();

	public int getCookiePoolSize();

	public long getBloomExected();

	public double getBloomFpp();

	public String getBloomPath();

	public String getAccountUser();

	public String getAccountPassword();
}
