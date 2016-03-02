package com.mingchao.snsspider.qq.main;

import com.mingchao.snsspider.logging.LogFactory;
import com.mingchao.snsspider.qq.common.Paraments;
import com.mingchao.snsspider.qq.common.ParamentsProvider;
import com.mingchao.snsspider.qq.task.schedule.SchaduleDispatchFollowTask;
import com.mingchao.snsspider.qq.task.schedule.SchaduleVisitFollowTask;
import com.mingchao.snsspider.task.RunnableTask;
import com.mingchao.snsspider.task.Task;

public class SpiderMain {
	Paraments para =ParamentsProvider.getInstance();
	Task schaduleTask1 = new SchaduleDispatchFollowTask();
	Task schaduleTask2 = new SchaduleVisitFollowTask();

	public static void main(String[] args) {
		new SpiderMain().spider();
	}

	public void spider() {
		try {
			Thread schaduleThread1 = new Thread(new RunnableTask(schaduleTask1));
			Thread schaduleThread2 = new Thread(new RunnableTask(schaduleTask2));
			schaduleThread1.start();
			schaduleThread2.start();
		} catch (Exception e) {
			LogFactory.getLog(this.getClass().getName()).error(e, e);
		}
	}
	
	// TODO 关闭爬虫
	public void close(){
		//关闭调度线程
		//schaduleTask1.close();
		//schaduleTask2.close();
		
		//关闭线程池，关闭webDriver
		//resource.close(); 
	}
}
