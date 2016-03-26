package com.mingchao.snsspider.qq.task;

import com.mingchao.snsspider.qq.provider.ResourceProvider;
import com.mingchao.snsspider.qq.resource.Resource;
import com.mingchao.snsspider.task.BaseTask;

public class BaseTaskImpl extends BaseTask {
	protected Resource resource = ResourceProvider.INSTANCE.getResource();
}
