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
public class UserComment  implements java.io.Serializable{
	@Id
	private Long oqq;   // 被评论qq
	@Id
	private String dataid;//从说说中
	@Id
	private Integer order;
	
	private Long qq;   // 评论qq
	private String comment;
	private String time;
	@ElementCollection
	@OrderColumn
	private List<SecondaryUserComment> secondarycomments;
	
	public Long getOqq() {
		return oqq;
	}

	public void setOqq(Long oqq) {
		this.oqq = oqq;
	}

	public String getDataid() {
		return dataid;
	}

	public void setDataid(String dataid) {
		this.dataid = dataid;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
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
