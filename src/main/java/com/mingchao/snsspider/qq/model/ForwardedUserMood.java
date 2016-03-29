package com.mingchao.snsspider.qq.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import org.hibernate.annotations.Type;

@Entity(name="t_forwarded_mood")
public class ForwardedUserMood{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Type(type = "objectid")
	private String id; 
	private Long qq;      
	private String qqstr;//可能是朋友网，或者是经过加密的qq，与上面的qq二选一
	private String dataid;//从说说中
	private String mood;
	private String createtime;
	private Long currenttime;

	//like
	private Integer likecount;
	@ElementCollection
	private List<Long> likeqqs;
	
	// comment
	private Integer commentcount;
	@OneToMany(cascade = {CascadeType.ALL})
	@OrderColumn
	private List<UserComment> comments;
	
	// forward
	private Integer forwardcount;
	
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
	public String getDataid() {
		return dataid;
	}
	public void setDataid(String dataid) {
		this.dataid = dataid;
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
}
