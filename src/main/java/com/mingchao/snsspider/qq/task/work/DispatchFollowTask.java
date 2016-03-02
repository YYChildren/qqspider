package com.mingchao.snsspider.qq.task.work;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.task.SingleTask;

public class DispatchFollowTask  extends SingleTask {

	private ScheduleUserKey suk;

	public DispatchFollowTask(ScheduleUserKey userKey) {
		this.suk = userKey;
	}

	@Override
	public void execute() throws ClientProtocolException, IOException {
		schaduleRela();
	}

	private void schaduleRela() {
		scheduleFollow.schadule(new ScheduleFollowKey(suk.getQq(), 1));
	}
}
