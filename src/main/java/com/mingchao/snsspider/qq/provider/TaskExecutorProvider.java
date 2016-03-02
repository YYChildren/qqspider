package com.mingchao.snsspider.qq.provider;

import com.mingchao.snsspider.manager.BaseTaskExcutor;
import com.mingchao.snsspider.manager.TaskExcutor;

public class TaskExecutorProvider {
	public static  TaskExcutor newInstance() {
		return new BaseTaskExcutor();
	}
}
