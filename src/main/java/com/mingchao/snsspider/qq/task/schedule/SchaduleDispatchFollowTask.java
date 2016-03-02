package com.mingchao.snsspider.qq.task.schedule;

import java.io.IOException;

import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.task.SingleTask;
import com.mingchao.snsspider.qq.task.work.DispatchFollowTask;
import com.mingchao.snsspider.task.RunnableTask;

public class SchaduleDispatchFollowTask extends SingleTask {
	
	public static RunnableTask newRunnableTask(){
		return new RunnableTask(new SchaduleDispatchFollowTask());
	}
	
	@Override
	public void execute() throws IOException {
		// 需要登录
		while (true) {
			ScheduleUserKey usk = scheduleUser.fetch();
			if (usk != null) {
				resource.getTaskExcutor().execute(new DispatchFollowTask(usk));
			}
		}
	}
}
