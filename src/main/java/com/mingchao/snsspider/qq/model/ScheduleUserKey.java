package com.mingchao.snsspider.qq.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
import com.mingchao.snsspider.model.HadoopString;
import com.mingchao.snsspider.model.IdAble;

/**
 * 用于调度用户
 * @author yangchaojun
 *
 */
@Entity(name="t_schedule_user_key")
public class ScheduleUserKey extends HadoopString implements IdAble{
	private Long id;
	private Long qq;

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
	public String toString() {
		return "ScheduleUserKey [id=" + id + ", qq=" + qq + "]";
	}
	
	public static Funnel<ScheduleUserKey> getFunnel(){
		return ScheduleUserKeyFunnel.INSTANCE;
	}
}

enum ScheduleUserKeyFunnel implements Funnel<ScheduleUserKey> {
	INSTANCE;

	public void funnel(ScheduleUserKey from, PrimitiveSink into) {
		into.putLong(from.getQq());
	}

	@Override
	public String toString() {
		return "Funnels.ScheduleUserKeyFunnel()";
	}
}
