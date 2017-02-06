package com.srinnix.kindergarten.model;

/**
 * Created by DELL on 2/6/2017.
 */

public class ChatMember {
	public static final int ONLINE = 1;
	public static final int OFFLINE = 0;
	
	private String name;
	private int status;
	
	public ChatMember(String name, int status) {
		this.name = name;
		this.status = status;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
}
