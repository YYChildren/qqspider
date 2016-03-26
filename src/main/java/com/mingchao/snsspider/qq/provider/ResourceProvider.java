package com.mingchao.snsspider.qq.provider;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mingchao.snsspider.executor.TaskExcutor;
import com.mingchao.snsspider.http.webdriver.WebDriverPool;
import com.mingchao.snsspider.qq.common.Paraments;
import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.resource.Resource;
import com.mingchao.snsspider.schedule.Schedule;
import com.mingchao.snsspider.storage.Storage;
import com.mingchao.snsspider.storage.db.StorageDB;

public enum ResourceProvider {
	INSTANCE;
	private ApplicationContext applicationContext;

	private ResourceProvider() {
		applicationContext = new ClassPathXmlApplicationContext("beans.xml");
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public Paraments getParaments(){
		return (Paraments) applicationContext.getBean("paraments");
	}
	
	public StorageDB getMongoStorage(){
		return (StorageDB) applicationContext.getBean("storageMongo");
	}
	
	public Storage getStorageSlave(){
		return (Storage) applicationContext.getBean("storageSlave");
	}
	
	public Storage getStorageMaster(){
		return (Storage) applicationContext.getBean("storageMaster");
	}
	
	@SuppressWarnings("unchecked")
	public Schedule<ScheduleFollowKey> getScheduleFollow(){
		return (Schedule<ScheduleFollowKey>) applicationContext.getBean("scheduleFollow");
	}

	@SuppressWarnings("unchecked")
	public Schedule<ScheduleUserKey> getScheduleUser(){
		return (Schedule<ScheduleUserKey>) applicationContext.getBean("scheduleUser");
	}
	
	public TaskExcutor getTaskExecutor(){
		return (TaskExcutor) applicationContext.getBean("taskExecutor");
	}
	
	public WebDriverPool getWebDriverPool(){
		return (WebDriverPool) applicationContext.getBean("webDriverPool");
	}

	public Resource getResource() {
		return (Resource) applicationContext.getBean("resource");
	}
}
