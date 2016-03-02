package com.mingchao.snsspider.qq.provider;

import com.mingchao.snsspider.http.CookieStorePool;

public class CookieStorePoolProvider {
	public static CookieStorePool newInstance() {
		return new CookieStorePool();
	}
}
