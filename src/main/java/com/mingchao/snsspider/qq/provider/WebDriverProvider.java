package com.mingchao.snsspider.qq.provider;

import com.mingchao.snsspider.http.WebDriverPool;
import com.mingchao.snsspider.qq.common.ParamentsProvider;

public class WebDriverProvider {
	public static WebDriverPool newInstance() {
		return new WebDriverPool(ParamentsProvider.getInstance()
				.getWebDriverPoolSize());
	}
}
