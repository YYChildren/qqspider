package com.mingchao.snsspider.qq.common;


public interface Paraments {
	public String getProjectName();

	public String getProjectPath();

	public String getHadoopPath();

	public int getExecutorPoolSize();

	public int getWebDriverPoolSize();

	public int getCookiePoolSize();

	public long getBloomExected();

	public double getBloomFpp();

	public String getBloomPath();

	public String getAccountUser();

	public String getAccountPassword();
}
