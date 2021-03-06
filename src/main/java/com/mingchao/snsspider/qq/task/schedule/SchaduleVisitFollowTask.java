package com.mingchao.snsspider.qq.task.schedule;

import com.mingchao.snsspider.exception.NPInterruptedException;
import com.mingchao.snsspider.executor.TaskExcutor;
import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.task.BaseCloseableTask;
import com.mingchao.snsspider.qq.task.work.VisitMoodTask;
import com.mingchao.snsspider.schedule.Schedule;
import com.mingchao.snsspider.util.TimeUtils;

public class SchaduleVisitFollowTask extends BaseCloseableTask {

	private boolean run = true;
	private Schedule<ScheduleFollowKey> scheduleFollow = resource
			.getScheduleFollow();
	private TaskExcutor executor = resource.getTaskExecutor();

	@Override
	public void execute(){
		// 需要登录
		while (run) {
			try {
				ScheduleFollowKey srk = scheduleFollow.fetch();
				if (srk != null) {
					executor.execute(new VisitMoodTask(srk));
				}else{
					TimeUtils.sleep();
				}
			} catch (InterruptedException | NPInterruptedException e) {
				log.debug(e, e);
				break;
			} catch(Exception e){
				log.warn(e, e);
			}
		}
		log.warn(Thread.currentThread() + " Stoped");
	}

	@Override
	public void close() {
		run = false;
	}
}
