package com.mingchao.snsspider.qq.provider;

import com.mingchao.snsspider.model.QueueStatusImpl;
import com.mingchao.snsspider.qq.common.Paraments;
import com.mingchao.snsspider.qq.common.ParamentsProvider;
import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.schedule.Schedule;
import com.mingchao.snsspider.schedule.ScheduleDist;

public class ScheduleProvider {

	private static Paraments paras = ParamentsProvider.getInstance();
	private static Schedule<ScheduleUserKey> scheduleUser;
	private static Schedule<ScheduleFollowKey> scheduleFollow;

	public static Schedule<ScheduleFollowKey> getScheduleFollow() {
		if (scheduleFollow == null) {
			initScheduleFollow();
		}
		return scheduleFollow;
	}

	private static synchronized void initScheduleFollow() {
		if (scheduleFollow == null) {
			scheduleFollow = new ScheduleDist<ScheduleFollowKey>(
					ScheduleFollowKey.class, QueueStatusImpl.class,
					ScheduleFollowKey.getFunnel(),
					paras.getBloomExected(), paras.getBloomFpp(),
					paras.getBloomPath());
		}
	}

	public static Schedule<ScheduleUserKey> getScheduleUser() {
		if (scheduleUser == null) {
			initScheduleUser();
		}
		return scheduleUser;
	}

	private static synchronized void initScheduleUser() {
		if (scheduleUser == null) {
			scheduleUser = new ScheduleDist<ScheduleUserKey>(
					ScheduleUserKey.class, QueueStatusImpl.class,
					ScheduleUserKey.getFunnel(),
					paras.getBloomExected(), paras.getBloomFpp(),
					paras.getBloomPath());
		}
	}
}
