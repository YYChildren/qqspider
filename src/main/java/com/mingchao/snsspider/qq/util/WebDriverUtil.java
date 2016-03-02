package com.mingchao.snsspider.qq.util;

import java.util.Iterator;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;

import com.mingchao.snsspider.http.CookieSns;
import com.mingchao.snsspider.http.CookieSnsStore;

public class WebDriverUtil {
	
	public static enum STATUS {
		NOLOGIN, // 未登录
		NOPRIVILEGE, // 已登录,没有权限
		PRIVILEGE // 已登录,有权限
	}
	
	public static STATUS verifyStatus(WebDriver webDriver) {
		if (webDriver.findElement(By.xpath("//body")).getAttribute("class").startsWith("no_privilege")){
			return STATUS.NOPRIVILEGE;
		}
		try {
			webDriver.findElement(By.id("QZ_Toolbar_Container"));
			return STATUS.PRIVILEGE;
		} catch (NoSuchElementException e) {
			return STATUS.NOLOGIN;
		}
	}
	
	public static void addCookies(WebDriver webDriver, CookieSnsStore cl) {
		Options opt = webDriver.manage();
		for (Iterator<CookieSns> iterator = cl.iterator(); iterator.hasNext();) {
			CookieSns cookie = iterator.next();
			opt.addCookie(cookie);
		}
	}
	
}
