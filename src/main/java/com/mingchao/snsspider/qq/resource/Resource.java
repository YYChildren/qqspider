package com.mingchao.snsspider.qq.resource;

import org.springframework.beans.factory.annotation.Required;

import com.mingchao.snsspider.executor.TaskExcutor;
import com.mingchao.snsspider.http.webdriver.WebDriverPool;
import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.schedule.Schedule;
import com.mingchao.snsspider.storage.Storage;
import com.mingchao.snsspider.util.Closeable;

public class Resource implements Closeable{
	private TaskExcutor taskExecutor;
	private WebDriverPool webDriverPool;
	private Storage storage;
	private Storage storageMongo;
	private Schedule<ScheduleFollowKey> scheduleFollow;
	private Schedule<ScheduleUserKey> scheduleUser;

	public Resource() {
	}

	@Override
	public void close() {
		taskExecutor.close();// 关闭线程池
		webDriverPool.close();//关闭webDriver 池
		
		scheduleUser.closing();
		scheduleFollow.closing();
		scheduleUser.dump();
		scheduleFollow.dump();
		scheduleUser.close();//关闭user 调度器
		scheduleFollow.close();//关闭follow 调度器
		
		storage.close();//关闭hibernate
	}

	public TaskExcutor getTaskExecutor() {
		return taskExecutor;
	}

	@Required
	public void setTaskExecutor(TaskExcutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public WebDriverPool getWebDriverPool() {
		return webDriverPool;
	}

	@Required
	public void setWebDriverPool(WebDriverPool webDriverPool) {
		this.webDriverPool = webDriverPool;
	}

	public Storage getStorage() {
		return storage;
	}

	@Required
	public void setStorage(Storage storage) {
		this.storage = storage;
	}
	
	public Storage getStorageMongo() {
		return storageMongo;
	}

	@Required
	public void setStorageMongo(Storage mongoStorage) {
		this.storageMongo = mongoStorage;
	}
	
	public Schedule<ScheduleFollowKey> getScheduleFollow() {
		return scheduleFollow;
	}

	@Required
	public void setScheduleFollow(Schedule<ScheduleFollowKey> scheduleFollow) {
		this.scheduleFollow = scheduleFollow;
	}

	public Schedule<ScheduleUserKey> getScheduleUser() {
		return scheduleUser;
	}
	
	@Required
	public void setScheduleUser(Schedule<ScheduleUserKey> scheduleUser) {
		this.scheduleUser = scheduleUser;
	}

	public String getLoginUrl(){
		return "http://i.qq.com/";
	}
	
	public String getUserProfileUrl(Long qq) {
		return String.format("http://user.qzone.qq.com/%d/profile", qq);
	}

	public String getModUrl(Long qq) {
		return String.format("http://user.qzone.qq.com/%d/mood", qq);
	}
}
