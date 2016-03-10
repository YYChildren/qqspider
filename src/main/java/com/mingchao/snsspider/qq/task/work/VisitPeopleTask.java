package com.mingchao.snsspider.qq.task.work;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mingchao.snsspider.exception.NPInterruptedException;
import com.mingchao.snsspider.http.WebDriverWrapper;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.model.UserInfo;
import com.mingchao.snsspider.qq.task.VisitTask;
import com.mingchao.snsspider.qq.util.WebDriverUtil;
import com.mingchao.snsspider.schedule.Schedule;

public class VisitPeopleTask extends VisitTask<UserInfo> {

	private ScheduleUserKey schUserKey;
	private Schedule<ScheduleUserKey> scheduleUser = resource.getScheduleUser(); 

	public VisitPeopleTask(ScheduleUserKey userKey) {
		this.schUserKey = userKey;
	}

	@Override
	protected boolean visit(WebDriverWrapper webDriverWrapper) {
		Long qq = schUserKey.getQq();
		String profileUrl = resource.getUserProfileUrl(qq);
		if (tryVisit(profileUrl, webDriverWrapper)) {
			return true;
		}else{
			reschadule();
			return false;
		}
	}

	@Override
	protected UserInfo handleNoLogin(boolean hadTryLogin,
			WebDriverWrapper webDriverWrapper) {
		log.info(WebDriverUtil.STATUS.NOLOGIN);
		// 如果已经尝试登录过一次
		if(hadTryLogin){
			reschadule();
			return null;
		}else{
			try{
				login(webDriverWrapper);
			} catch (TimeoutException e) {
				log.warn(e, e);
				reschadule();
				return null;
			}
			return doVisit(true, webDriverWrapper);
		}
	}

	@Override
	protected UserInfo handleNoProvilege(WebDriverWrapper webDriverWrapper) {
		return null;
	}

	@Override
	protected UserInfo handleProvilege(WebDriverWrapper webDriverWrapper) {
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
		
		webDriver.switchTo().defaultContent();
		log.info("get profile, qq: " + qq);
		return info;
	}

	@Override
	protected UserInfo handleException(WebDriverException e,
			WebDriverWrapper webDriverWrapper) {
		reschadule();
		throw e;
	}
	
	@Override
	protected UserInfo handleException(NPInterruptedException e,
			WebDriverWrapper webDriverWrapper) {
		reschadule();
		throw e;
	}

	@Override
	protected void handleResult(UserInfo info) {
		if (info == null) {
			return;
		}
		resource.getStorage().insertDuplicate(info);
	}
	
	private void reschadule() {
		schUserKey.setId(null);
		scheduleUser.reschadule(schUserKey);
	}

}
