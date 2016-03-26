package com.mingchao.snsspider.qq.model;

import javax.persistence.Embeddable;

@Embeddable
public class UserMoodId implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9091001890467640724L;
	
	private Long qq;
	private String dataid;//从说说中
	public Long getQq() {
		return qq;
	}
	public void setQq(Long qq) {
		this.qq = qq;
	}
	public String getDataid() {
		return dataid;
	}
	public void setDataid(String dataid) {
		this.dataid = dataid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataid == null) ? 0 : dataid.hashCode());
		result = prime * result + ((qq == null) ? 0 : qq.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserMoodId other = (UserMoodId) obj;
		if (dataid == null) {
			if (other.dataid != null)
				return false;
		} else if (!dataid.equals(other.dataid))
			return false;
		if (qq == null) {
			if (other.qq != null)
				return false;
		} else if (!qq.equals(other.qq))
			return false;
		return true;
	}
}
