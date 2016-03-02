package com.mingchao.snsspider.qq.provider;

import com.mingchao.snsspider.http.WebDriverPool;

public class WebDriverProvider {
	public static WebDriverPool newInstance() {
		return new WebDriverPool();
	}
}
