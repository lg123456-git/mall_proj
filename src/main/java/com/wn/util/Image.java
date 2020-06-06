package com.wn.util;

public class Image {
	private String imgName;
	private String img;
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public Image(String imgName, String img) {
		super();
		this.imgName = imgName;
		this.img = img;
	}
	public Image() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Image [imgName=" + imgName + ", img=" + img + "]";
	}
	
}
