package com.mingchao.snsspider.qq.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.mingchao.snsspider.model.HadoopString;
import com.mingchao.snsspider.model.IdAble;

@Entity(name="t_user_info")
public class UserInfo extends HadoopString implements IdAble{
	private Long id;
	private Long qq;
	private String sex;
	private String age;
	private String birthday;
	private String astro;
	private String live_address;
	private String marriage;
	private String blood;
	private String hometown_address;
	private String career;
	private String company;
	private String company_cadress;
	private String caddress;
	
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAstro() {
		return astro;
	}
	public void setAstro(String astro) {
		this.astro = astro;
	}
	public String getLive_address() {
		return live_address;
	}
	public void setLive_address(String live_address) {
		this.live_address = live_address;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getBlood() {
		return blood;
	}
	public void setBlood(String blood) {
		this.blood = blood;
	}
	public String getHometown_address() {
		return hometown_address;
	}
	public void setHometown_address(String hometown_address) {
		this.hometown_address = hometown_address;
	}
	public String getCareer() {
		return career;
	}
	public void setCareer(String career) {
		this.career = career;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompany_cadress() {
		return company_cadress;
	}
	public void setCompany_cadress(String company_cadress) {
		this.company_cadress = company_cadress;
	}
	public String getCaddress() {
		return caddress;
	}
	public void setCaddress(String caddress) {
		this.caddress = caddress;
	}
	
	@Override
	public String toHadoopString() {
		return convertNull(id) 
				+ "\t" + convertNull(qq)  
				+ "\t" + convertNull(sex)
				+ "\t" + convertNull(age) 
				+ "\t" + convertNull(birthday)  
				+ "\t" + convertNull(astro)  
				+ "\t" + convertNull(live_address)  
				+ "\t" + convertNull(marriage)  
				+ "\t" + convertNull(blood)  
				+ "\t" + convertNull(hometown_address)  
				+ "\t" + convertNull(career)  
				+ "\t" + convertNull(company)  
				+ "\t" + convertNull(company_cadress)
				+ "\t" + convertNull(caddress)  ;
	}
	
}
