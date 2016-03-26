package com.mingchao.snsspider.qq.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;

import org.hibernate.annotations.Type;

@Entity(name="t_user_comment")
public class UserComment {
	@EmbeddedId
	private UserCommentId id;
	private Long qq;   // 评论qq
	private String comment;
	private String time;
	@ElementCollection
	@OrderColumn
	private List<SecondaryUserComment> secondarycomments;
	
	public UserCommentId getId() {
		return id;
	}

	public void setId(UserCommentId id) {
		this.id = id;
	}

	public Long getQq() {
		return qq;
	}

	public void setQq(Long qq) {
		this.qq = qq;
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
}
