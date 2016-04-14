package com.mingchao.snsspider.qq.task;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mingchao.snsspider.exception.NPInterruptedException;
import com.mingchao.snsspider.exception.WebDriverCosingException;
import com.mingchao.snsspider.http.webdriver.SubmitTask;
import com.mingchao.snsspider.http.webdriver.WebDriverPool;
import com.mingchao.snsspider.http.webdriver.WebDriverWrapper;
import com.mingchao.snsspider.qq.common.Paraments;
import com.mingchao.snsspider.qq.provider.ResourceProvider;
import com.mingchao.snsspider.qq.util.WebDriverUtil;

public abstract class VisitTask<T> extends BaseTaskImpl {
	protected Paraments para = ResourceProvider.INSTANCE.getParaments();
	protected WebDriverPool pool = resource.getWebDriverPool();
	
	@Override
	public void execute() {
		SubmitTask<T> getTask = new SubmitTask<T>() {
			@Override
			public T submit(WebDriverWrapper webDriverWrapper) {
				if(visit(webDriverWrapper)){
					return doVisit(false, webDriverWrapper);
				}else{
					return null;
				}
			}
		};
		T info = null;
		try {
			info = pool.submit(getTask);
		} catch(WebDriverCosingException e){//Do Notting
		} catch (InterruptedException e) {
			log.warn(e,e);
			throw new NPInterruptedException(e);
		}
		handleResult(info);
	}

	protected abstract boolean visit(WebDriverWrapper webDriverWrapper);
	
	protected boolean tryVisit(String url, WebDriverWrapper webDriverWrapper) {
		RemoteWebDriver webDriver = webDriverWrapper.getWebDriver();
		int tryTime = 2;
		do {
			tryTime--;
			try {
				webDriver.get(url);
				return true;
			} catch (TimeoutException e) {
				log.warn(e, e);
				continue;
			}
		} while (tryTime > 0);
		return false;
	}

	protected T doVisit(boolean hadTryLogin, WebDriverWrapper webDriverWrapper) {
		try {
			switch (WebDriverUtil.verifyStatus(webDriverWrapper.getWebDriver())) {
			case NOLOGIN:
				return handleNoLogin(hadTryLogin, webDriverWrapper);
			case NOPRIVILEGE:
				return handleNoProvilege(webDriverWrapper);
			case PRIVILEGE:
				return handleProvilege(webDriverWrapper);
			default:
				return null;
			}
		} catch (WebDriverException e) {
			return handleException(e, webDriverWrapper);
		} catch (NPInterruptedException e){
			return handleException(e, webDriverWrapper);
		}
	}

	protected abstract T handleNoLogin(boolean hadTryLogin, WebDriverWrapper webDriverWrapper);

	protected abstract T handleNoProvilege(WebDriverWrapper webDriverWrapper);

	protected abstract T handleProvilege(WebDriverWrapper webDriverWrapper);

	protected abstract T handleException(WebDriverException e,
			WebDriverWrapper webDriverWrapper);
	
	protected abstract T handleException(NPInterruptedException e,
			WebDriverWrapper webDriverWrapper);
	
	protected abstract void handleResult(T info);

	protected boolean login(WebDriverWrapper webDriverWrapper) {
		RemoteWebDriver webDriver = webDriverWrapper.getWebDriver();
		log.info("Try login");
		for (int i = 0; i < 3; i++) {
			try {
				if(WebDriverUtil.verifyStatus(webDriver) != WebDriverUtil.STATUS.NOLOGIN){
					return true;
				}
				webDriver.switchTo().frame("login_frame");
				webDriver.executeScript("arguments[0].click();", webDriver.findElement(By.id("switcher_plogin")));
				webDriver.executeScript("arguments[0].value='"+para.getAccountUser()+"';", webDriver.findElement(By.id("u")));
				webDriver.executeScript("arguments[0].value='"+para.getAccountPassword()+"';", webDriver.findElement(By.id("p")));
				webDriver.executeScript("arguments[0].click();", webDriver.findElement(By.id("login_button")));
				webDriver.switchTo().defaultContent();
				if(WebDriverUtil.verifyStatus(webDriver) != WebDriverUtil.STATUS.NOLOGIN){
					return true;
				}
			} catch (TimeoutException | StaleElementReferenceException e) {
			}
		}
		return false;
	}
}
