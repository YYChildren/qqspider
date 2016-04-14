package com.mingchao.snsspider.qq.task.schedule;

import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.task.work.DispatchFollowTask;

public class TestSchaduleFollowTask {

	public static void main(String[] args) {
		ScheduleUserKey suk = new ScheduleUserKey();
		suk.setQq(1946231687L);
		new DispatchFollowTask(suk).execute();
	}

}
