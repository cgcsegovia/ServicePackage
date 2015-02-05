package com.smarthotel.servicepack.services;

public class Service {
	private String name;
	private int rol;
	private String bbdd;
	private Query query;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getRol() {
		return rol;
	}
	public void setRol(int rol) {
		this.rol = rol;
	}
	public String getBbdd() {
		return bbdd;
	}
	public void setBbdd(String bbdd) {
		this.bbdd = bbdd;
	}
	public Query getQuery() {
		return query;
	}
	public void setQuery(Query query) {
		this.query = query;
	}
	
	
}
