package com.mingchao.snsspider.qq.task.work;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mingchao.snsspider.exception.NPInterruptedException;
import com.mingchao.snsspider.http.webdriver.WebDriverWrapper;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.model.UserInfo;
import com.mingchao.snsspider.qq.task.VisitTask;
import com.mingchao.snsspider.qq.util.WebDriverUtil;
import com.mingchao.snsspider.schedule.Schedule;

public class VisitProfileTask extends VisitTask<String> {

	private ScheduleUserKey schUserKey;
	private Schedule<ScheduleUserKey> scheduleUser = resource.getScheduleUser(); 

	public VisitProfileTask(ScheduleUserKey userKey) {
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
	protected String handleNoLogin(boolean hadTryLogin,
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
	protected String handleNoProvilege(WebDriverWrapper webDriverWrapper) {
		return null;
	}

	@Override
	protected String handleProvilege(WebDriverWrapper webDriverWrapper) {
		
		RemoteWebDriver webDriver = webDriverWrapper.getWebDriver();
		webDriver.switchTo().frame(webDriver.findElement(By.xpath("//iframe[@class='app_canvas_frame']")));
		webDriver.executeScript("arguments[0].click();", webDriver.findElement(By.id("info_link")));
		WebElement wele = webDriver.findElement(By.id("info_preview")).findElement(By.xpath("//div[@class=\"preview_list\"]/ul"));
		wele.findElement(By.id("sex"));// 性别
		wele.findElement(By.id("age"));// 年龄
		wele.findElement(By.id("birthday"));// 生日
		wele.findElement(By.id("astro"));// 星座
		wele.findElement(By.id("live_address"));// 现居地
		wele.findElement(By.id("marriage"));// 婚姻状况
		wele.findElement(By.id("blood"));// 血型
		wele.findElement(By.id("hometown_address"));// 故乡
		wele.findElement(By.id("career"));// 职业
		wele.findElement(By.id("company"));// 公司名称
		wele.findElement(By.id("company_caddress"));// 公司所在地
		wele.findElement(By.id("caddress"));// 详细地址
		String source = webDriver.getPageSource();
		webDriver.switchTo().defaultContent();
		return source;
	}

	@Override
	protected String handleException(WebDriverException e,
			WebDriverWrapper webDriverWrapper) {
		reschadule();
		throw e;
	}
	
	@Override
	protected String handleException(NPInterruptedException e,
			WebDriverWrapper webDriverWrapper) {
		reschadule();
		throw e;
	}

	@Override
	protected void handleResult(String source) {
		if (source == null) {
			return;
		}
		Document doc =  Jsoup.parse(source);
		String name = doc.getElementById("nickname_n").text();
		org.jsoup.select.Elements vipElements = doc.getElementById("vip_span").select("a[href]");
		String vip = vipElements.isEmpty() ? null : vipElements.get(0).attr("class");
		
		Long qq = schUserKey.getQq();
		String sex = doc.getElementById("sex").text(); // 性别
		String age = doc.getElementById("age").text();// 年龄
		String birthday = doc.getElementById("birthday").text();// 生日
		String astro = doc.getElementById("astro").text();// 星座
		String live_address = doc.getElementById("live_address").text();// 现居地
		String marriage = doc.getElementById("marriage").text();// 婚姻状况
		String blood = doc.getElementById("blood").text();// 血型
		String hometown_address = doc.getElementById("hometown_address").text();// 故乡
		String career = doc.getElementById("career").text();// 职业
		String company = doc.getElementById("company").text();// 公司名称
		String company_cadress = doc.getElementById("company_caddress").text();// 公司所在地
		String caddress = doc.getElementById("caddress").text();// 详细地址
		
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
		log.info(name);
		log.info(vip);
		resource.getStorage().insertDuplicate(info);
		log.info("get profile, qq: " + qq);
		
	}
	
	private void reschadule() {
		schUserKey.setId(null);
		scheduleUser.reschadule(schUserKey);
	}

}
