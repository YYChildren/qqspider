package com.mingchao.snsspider.qq.task;

import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.provider.ScheduleProvider;
import com.mingchao.snsspider.qq.resource.Resource;
import com.mingchao.snsspider.schedule.Schedule;
import com.mingchao.snsspider.task.BaseTask;

public class BaseTaskImpl extends BaseTask {
	protected Resource resource = Resource.getInstance();
	protected Schedule<ScheduleFollowKey> scheduleFollow = ScheduleProvider.getScheduleFollow();
	protected Schedule<ScheduleUserKey> scheduleUser = ScheduleProvider.getScheduleUser();
}
