package com.mingchao.snsspider.qq.task.impl;

import java.io.IOException;

import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.task.work.DispatchFollowTask;


public class TestSchaduleFollowTask {

	public static void main(String[] args) {
		ScheduleUserKey suk = new ScheduleUserKey();
		suk.setQq(1946231687L);
		try {
			new DispatchFollowTask(suk ).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
