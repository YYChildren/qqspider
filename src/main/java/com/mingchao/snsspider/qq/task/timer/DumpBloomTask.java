package com.mingchao.snsspider.qq.task.timer;

import java.util.TimerTask;

import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.provider.ResourceProvider;
import com.mingchao.snsspider.qq.resource.Resource;
import com.mingchao.snsspider.schedule.Schedule;
import com.mingchao.snsspider.schedule.ScheduleAdaptor;

public class DumpBloomTask extends TimerTask{
	private Resource resource = ResourceProvider.INSTANCE.getResource();
	private Schedule<ScheduleFollowKey> scheduleFollow = resource.getScheduleFollow(); 
	private Schedule<ScheduleUserKey> scheduleUser = resource.getScheduleUser();
	
	@Override
	public void run() {
		((ScheduleAdaptor<ScheduleFollowKey>)scheduleFollow).dumpFilter();
		((ScheduleAdaptor<ScheduleUserKey>)scheduleUser).dumpFilter();
	}
}
