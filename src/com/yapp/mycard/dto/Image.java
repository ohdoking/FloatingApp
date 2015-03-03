package com.yapp.mycard.dto;


public class Image {

	int id;
	String name;
	Integer cardName;
	Integer img;
	boolean secure;
	String etc;
	String img2;
	
	
	public String getImg2() {
		return img2;
	}
	public void setImg2(String img2) {
		this.img2 = img2;
	}
	public Integer getImg() {
		return img;
	}
	public void setImg(Integer img) {
		this.img = img;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCardName() {
		return cardName;
	}
	public void setCardName(Integer cardName) {
		this.cardName = cardName;
	}

	public boolean isSecure() {
		return secure;
	}
	public void setSecure(boolean secure) {
		this.secure = secure;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}

	
	
}
