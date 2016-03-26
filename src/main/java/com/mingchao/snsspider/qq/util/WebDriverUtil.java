package com.mingchao.snsspider.qq.util;

import java.util.Iterator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;

import com.mingchao.snsspider.http.cookie.CookieSns;
import com.mingchao.snsspider.http.cookie.CookieSnsStore;

public class WebDriverUtil {
	
	public static enum STATUS {
		NOLOGIN, // 未登录
		NOPRIVILEGE, // 已登录,没有权限, 或者是未开通空间
		PRIVILEGE, // 已登录,有权限
		FOF//404, qq号不存在
	}
	
	// TODO 需要添加404状态 <title>404</title>
	// 
	public static STATUS verifyStatus(WebDriver webDriver) {
		String bodyClass = webDriver.findElement(By.xpath("//body")).getAttribute("class");
		if (bodyClass.startsWith("no_privilege")){
			return STATUS.NOPRIVILEGE;
		}
		String title = webDriver.getTitle();
		if(title.endsWith("]")){
			return STATUS.PRIVILEGE;
		}
		if(title.equals("404")){
			return STATUS.FOF;
		}
		return STATUS.NOLOGIN;
	}
	
	public static void addCookies(WebDriver webDriver, CookieSnsStore cl) {
		Options opt = webDriver.manage();
		for (Iterator<CookieSns> iterator = cl.iterator(); iterator.hasNext();) {
			CookieSns cookie = iterator.next();
			opt.addCookie(cookie);
		}
	}
	
}
