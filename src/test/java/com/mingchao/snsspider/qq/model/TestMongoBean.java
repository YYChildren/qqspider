package com.mingchao.snsspider.qq.model;


import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mingchao.snsspider.storage.db.StorageDB;

public class TestMongoBean {
	static ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
	
	public static void main(String[] args) {
		Long qq = 1L;
		String dataid= "asdgviubisrg";
		StorageDB storage = (StorageDB) applicationContext.getBean("storageMongo");
		UserMood userMood = new UserMood();
		userMood.setDataid(dataid);
		userMood.setQq(qq);
		userMood.setMood("早上好！");
		userMood.setCreatetime("2014年3月16日");
		userMood.setCurrenttime(31432164321L);
		userMood.setLikecount(2);
		
		List<Long> likeqqs = new ArrayList<Long>();
		{
			likeqqs.add(11L);
			likeqqs.add(12L);
		}
		userMood.setLikeqqs(likeqqs);
		
		userMood.setCommentcount(10);
		
		List<UserComment> comments = new ArrayList<UserComment>();
		{
			UserComment comment21 = new UserComment();
			{
				comment21.setQq(21L);
				comment21.setComment("下午好！");
				comment21.setTime("2014年3月16日");
				List<SecondaryUserComment> secondarycomments21 = new ArrayList<SecondaryUserComment>();
				{
					SecondaryUserComment secondarycomments31 = new SecondaryUserComment();
					secondarycomments31.setQq(31L);
					secondarycomments31.setOqq(21L);
					secondarycomments31.setComment("谢谢！");
					secondarycomments31.setTime("2014年3月16日");
					secondarycomments31.setUsercomment(comment21);
					secondarycomments21.add(secondarycomments31);
				}
				comment21.setSecondarycomments(secondarycomments21);
				
			}
			comment21.setUsermood(userMood);
			comments.add(comment21);
			
			UserComment comment22 = new UserComment();
			{
				comment22.setQq(22L);
				comment22.setComment("下午好！");
				comment22.setTime("2014年3月16日");
				List<SecondaryUserComment> secondarycomments22 = new ArrayList<SecondaryUserComment>();
				{
					SecondaryUserComment secondarycomments32 = new SecondaryUserComment();
					secondarycomments32.setQq(31L);
					secondarycomments32.setOqq(21L);
					secondarycomments32.setComment("谢谢！");
					secondarycomments32.setTime("2014年3月16日");
					secondarycomments32.setUsercomment(comment22);
					secondarycomments22.add(secondarycomments32);
				}
				comment22.setSecondarycomments(secondarycomments22);
			}
			comment22.setUsermood(userMood);
			comments.add(comment22);
			
		}
		
		userMood.setComments(comments);
		
		userMood.setIsforward(true);
		ForwardedUserMood forwardmood = new ForwardedUserMood();
		userMood.setForwardmood(forwardmood);
		
		storage.insertDuplicate(userMood);
		storage.close();
	}

}
