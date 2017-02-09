package com.srinnix.kindergarten.model;

import java.util.ArrayList;

/**
 * Created by DELL on 2/9/2017.
 */

public class Post {
	private int id;
	private String content;
	private ArrayList<String> listImage;
	private int type;
	private long createdAt;
	private String postBy;
	
	public Post(int id, String content, ArrayList<String> listImage, int type, long createdAt, String postBy) {
		this.id = id;
		this.content = content;
		this.listImage = listImage;
		this.type = type;
		this.createdAt = createdAt;
		this.postBy = postBy;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public ArrayList<String> getListImage() {
		return listImage;
	}
	
	public void setListImage(ArrayList<String> listImage) {
		this.listImage = listImage;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public long getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getPostBy() {
		return postBy;
	}
	
	public void setPostBy(String postBy) {
		this.postBy = postBy;
	}
}
