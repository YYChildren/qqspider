package com.mingchao.snsspider.qq.provider;


import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mingchao.snsspider.qq.model.SecondaryUserComment;
import com.mingchao.snsspider.qq.model.UserComment;
import com.mingchao.snsspider.qq.model.UserCommentId;
import com.mingchao.snsspider.qq.model.UserMood;
import com.mingchao.snsspider.qq.model.UserMoodId;
import com.mingchao.snsspider.storage.db.StorageDB;

public class TestMongo2 {
	static ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
	
	public static void main(String[] args) {
		Long qq = 1L;
		String dataid= "asdgviubisrg";
		StorageDB storage = (StorageDB) applicationContext.getBean("storageMongo");
		UserMood userMood = new UserMood();
		UserMoodId id = new UserMoodId();
		{
			id.setDataid(dataid);
			id.setQq(qq);
		}
		userMood.setId(id);
		userMood.setMood("早上好！");
		userMood.setIsforward(true);
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
					secondarycomments31.setQq(21L);
					secondarycomments31.setComment("谢谢！");
					secondarycomments31.setTime("2014年3月16日");
					secondarycomments21.add(secondarycomments31);
				}
				comment21.setSecondarycomments(secondarycomments21);
				
			}
			comments.add(comment21);
			UserCommentId ucid21 = new UserCommentId();
			ucid21.setQq(qq);
			ucid21.setDataid(dataid);
			ucid21.setOrder(1);
			comment21.setId(ucid21);
			
			UserComment comment22 = new UserComment();
			{
				comment22.setQq(22L);
				comment22.setComment("下午好！");
				comment22.setTime("2014年3月16日");
				List<SecondaryUserComment> secondarycomments22 = new ArrayList<SecondaryUserComment>();
				{
					SecondaryUserComment secondarycomments32 = new SecondaryUserComment();
					secondarycomments32.setQq(31L);
					secondarycomments32.setQq(21L);
					secondarycomments32.setComment("谢谢！");
					secondarycomments32.setTime("2014年3月16日");
					secondarycomments22.add(secondarycomments32);
				}
				comment22.setSecondarycomments(secondarycomments22);
			}
			comments.add(comment22);
			UserCommentId ucid22 = new UserCommentId();
			ucid22.setQq(qq);
			ucid22.setDataid(dataid);
			ucid22.setOrder(2);
			comment22.setId(ucid22);
			
		}
		
		userMood.setComments(comments);
		storage.insertDuplicate(userMood);
		
		storage.close();
	}
}
