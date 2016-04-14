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
import com.mingchao.snsspider.qq.model.ForwardedUserMood;
import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.model.SecondaryUserComment;
import com.mingchao.snsspider.qq.model.UserComment;
import com.mingchao.snsspider.qq.model.UserKey;
import com.mingchao.snsspider.qq.model.UserMood;
import com.mingchao.snsspider.qq.model.UserRelation;
import com.mingchao.snsspider.qq.task.VisitTask;
import com.mingchao.snsspider.schedule.Schedule;
import com.mingchao.snsspider.storage.Storage;
import com.mingchao.snsspider.util.TimeUtils;

public class VisitMoodTask extends VisitTask<String> {
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

	public List<UserMood> getMoods() {
		return moods;
	}

	public Set<Long> getNewQqs() {
		return newQqs;
	}

	@Override
	protected boolean visit(WebDriverWrapper webDriverWrapper) {

		if (tryVisit(modUrl, webDriverWrapper)) {
			return true;
		} else {
			reschaduleRela();
			return false;
		}
	}

	// 未登录状态的处理
	@Override
	protected String handleNoLogin(boolean hadTryLogin, WebDriverWrapper webDriverWrapper) {
		// 如果已经尝试登录过一次
		if (hadTryLogin) {
			reschaduleRela();
			return null;
		}
		try {
			while (true) {
				if (login(webDriverWrapper)) {// 如果登录成功
					break;
				}
				TimeUtils.sleep();
			}
		} catch (InterruptedException e) {
			throw new NPInterruptedException(e);
		}
		return doVisit(true, webDriverWrapper);
	}

	// 无访问权限的处理
	@Override
	protected String handleNoProvilege(WebDriverWrapper webDriverWrapper) {
		// 设置为无权访问
		if (pageNum.equals(1)) {
			UserKey uk = new UserKey();
			uk.setQq(qq);
			uk.setVisitable(false);
			storage.insertDuplicate(uk);
		}
		return null;
	}

	// 有访问权限的处理
	@Override
	protected String handleProvilege(WebDriverWrapper webDriverWrapper) {
		// 设置为有权访问
		if (pageNum.equals(1)) {
			UserKey uk = new UserKey();
			uk.setQq(qq);
			uk.setVisitable(true);
			storage.insertDuplicate(uk);
		}
		String pageSource = null;
		RemoteWebDriver webDriver = webDriverWrapper.getWebDriver();
		webDriver.switchTo().frame(webDriver.findElement(By.xpath("//iframe[@class='app_canvas_frame']")));
		boolean isOk = false;
		int tryTime = 2;
		do {
			try {
				tryTime--;
				// 如果当前页不是1，current标签不是当前页
				if (pageNum != 1 && Integer
						.parseInt(webDriver.findElement(By.xpath("//div[@id='pager']//span[@class='current']/span"))
								.getText()) != pageNum) {
					WebElement target = webDriver.findElement(By.xpath("//input[starts-with(@id,'pager_go')]"));
					webDriver.executeScript("arguments[0].value='" + String.valueOf(pageNum) + "';", target);
					webDriver.executeScript("arguments[0].click();",
							webDriver.findElement(By.xpath("//button[starts-with(@id,'pager_gobtn')]")));
				}

				if (pageNum != 1 && Integer
						.parseInt(webDriver.findElement(By.xpath("//div[@id='pager']//span[@class='current']/span"))
								.getText()) != pageNum) {
					try {
						TimeUtils.sleep(100);
					} catch (InterruptedException e) {
						throw new NPInterruptedException(e);
					}
					continue;
				}

				// 相当于等待页面加载完成
				webDriver.findElements(By.xpath("//li[@class='feed']"));
				// webDriver.findElements(By.xpath("//div[@class='comments_content']/a[@class='nickname']"));
				pageSource = webDriver.getPageSource();

				// 判断是否有下一页，如果有，则调度
				boolean hasNext = true;
				WebElement pager = webDriver.findElement(By.xpath("//div[@id='pager']"));
				if (pager.getAttribute("style").equals("display: none;")) {// 没有显示，肯定是没有下一页
					hasNext = false;
				} else {
					for (WebElement element : webDriver
							.findElements(By.xpath("//div[@id='pager']//p[@class='mod_pagenav_main']"))) {
						// 下一页被禁用，说明没有下一页
						if (element.getAttribute("class") == "mod_pagenav_disable"
								&& element.findElement(By.xpath("/span")).getText().contains("下一页")) {
							hasNext = false;
							break;
						}
					}
				}
				if (hasNext) {
					schaduleNextRela();
				}

				isOk = true;
			} catch (TimeoutException | StaleElementReferenceException e) {
				if (tryTime == 0) {
					reschaduleRela();
				}
				try {
					TimeUtils.sleep(100);
				} catch (InterruptedException e1) {
					throw new NPInterruptedException(e1);
				}
				continue;
			} catch (NoSuchElementException e) {
				break;
			}
		} while (!isOk && tryTime > 0);
		webDriver.switchTo().defaultContent();
		return pageSource;
	}

	@Override
	protected String handleException(WebDriverException e, WebDriverWrapper webDriverWrapper) {
		reschaduleRela();
		throw e;
	}

	@Override
	protected String handleException(NPInterruptedException e, WebDriverWrapper webDriverWrapper) {
		reschaduleRela();
		throw e;
	}

	@Override
	protected void handleResult(String pageSource) {
		if (pageSource == null || pageSource.isEmpty()) {
			return;
		}
		parse(pageSource);
		if (newQqs != null && !newQqs.isEmpty()) {
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
			schadule(suks); // 调度新QQ
			storage.insertIgnore(urs); // 持久化关系链
		}
		if (moods != null && !moods.isEmpty()) {
			storageMogo.insertDuplicate(moods); // 持久化说说
		}
		log.info("get follow, qq: " + qq + ", page: " + pageNum);
	}

	public void parse(String pageSource) {
		Document doc = Jsoup.parse(pageSource);
		moods = new ArrayList<UserMood>();
		newQqs = new HashSet<Long>();
		getUserMoods(doc);
		moods = moods.isEmpty() ? null : moods;
		newQqs.remove(qq);// 删除自身
		newQqs = newQqs.isEmpty() ? null : newQqs;
		System.out.println();
	}

	private void getUserMoods(Document doc) {
		Long currenttime = System.currentTimeMillis();
		Element msgList = doc.getElementById("msgList");// ol 标签
		for (Element element : msgList.children()) {// li标签
			UserMood userMood = getUserMood(currenttime, element);
			moods.add(userMood);
		}
	}

	private UserMood getUserMood(Long currenttime, Element element) {
		UserMood userMood = new UserMood();
		// Long qq;
		// Long qqstr;
		String dataid = element.attr("data-tid");
		String mood = null;
		String createtime = null;
		// Long currenttime;

		// like
		Integer likecount = null;
		List<Long> likeqqs = null;

		// comment
		Integer commentcount = null;
		List<UserComment> comments = null;

		// forward
		Integer forwardcount = null;
		Boolean isforward = null;// 是不是转发，如果是转发，所有点赞归原说说
		ForwardedUserMood forwardmood = null;// 转发的内容
		Elements eles = element.select("div[class=box bgr3]");
		if (!eles.isEmpty()) {
			for (Element feedchild : eles.get(0).children()) {
				switch (feedchild.attr("class")) {
				case "bd":
					Elements moodElements = feedchild.getElementsByTag("pre");
					mood = moodElements.isEmpty() ? null : moodElements.get(0).text();
					break;
				case "md": // TODO 可能有图片
					break;
				case "md rt_content": // 转发
					isforward = true;
					forwardmood = getForwardMood(feedchild);
					break;
				case "ft": // 时间 -- 数量（赞 -- 评论 -- 转发）
					Elements infoElements = feedchild.getElementsByClass("info");
					Elements timeElements = infoElements.isEmpty() ? null : infoElements.select("a[href]");
					createtime = (timeElements == null || timeElements.isEmpty()) ? null : timeElements.text();
					createtime = ("".equals(createtime) || createtime == null) ? null : createtime;

					Elements opElements = feedchild.getElementsByClass("op");
					// log.info(opElements);
					Elements likeElements = opElements.select("a[_origtemp=赞{cnt}]");
					String likeText = likeElements.isEmpty() ? null
							: likeElements.get(0).text().replaceAll("[^\\d]*", "");
					likecount = ("".equals(likeText) || likeText == null) ? null : Integer.parseInt(likeText);

					Elements commentElements = opElements.select("a[class=c_tx comment_btn]");
					String commentText = commentElements.isEmpty() ? null
							: commentElements.text().replaceAll("[^\\d]*", "");
					commentcount = ("".equals(commentText) || commentText == null) ? null
							: Integer.parseInt(commentText);

					Elements forwardElements = opElements.select("a[class=c_tx forward_btn]");
					String forwardText = forwardElements.isEmpty() ? null
							: forwardElements.text().replaceAll("[^\\d]*", "");
					forwardcount = ("".equals(forwardText) || forwardText == null) ? null
							: Integer.parseInt(forwardText);
					break;
				case "box_extra bor3 ": // 点赞、评论具体情况
					Elements feedLikeEles = feedchild.getElementsByClass("feed_like");
					if (!feedLikeEles.isEmpty()) {
						likeqqs = new ArrayList<Long>();
						for (Element element2 : feedLikeEles.select("a[href]")) {
							String href = element2.attr("href");
							try {
								Long likeqq = Long.parseLong(getAccountStr(href));
								likeqqs.add(likeqq);
								newQqs.add(likeqq); // ADD new qq
							} catch (NumberFormatException e) {
							}
						}
						likeqqs = likeqqs.isEmpty() ? null : likeqqs;
					}
					comments = getUserComments(userMood, feedchild);
					break;
				}
			}
		}

		userMood.setQq(qq);
		userMood.setDataid(dataid);
		userMood.setMood(mood);
		userMood.setCreatetime(createtime);
		userMood.setCurrenttime(currenttime);
		if (isforward != null && isforward.equals(true)) {
			forwardmood.setCurrenttime(currenttime);
			forwardmood.setLikecount(likecount);
		} else {
			userMood.setLikecount(likecount);
		}
		userMood.setLikeqqs(likeqqs);
		userMood.setCommentcount(commentcount);
		userMood.setComments(comments);
		userMood.setForwardcount(forwardcount);
		userMood.setIsforward(isforward);
		userMood.setForwardmood(forwardmood);
		return userMood;
	}

	private ForwardedUserMood getForwardMood(Element element) {
		ForwardedUserMood forwardUserMood = new ForwardedUserMood();
		Long fqq = null;
		String fqqstr = null;
		String dataid = null;
		String mood = null;
		String createtime = null;
		// Long currenttime;
		// Integer likecount;
		// List<Long> likeqqs;
		Integer commentcount = null;
		// List<UserComment> comments;
		Integer forwardcount = null;
		for (Element child : element.child(0).children()) {
			switch (child.attr("class")) {
			case "bd":
				String accountStr = child.getElementsByTag("a").get(0).attr("data-uin");
				try {
					fqq = Long.parseLong(accountStr);
				} catch (NumberFormatException e) {
					fqqstr = accountStr;
				}
				mood = child.getElementsByTag("pre").text();
				mood = mood.isEmpty() ? null : mood;
				break;
			case "md": // TODO 可能有照片
				break;
			case "ft":
				Elements infoElements = child.getElementsByClass("info");
				Elements timeElements = infoElements.isEmpty() ? null : infoElements.select("a[href]");
				if (timeElements != null && !timeElements.isEmpty()) {
					Element timeElement = timeElements.get(0);
					String infoUrl = timeElement.attr("href");
					dataid = ("".equals(infoUrl) || infoUrl == null) ? null
							: infoUrl.replaceFirst(".*mood/", "").replaceAll("\\..*", "");
					createtime = timeElement.text();
					createtime = ("".equals(createtime) || createtime == null) ? null : createtime;
				}

				Elements opElements = child.getElementsByClass("op");

				Elements commentElements = opElements.select("a[class=c_tx rt_comment_btn]");
				String commentText = commentElements.isEmpty() ? null
						: commentElements.text().replaceAll("[^\\d]*", "");
				commentcount = ("".equals(commentText) || commentText == null) ? null : Integer.parseInt(commentText);

				Elements forwardElements = opElements.select("a[class=c_tx rt_forward_btn]");
				String forwardText = forwardElements.isEmpty() ? null
						: forwardElements.text().replaceAll("[^\\d]*", "");
				forwardcount = ("".equals(forwardText) || forwardText == null) ? null : Integer.parseInt(forwardText);
				break;
			}
		}
		forwardUserMood.setQq(fqq);
		forwardUserMood.setQqstr(fqqstr);
		forwardUserMood.setDataid(dataid);
		forwardUserMood.setMood(mood);
		forwardUserMood.setCreatetime(createtime);
		forwardUserMood.setCommentcount(commentcount);
		forwardUserMood.setForwardcount(forwardcount);
		return forwardUserMood;
	}

	private List<UserComment> getUserComments(UserMood userMood, Element element) {
		Elements commentEles = element.getElementsByClass("comments_list");
		if (commentEles.isEmpty()) {
			return null;
		}
		Element commentUl = null;
		for (Element element2 : commentEles.get(0).children()) {
			if (element2.tagName().equals("ul")) {
				commentUl = element2;
				break;
			}
		}
		if (commentUl == null) {
			return null;
		}
		List<UserComment> comments = new ArrayList<UserComment>();
		for (Element element2 : commentUl.children()) {
			UserComment userComment = null;
			List<SecondaryUserComment> secondarycomments = new ArrayList<SecondaryUserComment>();
			int i = 0;
			for (Element element3 : element2.getElementsByClass("comments_content")) {
				if (i++ == 0) {
					userComment = getUserComment(userMood, element3);
				} else {
					SecondaryUserComment secondarycomment = getSecondaryUserComment(userComment, element3);
					if (secondarycomment != null) {
						secondarycomments.add(secondarycomment);
					}
				}
			}
			secondarycomments = secondarycomments.isEmpty() ? null : secondarycomments;
			if (userComment != null) {
				if (secondarycomments != null) {
					userComment.setSecondarycomments(secondarycomments);
				}
				comments.add(userComment);
			}
		}
		comments = comments.isEmpty() ? null : comments;
		return comments;
	}

	private UserComment getUserComment(UserMood userMood, Element element) {
		UserComment userComment = new UserComment();
		Long qq = null; // 评论qq
		String qqstr = null;// 可能是朋友网，或者是经过加密的qq，与上面的qq二选一
		String comment = null;
		String time = null;
		// List<SecondaryUserComment> secondarycomments = null;
		// UserMood usermood = null;

		for (Element timeElement : element.select("span[class=c_tx3 ui_mr10]")) {
			if ("".equals(timeElement.attr("style"))) {
				time = timeElement.text();
				break;
			}
		} // 获取时间
		for (Element opElement : element.getElementsByClass("comments_op")) {
			opElement.remove();
		} // 删除无用标签

		Elements nickNameElements = element.select("a.nickname");
		String qqHref = nickNameElements.isEmpty() ? null : nickNameElements.get(0).attr("href");
		String accountStr = getAccountStr(qqHref);
		try {
			qq = Long.parseLong(accountStr);
			newQqs.add(qq); // ADD new qq
		} catch (NumberFormatException e) {
			qqstr = accountStr;
		}
		comment = element.text();

		userComment.setQq(qq);
		userComment.setQqstr(qqstr);
		userComment.setComment(comment);
		userComment.setTime(time);
		userComment.setUsermood(userMood);
		return userComment;
	}

	private SecondaryUserComment getSecondaryUserComment(UserComment userComment, Element element) {
		SecondaryUserComment subUserComment = new SecondaryUserComment();
		Long qq = null; // 评论qq
		String qqstr = null;// 可能是朋友网，或者是经过加密的qq，与上面的qq二选一
		Long oqq = null; // 被评论qq
		String oqqstr = null;// 可能是朋友网，或者是经过加密的qq，与上面的oqq二选一
		String comment = null; // 评论
		String time = null; // 评论时间
		// UserComment usercomment;

		for (Element timeElement : element.select("span[class=c_tx3 ui_mr10]")) {
			if ("".equals(timeElement.attr("style"))) {
				time = timeElement.text();
				break;
			}
		} // 获取时间
		for (Element opElement : element.getElementsByClass("comments_op")) {
			opElement.remove();
		} // 删除无用标签

		Elements nickNameElements = element.select("a.nickname");
		if (nickNameElements.size() == 2) {
			String qqHref = nickNameElements.get(0).attr("href");
			String qqAccountStr = getAccountStr(qqHref);
			try {
				qq = Long.parseLong(qqAccountStr);
				newQqs.add(qq); // ADD new qq
			} catch (NumberFormatException e) {
				qqstr = qqAccountStr;
			}
			String oqqHref = nickNameElements.get(1).attr("href");
			String oqqAccountStr = getAccountStr(oqqHref);
			try {
				oqq = Long.parseLong(oqqAccountStr);
				newQqs.add(oqq); // ADD new qq
			} catch (NumberFormatException e) {
				oqqstr = oqqAccountStr;
			}
		}
		comment = element.text();

		subUserComment.setQq(qq);
		subUserComment.setQqstr(qqstr);
		subUserComment.setOqq(oqq);
		subUserComment.setOqqstr(oqqstr);
		subUserComment.setComment(comment);
		subUserComment.setTime(time);
		subUserComment.setUsercomment(userComment);
		return subUserComment;
	}

	public static String getAccountStr(String href) {
		if (href == null || href.isEmpty()) {
			return null;
		}
		if (href.contains("user.qzone.qq.com") || href.contains("pengyou.com")) {
			return href.replaceFirst("^.*com/", "").replaceFirst("^.*u=", "").replaceFirst("/.*$", "");
		} else {
			return null;
		}

	}

	// TODO
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
