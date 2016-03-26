package com.mingchao.snsspider.qq.model;

import javax.persistence.Embeddable;

@Embeddable
public class SecondaryUserComment {
	private Long qq;   // 评论qq
	private Long oqq; // 被评论qq
	private String comment; //评论
	private String time; //评论时间

	public Long getQq() {
		return qq;
	}

	public void setQq(Long qq) {
		this.qq = qq;
	}

	public Long getOqq() {
		return oqq;
	}

	public void setOqq(Long oqq) {
		this.oqq = oqq;
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
}
