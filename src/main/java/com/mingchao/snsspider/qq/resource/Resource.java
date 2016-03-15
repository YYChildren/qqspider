package com.mingchao.snsspider.qq.resource;

import com.mingchao.snsspider.executor.TaskExcutor;
import com.mingchao.snsspider.http.WebDriverPool;
import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.provider.ScheduleProvider;
import com.mingchao.snsspider.qq.provider.StorageProvider;
import com.mingchao.snsspider.qq.provider.TaskExecutorProvider;
import com.mingchao.snsspider.qq.provider.WebDriverProvider;
import com.mingchao.snsspider.schedule.Schedule;
import com.mingchao.snsspider.storage.Storage;
import com.mingchao.snsspider.util.Closeable;

public class Resource implements Closeable{
	//只用Resource 单例，其他资源是否单例由Resource控制
	private static Resource instance;
	private TaskExcutor taskExcutor = TaskExecutorProvider.newInstance();
	private WebDriverPool webDriverPool = WebDriverProvider.newInstance();
	private Storage storage = StorageProvider.newMySQLStorage();
	private Schedule<ScheduleFollowKey> scheduleFollow = ScheduleProvider.getScheduleFollow();
	private Schedule<ScheduleUserKey> scheduleUser = ScheduleProvider.getScheduleUser();

	private Resource() {
	}

	@Override
	public void close() {
		taskExcutor.close();// 关闭线程池
		webDriverPool.close();//关闭webDriver 池
		
		scheduleUser.closing();
		scheduleFollow.closing();
		scheduleUser.dump();
		scheduleFollow.dump();
		scheduleUser.close();//关闭user 调度器
		scheduleFollow.close();//关闭follow 调度器
		
		storage.close();//关闭hibernate
		instance = null;
	}

	public static Resource getInstance() {
		if (instance == null) {
			init();
		}
		return instance;
	}

	private static synchronized void init() {
		if (instance == null) {
			instance = new Resource();
		}
	}

	public Storage getStorage() {
		return storage;
	}

	public TaskExcutor getTaskExcutor() {
		return taskExcutor;
	}

	public WebDriverPool getWebDriverPool() {
		return webDriverPool;
	}

	public WebDriverPool getPool() {
		return webDriverPool;
	}
	
	public Schedule<ScheduleFollowKey> getScheduleFollow() {
		return scheduleFollow;
	}

	public Schedule<ScheduleUserKey> getScheduleUser() {
		return scheduleUser;
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
