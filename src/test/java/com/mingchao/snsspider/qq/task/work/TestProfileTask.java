package com.mingchao.snsspider.qq.task.work;

import com.mingchao.snsspider.qq.model.ScheduleUserKey;
import com.mingchao.snsspider.qq.provider.ResourceProvider;
import com.mingchao.snsspider.qq.resource.Resource;
import com.mingchao.snsspider.qq.task.work.VisitProfileTask;

public class TestProfileTask {
	public static void main(String[] args) {
		Resource resource = ResourceProvider.INSTANCE.getResource();
		ScheduleUserKey suk = new ScheduleUserKey();
		suk.setQq(1286406997L);
		try {
			new VisitProfileTask(suk).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			resource.close();
		}
	}
}
