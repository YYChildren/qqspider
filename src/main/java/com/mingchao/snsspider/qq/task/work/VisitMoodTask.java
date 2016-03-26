package com.mingchao.snsspider.qq.task.work;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import com.mingchao.snsspider.qq.model.SecondaryUserComment;
import com.mingchao.snsspider.qq.model.UserComment;
import com.mingchao.snsspider.qq.model.UserCommentId;
import com.mingchao.snsspider.qq.model.UserKey;
import com.mingchao.snsspider.qq.model.UserMood;
import com.mingchao.snsspider.qq.model.UserMoodId;
import com.mingchao.snsspider.qq.model.UserRelation;
import com.mingchao.snsspider.qq.task.VisitTask;
import com.mingchao.snsspider.qq.util.WebDriverUtil;
import com.mingchao.snsspider.schedule.Schedule;
import com.mingchao.snsspider.storage.Storage;
import com.mingchao.snsspider.util.TimeUtils;

public class VisitMoodTask extends VisitTask<String>{
	private Schedule<ScheduleFollowKey> scheduleFollow = resource.getScheduleFollow(); 
	private Schedule<ScheduleUserKey> scheduleUser = resource.getScheduleUser();
	private Storage storage = resource.getStorage();
	private Storage storageMogo = resource.getStorageMongo();
	private ScheduleFollowKey scheduleFollowKey;
	private Long qq;
	private Integer pageNum;
	private String modUrl;
	private List<UserMood> moods;
	private Set<Long> newQqs;

	public VisitMoodTask(ScheduleFollowKey scheduleFollowKey) {
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
	protected String handleNoLogin(boolean hadTryLogin, WebDriverWrapper webDriverWrapper) {
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
	protected String handleNoProvilege(WebDriverWrapper webDriverWrapper) {
		// 设置为无权访问
		UserKey uk = new UserKey();
		uk.setQq(qq);
		uk.setVisitable(false);
		storage.insertDuplicate(uk);
		return null;
	}
	
	//有访问权限的处理
	@Override
	protected String handleProvilege(WebDriverWrapper webDriverWrapper) {
		// 设置为有权访问
		UserKey uk = new UserKey();
		uk.setQq(qq);
		uk.setVisitable(true);
		storage.insertDuplicate(uk);
		
		
		String pageSource = null;
		RemoteWebDriver webDriver =  webDriverWrapper.getWebDriver();
		webDriver.switchTo().frame(webDriver.findElement(By.xpath("//iframe[@class='app_canvas_frame']")));
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
				
				// 相当于等待页面加载完成
				webDriver.findElements(By.xpath("//li[@class='feed']"));
				//webDriver.findElements(By.xpath("//div[@class='comments_content']/a[@class='nickname']"));
				pageSource = webDriver.getPageSource();
				
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
		return pageSource;
	}

	@Override
	protected String handleException(WebDriverException  e,WebDriverWrapper webDriverWrapper) {
		reschaduleRela();
		throw e;
	}
	
	@Override
	protected String handleException(NPInterruptedException  e,WebDriverWrapper webDriverWrapper) {
		reschaduleRela();
		throw e;
	}

	@Override
	protected void handleResult(String pageSource) {
		moods = null;
		newQqs = null;
		analyze(pageSource);
		if(newQqs!=null && !newQqs.isEmpty()){
			newQqs.remove(qq);//删除自身
			List<ScheduleUserKey> suks = new ArrayList<ScheduleUserKey>();
			List<UserRelation> urs = new ArrayList<UserRelation>();
			for (Iterator<Long> iterator = newQqs.iterator(); iterator.hasNext();) {
				Long newQq = iterator.next();
				ScheduleUserKey suk = new ScheduleUserKey();
				suk.setQq(newQq);
				suks.add(suk);
				UserRelation ur = new UserRelation();
				ur.setQq(qq);
				ur.setOqq(newQq);
				urs.add(ur);
			}
			schadule(suks);                                    //调度新QQ
			storage.insertIgnore(urs);                     //持久化关系链
		}
		if(moods !=null && !moods.isEmpty()){
			storageMogo.insertDuplicate(moods);  //持久化说说
		}
		log.info("get follow, qq: " + qq + ", page: " + pageNum);
	}
	
	public void analyze(String pageSource) {
		if (pageSource == null || pageSource.isEmpty()) {
			return;
		}
		moods = new ArrayList<UserMood>();
		newQqs = new HashSet<Long>();
		Long currenttime = System.currentTimeMillis();
		Document doc = Jsoup.parse(pageSource);
		Element msgList = doc.getElementById("msgList");//ol 标签
		for (Element element : msgList.children()) {//li标签
			UserMood userMood = new UserMood();
			
			UserMoodId id = new UserMoodId();
			{
				id.setQq(qq);
				id.setDataid(element.attr("data-tid"));
			}
			
			String mood = null;
			String createtime=null;
			//Long currenttime;

			//like
			Integer likecount = null;
			List<Long> likeqqs = null;
			
			// comment
			Integer commentcount = null;
			List<UserComment> comments = null;
			
			// forward
			Integer forwardcount = null;
			Boolean isforward = null;//是不是转发，如果是转发，所有点赞归原说说
			UserMood forwardmood = null;//转发的内容
			
			for(Element feedchild : element.getElementsByClass("box bgr3").get(0).children()){
				switch(feedchild.attr("class")){
				case "bd":
					mood = feedchild.getElementsByTag("pre").get(0).text();
					break;
				case "md":                    //TODO 可能有图片
					break;
				case "md rt_content":    //转发
					isforward=true;
					forwardmood = getForwardMood(feedchild);
					break;
				case "ft":                      //时间  --  数量（赞 -- 评论  -- 转发）
					Element infoElement = feedchild.getElementsByClass("info").get(0)
						.getElementsByTag("a").get(0);
					createtime = infoElement.text();
				
					Element opElement = feedchild.getElementsByClass("op").get(0);
					String likeText = opElement.select("a[_origtemp=赞{cnt}]").get(0).text().replace("赞", "");
					likecount = likeText.equals("") ? null : Integer.parseInt(likeText.replaceAll("[^\\d]*", ""));
					String commentText = opElement.getElementsByClass("c_tx rt_comment_btn").get(0).text().replace("评论", "");
					commentcount = commentText.equals("") ? null : Integer.parseInt(commentText.replaceAll("[^\\d]*", ""));
					String forwardText = opElement.getElementsByClass("c_tx rt_forward_btn").get(0).text().replace("转发", "");
					forwardcount = forwardText.equals("") ? null : Integer.parseInt(forwardText.replaceAll("[^\\d]*", ""));
				
					break;
				case "box_extra bor3": //点赞、评论具体情况
					Elements feedLikeEles = feedchild.getElementsByClass("feed_like");
					if(!feedLikeEles.isEmpty()){
						likeqqs = new ArrayList<Long>();
						for (Element element2 : feedLikeEles.select("a[href]")) {
							String href = element2.attr("href");
							if(href.contains("qzone.qq.com")){
								Long likeqq = Long.parseLong(href.replaceFirst("[^\\d]*", "").replaceFirst("[^\\d].*", ""));
								likeqqs.add(likeqq);
							}
						}
						if(likeqqs.isEmpty()){
							likeqqs = null;
						}else{
							newQqs.addAll(likeqqs);//点赞为新增QQ号
						}
					}
					comments = getUserComments(id,feedchild);
					break;
				}
			}
			
			userMood.setId(id);
			userMood.setMood(mood);
			userMood.setCreatetime(createtime);
			userMood.setCurrenttime(currenttime);
			userMood.setLikecount(likecount);
			userMood.setLikeqqs(likeqqs);
			userMood.setCommentcount(commentcount);
			userMood.setComments(comments);
			userMood.setForwardcount(forwardcount);
			userMood.setIsforward(isforward);
			userMood.setForwardmood(forwardmood);
			moods.add(userMood);
		}
	}
	
	private UserMood getForwardMood(Element element){
		UserMood forwardUserMood = new UserMood();
		UserMoodId forwardId = null; 
		String dataid = null;
		String createtime = null;
		Integer commentcount = null;
		Integer forwardcount = null;
		
		
		for (Element child : element.child(0).children()) {
			switch(element.attr("class")){
			case "bd":
				Long fqq = Long.parseLong(child.getElementsByTag("a").get(0).attr("data-uin"));
				String mood = child.getElementsByClass("pre").get(0).text();
				forwardId = new UserMoodId();
				forwardId.setQq(fqq);
				forwardUserMood.setMood(mood);
				break;
			case "md":  //TODO 可能有照片
				break;
			case "ft":
				Element infoElement = child.getElementsByClass("info").get(0)
					.getElementsByTag("a").get(0);
				String infoUrl = infoElement.attr("href");
				dataid = infoUrl.replaceFirst(".*mood/", "").replaceAll("\\..*", "");
				createtime = infoElement.text();
				
				Element opElement = child.getElementsByClass("op").get(0);
				String commentText = opElement.getElementsByClass("c_tx rt_comment_btn").get(0).text().replace("评论", "");
				commentcount = commentText.equals("") ? null : Integer.parseInt(commentText.replaceAll("[^\\d]*", ""));
				String forwardText = opElement.getElementsByClass("c_tx rt_forward_btn").get(0).text().replace("转发", "");
				forwardcount = forwardText.equals("") ? null : Integer.parseInt(forwardText.replaceAll("[^\\d]*", ""));
				
				
				break;
			}
		}
		
		forwardUserMood.setId(forwardId);
		forwardId.setDataid(dataid);
		forwardUserMood.setCreatetime(createtime);
		forwardUserMood.setCommentcount(commentcount);
		forwardUserMood.setForwardcount(forwardcount);
		return forwardUserMood;
	}
	
	private List<UserComment> getUserComments(UserMoodId id, Element element){
		Elements commentEles = element.getElementsByClass("comments_list");
		if(commentEles.isEmpty()){
			return null;
		}
		Element commentUl = null;
		for (Element element2 : commentEles.get(0).children()) {
			if(element2.tagName().equals("ul")){
				commentUl = element2;
				break;
			}
		}
		if(commentUl == null){
			return null;
		}
		List<UserComment> comments = new ArrayList<UserComment>();
		int order = 0;
		for (Element element2 : commentUl.children()) {
			UserComment userComment = null;
			List<SecondaryUserComment> subCommnets = new ArrayList<SecondaryUserComment>(); 
			int i = 0;
			for (Element element3 : element2.getElementsByClass("comments_content")) {
				if(i == 0){
					userComment = getUserComment(id, order++, element3);
				}else{
					SecondaryUserComment subCommnet = getSecondaryUserComment(element3);
					if(subCommnet!=null){
						subCommnets.add(subCommnet);
					}
				}
			}
			if (userComment != null) {
				if (!subCommnets.isEmpty()) {
					userComment.setSecondarycomments(subCommnets);
				}
				comments.add(userComment);
			}
		}
		if(comments.isEmpty()){
			comments = null;
		}
		return comments;
	}
	
	private UserComment getUserComment(UserMoodId id,int order, Element element){
		UserComment userComment = new UserComment();
		UserCommentId ucid = new UserCommentId();
		ucid.setQq(id.getQq());
		ucid.setDataid(id.getDataid());
		ucid.setOrder(order);

		for (Element opElement : element.getElementsByClass("comments_op")) {
			opElement.remove();
		}//删除无用标签
		String qqHref = element.getElementsByClass("nickname").get(0).attr("href");
		Long qq = Long.parseLong(qqHref.replaceFirst("[^\\d]*", "").replaceFirst("[^\\d].*", ""));
		String comment = element.text();
		String time = null;
		for (Element timeElement : element.getElementsByClass("c_tx3 ui_mr10")) {
			if(timeElement.attr("style").isEmpty()){
				time = timeElement.text();
				break;
			}
		}
		userComment.setId(ucid);
		userComment.setQq(qq);
		userComment.setComment(comment);
		userComment.setTime(time);
		return userComment;
	}
	
	private SecondaryUserComment getSecondaryUserComment(Element element){
		for (Element opElement : element.getElementsByClass("comments_op")) {
			opElement.remove();
		}
		SecondaryUserComment subUserComment = new SecondaryUserComment();
		Elements elements = element.getElementsByClass("nickname");
		Long qq = null;
		Long oqq = null;
		if(elements.size() == 2){
			String qqHref = elements.get(0).attr("href");
			String oqqHref = elements.get(0).attr("href");
			qq = Long.parseLong(qqHref.replaceFirst("[^\\d]*", "").replaceFirst("[^\\d].*", ""));
			oqq = Long.parseLong(oqqHref.replaceFirst("[^\\d]*", "").replaceFirst("[^\\d].*", ""));
		}
		String comment = element.text();
		String time = null;
		for (Element timeElement : element.getElementsByClass("c_tx3 ui_mr10")) {
			if(timeElement.attr("style").isEmpty()){
				time = timeElement.text();
				break;
			}
		}
		subUserComment.setQq(qq);
		subUserComment.setOqq(oqq);
		subUserComment.setComment(comment);
		subUserComment.setTime(time);
		return subUserComment;
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
