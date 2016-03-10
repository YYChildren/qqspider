package com.mingchao.snsspider.qq.provider;

import com.mingchao.snsspider.executor.BaseTaskExcutor;
import com.mingchao.snsspider.executor.TaskExcutor;
import com.mingchao.snsspider.qq.common.ParamentsProvider;

public class TaskExecutorProvider {
	public static TaskExcutor newInstance() {
		return new BaseTaskExcutor(ParamentsProvider.getInstance()
				.getExecutorPoolSize());
	}
}
