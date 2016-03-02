package com.mingchao.snsspider.qq.common;


public class ParamentsProvider {
	private static Paraments instance;

	public static Paraments getInstance() {
		if (instance == null) {
			initTaskExcutor();
		}
		return instance;
	}

	private static synchronized void initTaskExcutor() {
		if (instance == null) {
			instance = new BaseParaments();
		}
	}
}
