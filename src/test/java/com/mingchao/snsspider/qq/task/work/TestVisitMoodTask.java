package com.mingchao.snsspider.qq.task.work;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.codec.Charsets;

import com.mingchao.snsspider.qq.model.ScheduleFollowKey;
import com.mingchao.snsspider.qq.provider.ResourceProvider;

public class TestVisitMoodTask {

	public static void main(String[] args) throws IOException {
		try {
			VisitMoodTask visitMoodTask;
			ScheduleFollowKey scheduleFollowKey = new ScheduleFollowKey();
			scheduleFollowKey.setQq(2364168L);
			scheduleFollowKey.setPageNum(1);
			visitMoodTask = new VisitMoodTask(scheduleFollowKey);
			URL url = ClassLoader.getSystemResource("testmood.html");
			InputStream is = url.openStream();
			StringBuilder sb = new StringBuilder();
			int len = 0;
			byte [] bytes = new byte[8192];
			while((len = is.read(bytes)) != -1){
				if(len > 0 ){
					sb.append(new String(bytes,0,len,Charsets.UTF_8));
				}
			}
			is.close();
			visitMoodTask.parse(sb.toString());
			System.out.println(visitMoodTask.getNewQqs());
			ResourceProvider.INSTANCE.getMongoStorage().insertDuplicate(visitMoodTask.getMoods());  //持久化说说
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ResourceProvider.INSTANCE.getResource().close();
		}
		String str1 = VisitMoodTask.getAccountStr("http://user.qzone.qq.com/1946231687/311");
		String str2 =VisitMoodTask.getAccountStr("http://user.qzone.qq.com/asdgfdq45wnkvhgbsdf/311");
		System.out.println(str1);
		System.out.println(str2);
	}
}
