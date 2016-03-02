package com.mingchao.snsspider.qq.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.mingchao.snsspider.model.HadoopString;
import com.mingchao.snsspider.model.IdAble;

@Entity(name="t_user_relation")
public class UserRelation extends HadoopString implements IdAble{
	private Long id;
	private Long qq;
	private Long oqq;
	
	public UserRelation() {
		super();
	}
	
	public UserRelation(Long qq, Long oqq) {
		super();
		this.qq = qq;
		this.oqq = oqq;
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

	public Long getOqq() {
		return oqq;
	}

	public void setOqq(Long oqq) {
		this.oqq = oqq;
	}

	@Override
	public String toHadoopString() {
		return convertNull(id) + "\t" + convertNull(qq) + "\t" + convertNull(oqq);
	}

}
