package com.sssoft.Yundian.bean;

public class Other {
	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Other(Long id, String name) {
		super();
		this.id = id;
		this.name = name;

	}

	public Other(String name) {
		super();
		this.name = name;

	}

	public Other() {
		super();
	}

	public String toString() {
		return "[名称" + name + "]";
	}
}
