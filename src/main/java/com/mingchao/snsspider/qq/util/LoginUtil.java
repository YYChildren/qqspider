package com.mingchao.snsspider.qq.util;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mingchao.snsspider.http.WebDriverWrapper;
import com.mingchao.snsspider.qq.common.Paraments;
import com.mingchao.snsspider.qq.util.WebDriverUtil.STATUS;

public class LoginUtil {

	public static STATUS login(Paraments para, String loginUrl,
			WebDriverWrapper webDriverWrapper) {
		RemoteWebDriver webDriver = webDriverWrapper.getWebDriver();
		try {
			webDriver.get(loginUrl);
			webDriver.switchTo().frame("login_frame");
			webDriver.executeScript("arguments[0].click();", webDriver.findElement(By.id("switcher_plogin")));
			webDriver.executeScript("arguments[0].value='"+para.getAccountUser()+"';", webDriver.findElement(By.id("u")));
			webDriver.executeScript("arguments[0].value='"+para.getAccountPassword()+"';", webDriver.findElement(By.id("p")));
			webDriver.executeScript("arguments[0].click();", webDriver.findElement(By.id("login_button")));
			return WebDriverUtil.verifyStatus(webDriver);
		} finally {
			webDriver.switchTo().defaultContent();
		}
	}
}
