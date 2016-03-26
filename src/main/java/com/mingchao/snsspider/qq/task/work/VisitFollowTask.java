package com.mingchao.snsspider.qq.task.work;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mingchao.snsspider.exception.NPInterruptedException;
import com.mingchao.snsspider.http.webdriver.WebDriverWrapper;
import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.model.UserKey;
import com.mingchao.snsspider.qq.model.UserRelation;
import com.mingchao.snsspider.qq.task.VisitTask;
import com.mingchao.snsspider.qq.util.WebDriverUtil;
import com.mingchao.snsspider.schedule.Schedule;
import com.mingchao.snsspider.util.TimeUtils;

public class VisitFollowTask extends VisitTask<List<Long>>{
	private Schedule<ScheduleFollowKey> scheduleFollow = resource.getScheduleFollow(); 
	private Schedule<ScheduleUserKey> scheduleUser = resource.getScheduleUser();
	private ScheduleFollowKey scheduleFollowKey;
	private Long qq;
	private Integer pageNum;
	private String modUrl;

	public VisitFollowTask(ScheduleFollowKey scheduleFollowKey) {
		this.scheduleFollowKey = scheduleFollowKey;
		qq = scheduleFollowKey.getQq();
		pageNum = scheduleFollowKey.getPageNum();
		modUrl = resource.getModUrl(qq);
	}
	
	@Override
	protected boolean visit(WebDriverWrapper webDriverWrapper) {
		
		if (tryVisit(modUrl, webDriverWrapper)) {
			return true;
		}else{
			reschaduleRela();
			return false;
		}
	}
	
	// 未登录状态的处理
	@Override
	protected List<Long> handleNoLogin(boolean hadTryLogin, WebDriverWrapper webDriverWrapper) {
		log.info(WebDriverUtil.STATUS.NOLOGIN);
		// 如果已经尝试登录过一次
		if(hadTryLogin){
			reschaduleRela();
			return null;
		}else{
			try{
				login(webDriverWrapper);
			} catch (TimeoutException e) {
				log.warn(e, e);
				reschaduleRela();
				return null;
			}
			return doVisit(true, webDriverWrapper);
		}
	}

	//无访问权限的处理
	@Override
	protected List<Long> handleNoProvilege(WebDriverWrapper webDriverWrapper) {
		// 设置为无权访问
		UserKey uk = new UserKey();
		uk.setQq(qq);
		uk.setVisitable(false);
		resource.getStorage().insertDuplicate(uk);
		return null;
	}
	
	//有访问权限的处理
	@Override
	protected List<Long> handleProvilege(WebDriverWrapper webDriverWrapper) {
		// 设置为有权访问
		UserKey uk = new UserKey();
		uk.setQq(qq);
		uk.setVisitable(true);
		resource.getStorage().insertDuplicate(uk);
		
		RemoteWebDriver webDriver =  webDriverWrapper.getWebDriver();
		webDriver.switchTo().frame(webDriver.findElement(By.xpath("//iframe[@class='app_canvas_frame']")));
		
		List<Long> qqs = null;
		boolean isOk = false;
		int tryTime = 2;
		do{
			try{
				tryTime--;
				//如果当前页不是1，current标签不是当前页
				if(pageNum != 1 && Integer.parseInt(webDriver.findElement(By.xpath("//div[@id='pager']//span[@class='current']/span")).getText()) != pageNum){
					WebElement target = webDriver.findElement(By.xpath("//input[starts-with(@id,'pager_go')]"));
					webDriver.executeScript("arguments[0].value='"+String.valueOf(pageNum)+"';", target);
					webDriver.executeScript("arguments[0].click();", 
							webDriver.findElement(By.xpath("//button[starts-with(@id,'pager_gobtn')]")));
				}
				
				if(pageNum != 1 && Integer.parseInt(webDriver.findElement(By.xpath("//div[@id='pager']//span[@class='current']/span")).getText()) != pageNum){
					try {
						TimeUtils.sleep(100);
					} catch (InterruptedException e) {
						throw new NPInterruptedException(e);
					}
					continue;
				}
				
				qqs = new ArrayList<Long>();
				for (WebElement element : webDriver.findElements(By.xpath("//div[@class='comments_content']/a[@class='nickname']"))) {
					//提取QQ号
					String link = element.getAttribute("href");
					if(link.contains("qzone")){//有可能是朋友网链接，不提取
						Long newQq = Long.parseLong(link.replaceFirst("[^\\d]*", "").replaceFirst("[^\\d].*", ""));
						if(!newQq.equals(qq)){
							qqs.add(newQq);
						}
					}
				}
				
				// 判断是否有下一页，如果有，则调度
				boolean hasNext = true;
				WebElement pager = webDriver.findElement(By.xpath("//div[@id='pager']"));
				if(pager.getAttribute("style").equals("display: none;")){//没有显示，肯定是没有下一页
					hasNext = false;
				}else{
					for (WebElement element : webDriver.findElements(
							By.xpath("//div[@id='pager']//p[@class='mod_pagenav_main']"))) {
						//下一页被禁用，说明没有下一页
						if(element.getAttribute("class") == "mod_pagenav_disable" 
								&& element.findElement(By.xpath("/span")).getText().contains("下一页")){
							hasNext = false;
							break;
						}
					}
				}
				if(hasNext){
					schaduleNextRela();
				}
				
				isOk = true;
			}catch (TimeoutException | StaleElementReferenceException e){
				if(tryTime==0){
					reschaduleRela();
				}
				try {
					TimeUtils.sleep(100);
				} catch (InterruptedException e1) {
					throw new NPInterruptedException(e1);
				}
				continue;
			}catch(NoSuchElementException e){
				break;
			}
		}while(!isOk && tryTime > 0);
		webDriver.switchTo().defaultContent();
		return qqs;
	}

	@Override
	protected List<Long> handleException(WebDriverException  e,WebDriverWrapper webDriverWrapper) {
		reschaduleRela();
		throw e;
	}
	
	@Override
	protected List<Long> handleException(NPInterruptedException  e,WebDriverWrapper webDriverWrapper) {
		reschaduleRela();
		throw e;
	}


	@Override
	protected void handleResult(List<Long> info) {
		if (info == null || info.isEmpty()) {
			return;
		}
		LinkedHashSet<Long> qqsDeWeight = new LinkedHashSet<Long>();//去重
		qqsDeWeight.addAll(info);
		List<ScheduleUserKey> suks = new ArrayList<ScheduleUserKey>();
		List<UserKey> uks = new ArrayList<UserKey>();
		List<UserRelation> urs = new ArrayList<UserRelation>();
		for (Iterator<Long> iterator = qqsDeWeight.iterator(); iterator.hasNext();) {
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
		scheduleFollowKey.setId(null);
		reschaduleRela(scheduleFollowKey);
	}
	
	// 调度下一页的任务
	private void schaduleNextRela() {
		ScheduleFollowKey newScheduleFollowKey = new ScheduleFollowKey();
		newScheduleFollowKey.setQq(qq);
		newScheduleFollowKey.setPageNum(pageNum + 1);
		reschaduleRela(newScheduleFollowKey);
	}
	
	private void reschaduleRela(ScheduleFollowKey scheduleFollowKey) {
		scheduleFollow.reschadule(scheduleFollowKey);
	}


}
