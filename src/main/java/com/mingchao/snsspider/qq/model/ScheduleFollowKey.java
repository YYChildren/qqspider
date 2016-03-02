package com.mingchao.snsspider.qq.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.mingchao.snsspider.model.IdAble;
import com.mingchao.snsspider.model.ToBytes;
import com.mingchao.snsspider.util.Numeric;

/**
 * 用于调度用户关系链
 * @author yangchaojun
 *
 */
@Entity(name="t_schedule_follow_key")
public class ScheduleFollowKey implements ToBytes,IdAble {
	private Long id;
	private Long qq;
	private Integer pageNum;

	public ScheduleFollowKey(){
	}

	public ScheduleFollowKey(Long qq, Integer pageNum) {
		super();
		this.qq = qq;
		this.pageNum = pageNum;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQq() {
		return qq;
	}

	public void setQq(Long qq) {
		this.qq = qq;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	@Override
	public byte[] toBytes() {
		return Numeric.toBytes(qq);
	}
}
