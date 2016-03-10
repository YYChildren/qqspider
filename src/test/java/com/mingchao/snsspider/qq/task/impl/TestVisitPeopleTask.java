package com.mingchao.snsspider.qq.task.impl;

import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.resource.Resource;
import com.mingchao.snsspider.qq.task.work.VisitPeopleTask;

public class TestVisitPeopleTask {
	public static void main(String[] args) {
		Resource resource = Resource.getInstance();
		ScheduleUserKey suk = new ScheduleUserKey();
		suk.setQq(1946231687L);
		try {
			new VisitPeopleTask(suk).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			resource.close();
		}
	}
}
