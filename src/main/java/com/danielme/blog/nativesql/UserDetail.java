package com.danielme.blog.nativesql;

import java.math.BigInteger;

public class UserDetail {

	private BigInteger id;
	private String details;

	public UserDetail() {
		super();
	}

	public UserDetail(BigInteger id, String details) {
		super();
		this.id = id;
		this.details = details;
	}
	
	public UserDetail(Long id, String details) {
		super();
		this.id = BigInteger.valueOf(id);
		this.details = details;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
