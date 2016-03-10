package com.mingchao.snsspider.qq.provider;

import com.mingchao.snsspider.qq.common.Paraments;
import com.mingchao.snsspider.qq.common.ParamentsProvider;
import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.schedule.Schedule;
import com.mingchao.snsspider.schedule.ScheduleMySQL;

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
			scheduleFollow = new ScheduleMySQL<ScheduleFollowKey>(
					ScheduleFollowKey.class, ScheduleFollowKey.getFunnel(),
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
			scheduleUser = new ScheduleMySQL<ScheduleUserKey>(
					ScheduleUserKey.class, ScheduleUserKey.getFunnel(),
					paras.getBloomExected(), paras.getBloomFpp(),
					paras.getBloomPath());
		}
	}
}
