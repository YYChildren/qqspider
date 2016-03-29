package com.mingchao.snsspider.qq.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import org.hibernate.annotations.Type;

@Entity(name="t_user_comment")
public class UserComment{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Type(type = "objectid")
	private String id; 
	private Long qq;   //评论qq
	private String qqstr;//可能是朋友网，或者是经过加密的qq，与上面的qq二选一
	private String comment;
	private String time;
	@OneToMany(cascade = {CascadeType.ALL})
	@OrderColumn
	private List<SecondaryUserComment> secondarycomments;
	
	@ManyToOne
	private UserMood usermood;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getQq() {
		return qq;
	}

	public void setQq(Long qq) {
		this.qq = qq;
	}
	
	public String getQqstr() {
		return qqstr;
	}

	public void setQqstr(String qqstr) {
		this.qqstr = qqstr;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public List<SecondaryUserComment> getSecondarycomments() {
		return secondarycomments;
	}

	public void setSecondarycomments(List<SecondaryUserComment> secondarycomments) {
		this.secondarycomments = secondarycomments;
	}
	
	public UserMood getUsermood() {
		return usermood;
	}

	public void setUsermood(UserMood usermood) {
		this.usermood = usermood;
	}

}
