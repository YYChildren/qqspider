package com.mingchao.snsspider.qq.resource;

import com.mingchao.snsspider.http.CookieStorePool;
import com.mingchao.snsspider.http.WebDriverPool;
import com.mingchao.snsspider.manager.TaskExcutor;
import com.mingchao.snsspider.qq.provider.CookieStorePoolProvider;
import com.mingchao.snsspider.qq.provider.StorageProvider;
import com.mingchao.snsspider.qq.provider.TaskExecutorProvider;
import com.mingchao.snsspider.qq.provider.WebDriverProvider;
import com.mingchao.snsspider.storage.Storage;

public class Resource {
	//只用Resource 单例，其他资源是否单例由Resource控制
	private static Resource instance;
	private Storage storage = StorageProvider.newMySQLStorage();
	private TaskExcutor taskExcutor = TaskExecutorProvider.newInstance();
	private CookieStorePool cookieStorePool = CookieStorePoolProvider.newInstance();
	private WebDriverPool webDriverPool = WebDriverProvider.newInstance();

	private String mainUrl = "http://user.qzone.qq.com/";
	private String userProfileUrl = "http://user.qzone.qq.com/@QQ/profile";
	private String modUrl = "http://user.qzone.qq.com/@QQ/mood";

	private Resource() {
	}

	public void close() {
		webDriverPool.quit();
	}

	public static Resource getInstance() {
		if (instance == null) {
			getInstance2();
		}
		return instance;
	}

	public static synchronized Resource getNewInstance() {
		instance.close();
		instance = null;
		getInstance2();
		return instance;
	}

	private static synchronized void getInstance2() {
		if (instance == null) {
			instance = newInstance();
		}
	}

	public static Resource newInstance() {
		return new Resource();
	}

	public Storage getStorage() {
		return storage;
	}

	public TaskExcutor getTaskExcutor() {
		return taskExcutor;
	}

	public CookieStorePool getCookieStorePool() {
		return cookieStorePool;
	}

	public WebDriverPool getWebDriverPool() {
		return webDriverPool;
	}

	public WebDriverPool getPool() {
		return webDriverPool;
	}

	public String getMainUrl() {
		return mainUrl;
	}

	public String getUserProfileUrl(Long qq) {
		return userProfileUrl.replace("@QQ", qq.toString());
	}

	public String getUserProfileUrl() {
		return userProfileUrl;
	}

	public String getModUrl() {
		return modUrl;
	}

	public String getModUrl(Long qq) {
		return modUrl.replace("@QQ", qq.toString());
	}
}
