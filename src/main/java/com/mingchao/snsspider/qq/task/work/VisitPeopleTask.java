package com.mingchao.snsspider.qq.task.work;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mingchao.snsspider.http.CookieSnsStore;
import com.mingchao.snsspider.http.SubmitTask;
import com.mingchao.snsspider.http.WebDriverWrapper;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.model.UserInfo;
import com.mingchao.snsspider.qq.task.VisitTask;
import com.mingchao.snsspider.qq.util.WebDriverUtil;

public class VisitPeopleTask extends VisitTask {

	private ScheduleUserKey schUserKey;

	public VisitPeopleTask(ScheduleUserKey userKey) {
		this.schUserKey = userKey;
	}

	@Override
	public void execute() {
		getUserInfo();
	}

	private void getUserInfo() {
		SubmitTask<UserInfo> getTask = new SubmitTask<UserInfo>() {

			@Override
			public UserInfo submit(WebDriverWrapper webDriverWrapper) {
				
				if (!visit(webDriverWrapper)) {
					return null;
				}
				switch (WebDriverUtil.verifyStatus(webDriverWrapper
						.getWebDriver())) {
				case NOLOGIN:
					return handleNoLogin(webDriverWrapper);
				case NOPRIVILEGE:
					return handleNoProvilege(webDriverWrapper);
				case PRIVILEGE:
					return handleProvilege(webDriverWrapper);
				}
				return null;
			}
		};
		UserInfo info = resource.getPool().submit(getTask);
		storage(info);
	}
	
	protected boolean visit(WebDriverWrapper webDriverWrapper) {
		Long qq = schUserKey.getQq();
		RemoteWebDriver webDriver = webDriverWrapper.getWebDriver();
		
		if(webDriverWrapper.getCookieStore() == null){
			CookieSnsStore cookieStore = resource.getCookieStorePool().getRandomCookies();
			if(cookieStore == null){
				reschadule();
				return false;
			}else{
				webDriverWrapper.applyCookieStore(cookieStore);
			}
		}
		
		// 使用Cookie池里的新Cookie,TODO 新登录任务
		CookieSnsStore cookies2 = resource.getCookieStorePool().getRandomCookies();
		WebDriverUtil.addCookies(webDriver, cookies2);
		webDriverWrapper.setCookieStore(cookies2);
		String profileUrl = resource.getUserProfileUrl(qq);
		webDriver.get(profileUrl);
		return true;
	}

	private UserInfo handleNoLogin(WebDriverWrapper webDriverWrapper){
		log.info(WebDriverUtil.STATUS.NOLOGIN);
		CookieSnsStore cookies = webDriverWrapper.getCookieStore();
		if (cookies != null) {
			// 删除无用Cookie
			resource.getCookieStorePool().deleteCookieStore(cookies);
			//设置webDriver的cookie为null
			webDriverWrapper.setCookieStore(null);
		}
		return null;
	}

	private UserInfo handleNoProvilege(WebDriverWrapper webDriverWrapper) {
		return null;
	}
	
	private UserInfo handleProvilege(WebDriverWrapper webDriverWrapper) {
		Long qq = schUserKey.getQq();
		RemoteWebDriver webDriver = webDriverWrapper.getWebDriver();
		
		webDriver.switchTo().frame(webDriver.findElement(By.xpath("//iframe[@class='app_canvas_frame']")));
		webDriver.findElement(By.id("info_link")).click();
		WebElement wele = webDriver.findElement(By.id("info_preview")).findElement(By.xpath("//div[@class=\"preview_list\"]/ul"));
		
		String sex = wele.findElement(By.id("sex")).getText();// 性别
		String age = wele.findElement(By.id("age")).getText();// 年龄
		String birthday = wele.findElement(By.id("birthday")).getText();// 生日
		String astro = wele.findElement(By.id("astro")).getText();// 星座
		String live_address = wele.findElement(By.id("live_address")).getText();// 现居地
		String marriage = wele.findElement(By.id("marriage")).getText();// 婚姻状况
		String blood = wele.findElement(By.id("blood")).getText();// 血型
		String hometown_address = wele.findElement(By.id("hometown_address")).getText();// 故乡
		String career = wele.findElement(By.id("career")).getText();// 职业
		String company = wele.findElement(By.id("company")).getText();// 公司名称
		String company_cadress = wele.findElement(By.id("company_caddress")).getText();// 公司所在地
		String caddress = wele.findElement(By.id("caddress")).getText();// 详细地址
		
		UserInfo info = new UserInfo();
		info.setQq(qq);
		info.setSex(sex);
		info.setAge(age);
		info.setBirthday(birthday);
		info.setAstro(astro);
		info.setLive_address(live_address);
		info.setMarriage(marriage);
		info.setBlood(blood);
		info.setHometown_address(hometown_address);
		info.setCareer(career);
		info.setCompany(company);
		info.setCompany_cadress(company_cadress);
		info.setCaddress(caddress);
		
		webDriver.switchTo().parentFrame();
		log.info("get profile, qq: " + qq);
		return info;
	}
	
	private void reschadule() {
		// 添加VisitPeople任务
		scheduleUser.reschadule(schUserKey);
	}

	private void storage(UserInfo info){
		if (info == null) {
			return;
		}
		resource.getStorage().insertDuplicate(info);
	}
}
