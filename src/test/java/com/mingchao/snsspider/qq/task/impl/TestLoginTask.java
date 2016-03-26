package com.mingchao.snsspider.qq.task.impl;

import com.mingchao.snsspider.http.webdriver.SubmitTask;
import com.mingchao.snsspider.http.webdriver.WebDriverWrapper;
import com.mingchao.snsspider.qq.common.Paraments;
import com.mingchao.snsspider.qq.provider.ResourceProvider;
import com.mingchao.snsspider.qq.resource.Resource;
import com.mingchao.snsspider.qq.util.LoginUtil;
import com.mingchao.snsspider.qq.util.WebDriverUtil.STATUS;

public class TestLoginTask {

	public static void main(String[] args) {
		Resource resource = ResourceProvider.INSTANCE.getResource();
		try {
			final Paraments para = ResourceProvider.INSTANCE.getParaments();
			final String loginUrl = resource.getLoginUrl();
			SubmitTask<STATUS> getTask = new SubmitTask<STATUS>(){
				@Override
				public STATUS submit(WebDriverWrapper webDriverWrapper) {
					return LoginUtil.login(para, loginUrl, webDriverWrapper);
				}
			};
			resource.getWebDriverPool().submit(getTask );
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			resource.close();
		}
	}
}
