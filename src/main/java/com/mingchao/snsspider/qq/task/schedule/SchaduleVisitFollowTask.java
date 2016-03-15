package com.mingchao.snsspider.qq.task.schedule;

import java.io.IOException;

import com.mingchao.snsspider.exception.NPInterruptedException;
import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.task.BaseCloseableTask;
import com.mingchao.snsspider.qq.task.work.VisitFollowTask;
import com.mingchao.snsspider.schedule.Schedule;
import com.mingchao.snsspider.util.TimeUtils;

public class SchaduleVisitFollowTask extends BaseCloseableTask {

	private boolean run = true;
	private Schedule<ScheduleFollowKey> scheduleFollow = resource
			.getScheduleFollow();

	@Override
	public void execute() throws IOException {
		// 需要登录
		while (run) {
			try {
				ScheduleFollowKey srk = scheduleFollow.fetch();
				if (srk != null) {
					resource.getTaskExcutor().execute(new VisitFollowTask(srk));
				}else{
					TimeUtils.sleep();
				}
			} catch (InterruptedException | NPInterruptedException e) {
				log.debug(e, e);
				break;
			}
		}
		log.warn(Thread.currentThread() + " Stoped");
	}

	@Override
	public void close() {
		run = false;
	}
}
