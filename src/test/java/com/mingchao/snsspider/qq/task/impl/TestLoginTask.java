package com.mingchao.snsspider.qq.task.impl;

import com.mingchao.snsspider.qq.task.BaseTaskImpl;
import com.mingchao.snsspider.qq.task.work.LoginTask;

public class TestLoginTask extends BaseTaskImpl{
	
	public static void main(String[] args) {
		LoginTask.newRunnableTask().run();
	}
}
