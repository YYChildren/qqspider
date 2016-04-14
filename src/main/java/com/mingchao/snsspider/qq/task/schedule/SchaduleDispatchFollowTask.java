package com.mingchao.snsspider.qq.task.schedule;

import com.mingchao.snsspider.exception.NPInterruptedException;
import com.mingchao.snsspider.executor.TaskExcutor;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.task.BaseCloseableTask;
import com.mingchao.snsspider.qq.task.work.DispatchFollowTask;
import com.mingchao.snsspider.schedule.Schedule;
import com.mingchao.snsspider.util.TimeUtils;

public class SchaduleDispatchFollowTask extends BaseCloseableTask {

	private boolean run = true;
	private Schedule<ScheduleUserKey> scheduleUser = resource.getScheduleUser();
	private TaskExcutor executor = resource.getTaskExecutor();
	

	@Override
	public void execute(){
		while (run) {
			try {
				ScheduleUserKey usk = scheduleUser.fetch();
				if (usk != null) {
					executor.execute(new DispatchFollowTask(usk));
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
