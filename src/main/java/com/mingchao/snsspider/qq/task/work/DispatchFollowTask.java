package com.mingchao.snsspider.qq.task.work;

import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.task.BaseTaskImpl;
import com.mingchao.snsspider.schedule.Schedule;

public class DispatchFollowTask  extends BaseTaskImpl {

	private ScheduleUserKey suk;
	private Schedule<ScheduleFollowKey> scheduleFollow = resource.getScheduleFollow(); 

	public DispatchFollowTask(ScheduleUserKey userKey) {
		this.suk = userKey;
	}

	@Override
	public void execute(){
		schaduleRela();
	}

	private void schaduleRela() {
		scheduleFollow.schadule(new ScheduleFollowKey(suk.getQq(), 1));
	}
}
