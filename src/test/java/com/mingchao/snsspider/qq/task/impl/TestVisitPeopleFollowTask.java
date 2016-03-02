package com.mingchao.snsspider.qq.task.impl;

import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.resource.Resource;
import com.mingchao.snsspider.qq.task.work.LoginTask;
import com.mingchao.snsspider.qq.task.work.VisitFollowTask;

public class TestVisitPeopleFollowTask {
	public static void main(String[] args) {
		ScheduleFollowKey uk = new ScheduleFollowKey();
		uk.setQq(1946231687L);
		uk.setPageNum(2);
		try {
			new LoginTask().execute();
			new VisitFollowTask(uk).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			Resource.getInstance().close();
		}
	}
}
