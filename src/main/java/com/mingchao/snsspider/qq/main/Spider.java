package com.mingchao.snsspider.qq.main;

import com.mingchao.snsspider.executor.CloseableThread;

import java.util.Timer;

import org.apache.commons.logging.LogFactory;

import com.mingchao.snsspider.qq.common.Paraments;
import com.mingchao.snsspider.qq.provider.ResourceProvider;
import com.mingchao.snsspider.qq.resource.Resource;
import com.mingchao.snsspider.qq.task.schedule.SchaduleDispatchFollowTask;
import com.mingchao.snsspider.qq.task.schedule.SchaduleVisitFollowTask;
import com.mingchao.snsspider.qq.task.timer.DumpBloomTask;
import com.mingchao.snsspider.task.CloseRunnableTask;
import com.mingchao.snsspider.util.Crawlable;

public class Spider implements Crawlable {
	private Timer timer;  
	private CloseableThread dispatchFollowThread;
	private CloseableThread visitFollowThread;
	private Resource resource = ResourceProvider.INSTANCE.getResource();
	private Paraments para = ResourceProvider.INSTANCE.getParaments();

	private NotifyStopAble notifyStopAble;

	public Spider(NotifyStopAble notifyStopAble) {
		this.notifyStopAble = notifyStopAble;
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
			timer = new Timer();
			timer.schedule(new DumpBloomTask(), para.getDumpPeriod(), para.getDumpPeriod());//定时dump 一次bloom filter
		} catch (Exception e) {
			LogFactory.getLog(this.getClass().getName()).error(e, e);
		}
	}

	@Override
	public void close() {
		if(timer!=null){
			timer.cancel();
		}
		// 关闭调度线程
		dispatchFollowThread.close();
		visitFollowThread.close();

		// 关闭线程池，关闭webDriver，关闭队列， 关闭Hibernate
		resource.close();

		// 通知关闭主线程，程序结束
		notifyStopAble.notifyStop();
	}

}
