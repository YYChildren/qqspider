package com.mingchao.snsspider.qq.main;

import com.mingchao.snsspider.executor.CloseableThread;
import com.mingchao.snsspider.logging.LogFactory;
import com.mingchao.snsspider.qq.resource.Resource;
import com.mingchao.snsspider.qq.task.schedule.SchaduleDispatchFollowTask;
import com.mingchao.snsspider.qq.task.schedule.SchaduleVisitFollowTask;
import com.mingchao.snsspider.task.CloseRunnableTask;
import com.mingchao.snsspider.util.Crawlable;

public class Spider implements Crawlable {
	private CloseableThread dispatchFollowThread;
	private CloseableThread visitFollowThread;
	private Resource resource = Resource.getInstance();

	private Main mainObject;

	public Spider(Main mainObject) {
		this.mainObject = mainObject;
	}

	public void crawl() {
		try {
			SchaduleDispatchFollowTask schaduleDispatchFollowTask = new SchaduleDispatchFollowTask();
			SchaduleVisitFollowTask scheduleVisitFollowTask = new SchaduleVisitFollowTask();
			
			dispatchFollowThread = new CloseableThread(
					new CloseRunnableTask(schaduleDispatchFollowTask),
					SchaduleDispatchFollowTask.class.getSimpleName());
			visitFollowThread = new CloseableThread(
					new CloseRunnableTask(scheduleVisitFollowTask),
					SchaduleVisitFollowTask.class.getSimpleName());
			
			dispatchFollowThread.start();
			visitFollowThread.start();
			
		} catch (Exception e) {
			LogFactory.getLog(this.getClass().getName()).error(e, e);
		}
	}

	@Override
	public void close() {
		// 关闭调度线程
		dispatchFollowThread.close();
		visitFollowThread.close();

		// 关闭线程池，关闭webDriver，关闭队列， 关闭Hibernate
		resource.close();

		// 通知关闭主线程，程序结束
		mainObject.notifyStop();
	}

}
