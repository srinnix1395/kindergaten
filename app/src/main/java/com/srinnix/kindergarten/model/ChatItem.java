package com.srinnix.kindergarten.model;

/**
 * Created by DELL on 2/9/2017.
 */

public class ChatItem {
	private int id;
	private int idSender;
	private String message;
	private long createdAt;
	private int layoutType;
	
	public ChatItem(int id, int idSender, String message, long createdAt, int layoutType) {
		this.id = id;
		this.idSender = idSender;
		this.message = message;
		this.createdAt = createdAt;
		this.layoutType = layoutType;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getIdSender() {
		return idSender;
	}
	
	public void setIdSender(int idSender) {
		this.idSender = idSender;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getLayoutType() {
		return layoutType;
	}
	
	public void setLayoutType(int layoutType) {
		this.layoutType = layoutType;
	}
	
	public long getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
}
