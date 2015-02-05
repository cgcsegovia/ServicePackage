package com.smarthotel.servicepack.services;

import java.util.ArrayList;
import java.util.List;

public class Query {
	private String src;
	private String type;
	private String name;
	private List<Param> params = new ArrayList<Param>();
	
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Param> getParams() {
		return params;
	}
	public void setParams(List<Param> params) {
		this.params = params;
	}
	
	
	
}
