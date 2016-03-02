package com.mingchao.snsspider.qq.task.work;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mingchao.snsspider.http.CookieSns;
import com.mingchao.snsspider.http.SubmitTask;
import com.mingchao.snsspider.http.WebDriverWrapper;
import com.mingchao.snsspider.qq.common.Paraments;
import com.mingchao.snsspider.qq.common.ParamentsProvider;
import com.mingchao.snsspider.qq.resource.Resource;
import com.mingchao.snsspider.qq.task.SingleTask;
import com.mingchao.snsspider.task.RunnableTask;

public class LoginTask extends SingleTask{
	private Resource resource = Resource.getInstance();
	private Paraments para =ParamentsProvider.getInstance();
	
	public static RunnableTask newRunnableTask(){
		return new RunnableTask(new LoginTask());
	}
	
	@Override
	public void execute() throws IOException {
		login();
	}
	
	private void login(){
		SubmitTask<List<CookieSns>> getTask = new SubmitTask<List<CookieSns>>(){

			@Override
			public List<CookieSns> submit(WebDriverWrapper webDriverWrapper) {
				RemoteWebDriver webDriver = webDriverWrapper.getWebDriver();
				webDriver.manage().deleteAllCookies();
				webDriver.get(resource.getMainUrl());
				webDriver.switchTo().frame("login_frame");
				try {
					webDriver.findElement(By.id("switcher_plogin")).click();
					webDriver.findElement(By.id("u")).sendKeys(para.getAccountUser().toString());
					webDriver.findElement(By.id("p")).sendKeys(para.getAccountPassword());
					webDriver.findElement(By.id("login_button")).click();
					if(webDriver.findElements(By.id("QZ_Toolbar_Container")).isEmpty()){
						return null;
					}
					List<CookieSns> cookies = new ArrayList<CookieSns>();
					for (Cookie cookie : webDriver.manage().getCookies()) {
						cookies.add(new CookieSns(cookie));
					}
					return cookies;
				} finally{
					webDriver.switchTo().parentFrame();
				}
			}
		};
		List<CookieSns> cookieList = resource.getPool().submit(getTask);
		if(cookieList != null){
			resource.getCookieStorePool().addCookieStore(cookieList);
		}else{
			log.warn("login failed!");
		}
	}
}
