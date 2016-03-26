package com.mingchao.snsspider.qq.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

@Entity(name="t_user_mood")
public class UserMood {
	@EmbeddedId
	private UserMoodId id;
	private String mood;
	private String createtime;
	private Long currenttime;

	//like
	private Integer likecount;
	@ElementCollection
	private List<Long> likeqqs;
	
	// comment
	private Integer commentcount;
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@OrderColumn
	private List<UserComment> comments;
	
	// forward
	private Integer forwardcount;
	private Boolean isforward;//是不是转发，如果是转发，所有点赞归原说说
	@ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
	private UserMood forwardmood;//转发的内容
	
	
	public UserMoodId getId() {
		return id;
	}
	public void setId(UserMoodId id) {
		this.id = id;
	}
	public String getMood() {
		return mood;
	}
	public void setMood(String mood) {
		this.mood = mood;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public Long getCurrenttime() {
		return currenttime;
	}
	public void setCurrenttime(Long currenttime) {
		this.currenttime = currenttime;
	}
	public Integer getLikecount() {
		return likecount;
	}
	public void setLikecount(Integer likecount) {
		this.likecount = likecount;
	}
	public List<Long> getLikeqqs() {
		return likeqqs;
	}
	public void setLikeqqs(List<Long> likeqqs) {
		this.likeqqs = likeqqs;
	}
	public Integer getCommentcount() {
		return commentcount;
	}
	public void setCommentcount(Integer commentcount) {
		this.commentcount = commentcount;
	}
	public List<UserComment> getComments() {
		return comments;
	}
	public void setComments(List<UserComment> comments) {
		this.comments = comments;
	}
	public Integer getForwardcount() {
		return forwardcount;
	}
	public void setForwardcount(Integer forwardcount) {
		this.forwardcount = forwardcount;
	}
	public Boolean getIsforward() {
		return isforward;
	}
	public void setIsforward(Boolean isforward) {
		this.isforward = isforward;
	}
	public UserMood getForwardmood() {
		return forwardmood;
	}
	public void setForwardmood(UserMood forwardmood) {
		this.forwardmood = forwardmood;
	}
}
