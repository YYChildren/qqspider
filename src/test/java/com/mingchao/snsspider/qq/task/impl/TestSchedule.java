package com.mingchao.snsspider.qq.task.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.provider.ScheduleProvider;
import com.mingchao.snsspider.schedule.Schedule;

public class TestSchedule {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Schedule<ScheduleUserKey> scheduleUser;
		Schedule<ScheduleFollowKey> scheduleFollow;
		scheduleUser = ScheduleProvider.getScheduleUser();
		scheduleFollow = ScheduleProvider.getScheduleFollow();
		ScheduleUserKey user = new ScheduleUserKey();
		user.setQq(1946231687L);
		scheduleUser.schadule(user);
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		
	}

}
