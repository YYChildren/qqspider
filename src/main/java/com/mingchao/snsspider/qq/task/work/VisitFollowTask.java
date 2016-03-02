package com.mingchao.snsspider.qq.task.work;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mingchao.snsspider.http.CookieSnsStore;
import com.mingchao.snsspider.http.SubmitTask;
import com.mingchao.snsspider.http.WebDriverWrapper;
import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.model.UserKey;
import com.mingchao.snsspider.qq.model.UserRelation;
import com.mingchao.snsspider.qq.task.VisitTask;
import com.mingchao.snsspider.qq.util.WebDriverUtil;
import com.mingchao.snsspider.util.TimeUtils;

public class VisitFollowTask extends VisitTask {
	private ScheduleFollowKey scheduleFollowKey;

	public VisitFollowTask(ScheduleFollowKey scheduleFollowKey) {
		this.scheduleFollowKey = scheduleFollowKey;
	}

	@Override
	public void execute(){
		fetchNewQq();
	}

	private void fetchNewQq() {
		SubmitTask<List<Long>> getTask = new SubmitTask<List<Long>>() {
			@Override
			public List<Long> submit(WebDriverWrapper webDriverWrapper) {
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
		
		List<Long> qqs = resource.getPool().submit(getTask);
		after(qqs);
		
	}

	//访问页面，返回是否成功
	private boolean visit(WebDriverWrapper webDriverWrapper) {
		Long qq = scheduleFollowKey.getQq();
		String modUrl = resource.getModUrl(qq);
		
		RemoteWebDriver webDriver = webDriverWrapper.getWebDriver();
		
		//返回随机Cookie
		CookieSnsStore cookieStore = resource.getCookieStorePool().getRandomCookies();
		//没有可用Cookie
		if (cookieStore == null) {
			reschaduleRela();
			return false;
		} else {
			//应用随机Cookie
			webDriverWrapper.applyCookieStore(cookieStore);
		}
		
		int tryTime = 2;
		do{
			tryTime--;
			try {
				webDriver.get(modUrl);
				return true;
			} catch (TimeoutException e) {
				log.warn(e, e);
				continue;
			}
		}while(tryTime > 0);
		return false;
	}

	// 未登录状态的处理
	private List<Long> handleNoLogin(WebDriverWrapper webDriverWrapper) {
		log.info(WebDriverUtil.STATUS.NOLOGIN);
		CookieSnsStore cookieStore = webDriverWrapper.getCookieStore();
		if (cookieStore != null) {
			// 删除无用Cookie
			resource.getCookieStorePool().deleteCookieStore(cookieStore);
			// 设置webDriver的cookie为null
			webDriverWrapper.setCookieStore(null);
		}
		reschaduleRela();
		return null;
	}

	//无访问权限的处理
	private List<Long> handleNoProvilege(WebDriverWrapper webDriverWrapper) {
		// 设置为无权访问
		UserKey uk = new UserKey();
		uk.setQq(scheduleFollowKey.getQq());
		uk.setVisitable(false);
		resource.getStorage().insertDuplicate(uk);
		return null;
	}
	
	//有访问权限的处理
	private List<Long> handleProvilege(WebDriverWrapper webDriverWrapper) {
		// 设置为有权访问
		UserKey uk = new UserKey();
		uk.setQq(scheduleFollowKey.getQq());
		uk.setVisitable(true);
		resource.getStorage().insertDuplicate(uk);
		
		RemoteWebDriver webDriver =  webDriverWrapper.getWebDriver();
		Long qq = scheduleFollowKey.getQq();
		Integer pageNum = scheduleFollowKey.getPageNum();
		webDriver.switchTo().frame(webDriver.findElement(By.xpath("//iframe[@class='app_canvas_frame']")));
		
		List<Long> qqs = null;
		boolean isOk = false;
		int tryTime = 2;
		do{
			try{
				tryTime--;
				//如果当前页不是1，current标签不是当前页
				if(pageNum != 1 && Integer.parseInt(webDriver.findElement(By.xpath("//div[@id='pager']//span[@class='current']")).getText()) != pageNum){
					WebElement target = webDriver.findElement(By.xpath("//input[starts-with(@id,'pager_go')]"));
					target.sendKeys(String.valueOf(pageNum));
					webDriver.findElement(By.xpath("//button[starts-with(@id,'pager_gobtn')]")).click();
				}
				
				if(pageNum != 1 && Integer.parseInt(webDriver.findElement(By.xpath("//div[@id='pager']//span[@class='current']")).getText()) != pageNum){
					TimeUtils.sleep(100);
					continue;
				}
				
				qqs = new ArrayList<Long>();
				for (WebElement element : webDriver.findElements(By.xpath("//div[@class='comments_content']/a[@class='nickname']"))) {
					//提取QQ号
					Long newQq = Long.parseLong(element.getAttribute("href").replaceFirst("[^\\d]*", "").replaceFirst("[^\\d].*", ""));
					if(!newQq.equals(qq)){
						qqs.add(newQq);
					}
				}
				
				// 判断是否有下一页，如果有，则调度
				boolean hasNext = true;
				for (WebElement element : webDriver.findElements(By.xpath("//div[@id='pager']//p[@class='mod_pagenav_main']/span"))) {
					if(element.getAttribute("class") == "mod_pagenav_disable" && element.getText().contains("下一页")){
						hasNext = false;
						break;
					}
				}
				if(hasNext){
					schaduleNextRela();
				}
				
				isOk = true;
			}catch (StaleElementReferenceException e){
				TimeUtils.sleep(100);
				continue;
			}catch(NoSuchElementException e){
				
			}
		}while(!isOk && tryTime > 0);
		
		webDriver.switchTo().parentFrame();
		return qqs;
	}
	
	private void after(List<Long> qqs){
		Long qq = scheduleFollowKey.getQq();
		Integer pageNum = scheduleFollowKey.getPageNum();
		if (qqs == null || qqs.isEmpty()) {
			return;
		}
		
		List<ScheduleUserKey> suks = new ArrayList<ScheduleUserKey>();
		List<UserKey> uks = new ArrayList<UserKey>();
		List<UserRelation> urs = new ArrayList<UserRelation>();
		for (Iterator<Long> iterator = qqs.iterator(); iterator.hasNext();) {
			Long newQq = iterator.next();
			ScheduleUserKey suk = new ScheduleUserKey();
			suk.setQq(newQq);
			suks.add(suk);
			UserKey uk = new UserKey();
			uk.setQq(newQq);
			uks.add(uk);
			UserRelation ur = new UserRelation();
			ur.setQq(qq);
			ur.setOqq(newQq);
			urs.add(ur);
		}
		schadule(suks);
		resource.getStorage().insertIgnore(uks);
		resource.getStorage().insertIgnore(urs);
		log.info("get follow, qq: " + qq + ", page: " + pageNum);
	}

	//TODO
	private void schadule(List<ScheduleUserKey> suks) {
		scheduleUser.schadule(suks);
	}

	// 重新添加VisitPeopleFollow任务
	private void reschaduleRela() {
		reschaduleRela(scheduleFollowKey);
	}
	
	// 调度下一页的任务
	private void schaduleNextRela() {
		ScheduleFollowKey  scheduleFollowKey = new ScheduleFollowKey();
		scheduleFollowKey.setQq(this.scheduleFollowKey.getQq());
		scheduleFollowKey.setPageNum(this.scheduleFollowKey.getPageNum() + 1);
		reschaduleRela(scheduleFollowKey);
	}
	
	private void reschaduleRela(ScheduleFollowKey scheduleFollowKey) {
		scheduleFollow.reschadule(scheduleFollowKey);
	}
}
