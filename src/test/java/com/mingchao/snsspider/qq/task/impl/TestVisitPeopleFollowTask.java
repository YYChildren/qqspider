package com.mingchao.snsspider.qq.task.impl;

import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.resource.Resource;
import com.mingchao.snsspider.qq.task.work.VisitFollowTask;

public class TestVisitPeopleFollowTask {
	public static void main(String[] args) {
		Resource resource = Resource.getInstance();
		ScheduleFollowKey uk = new ScheduleFollowKey();
		//uk.setQq(31945L);//没发表说说
		//uk.setPageNum(1);
		//uk.setQq(24417L);//只发表一页说说
		//uk.setPageNum(1);
		uk.setQq(1946231687L);//发表多页说说
		uk.setPageNum(5);
		try {
			new VisitFollowTask(uk).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			resource.close();
		}
	}
}
