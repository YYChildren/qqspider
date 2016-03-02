package com.mingchao.snsspider.qq.task.schedule;

import java.io.IOException;

import com.mingchao.snsspider.http.CookieStorePool;
import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.task.SingleTask;
import com.mingchao.snsspider.qq.task.work.LoginTask;
import com.mingchao.snsspider.qq.task.work.VisitFollowTask;
import com.mingchao.snsspider.task.RunnableTask;

public class SchaduleVisitFollowTask extends SingleTask {
	
	private CookieStorePool pool = resource.getCookieStorePool();

	public static RunnableTask newRunnableTask(){
		return new RunnableTask(new SchaduleVisitFollowTask());
	}
	
	@Override
	public void execute() throws IOException {
		// 需要登录
		while (true) {
			// 登录任务，单线程执行
			if(!pool.isFull()){
				LoginTask.newRunnableTask().run();
			}
			
			ScheduleFollowKey srk = scheduleFollow.fetch();
			if (srk != null) {
				resource.getTaskExcutor().execute(new VisitFollowTask(srk));
			}
		}
	}
}
