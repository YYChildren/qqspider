package com.mingchao.snsspider.qq.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.mingchao.snsspider.model.HadoopString;
import com.mingchao.snsspider.model.IdAble;
import com.mingchao.snsspider.model.ToBytes;
import com.mingchao.snsspider.util.Numeric;

@Entity(name="t_user_key")
public class UserKey extends HadoopString implements ToBytes, IdAble{
	private Long id;
	private Long qq;
	private Boolean visitable;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	
	@Override
	public String toHadoopString() {
		return  convertNull(id) + "\t" + convertNull(qq) ;
	}
	@Override
	public byte[] toBytes() {
		return Numeric.toBytes(qq);
	}
	public Boolean getVisitable() {
		return visitable;
	}
	public void setVisitable(Boolean visitable) {
		this.visitable = visitable;
	}
}
