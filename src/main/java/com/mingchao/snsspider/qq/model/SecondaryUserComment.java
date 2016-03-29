package com.mingchao.snsspider.qq.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity(name="t_secondary_comment")
public class SecondaryUserComment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Type(type = "objectid")
	private String id;
	private Long qq;   // 评论qq
	private String qqstr;//可能是朋友网，或者是经过加密的qq，与上面的qq二选一
	private Long oqq; // 被评论qq
	private String oqqstr;//可能是朋友网，或者是经过加密的qq，与上面的oqq二选一
	private String comment; //评论
	private String time; //评论时间
	
	@ManyToOne
	private UserComment usercomment;

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

	public Long getOqq() {
		return oqq;
	}

	public void setOqq(Long oqq) {
		this.oqq = oqq;
	}

	public String getOqqstr() {
		return oqqstr;
	}

	public void setOqqstr(String oqqstr) {
		this.oqqstr = oqqstr;
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

	public UserComment getUsercomment() {
		return usercomment;
	}

	public void setUsercomment(UserComment usercomment) {
		this.usercomment = usercomment;
	}
}
