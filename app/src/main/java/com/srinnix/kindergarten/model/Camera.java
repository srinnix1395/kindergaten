package com.srinnix.kindergarten.model;

/**
 * Created by DELL on 2/7/2017.
 */

public class Camera {
	private String name;
	private String url;
	
	public Camera(String name, String url) {
		this.name = name;
		this.url = url;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
