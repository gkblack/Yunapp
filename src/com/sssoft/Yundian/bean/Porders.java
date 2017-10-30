package com.sssoft.Yundian.bean;

public class Porders {
	private long porders_id;
	private int porders_state;
	private String porders_date;
	private String porders_num;

	public Porders() {
		super();
	}

	public long getPorders_id() {
		return porders_id;
	}

	public void setPorders_id(long porders_id) {
		this.porders_id = porders_id;
	}

	public int getPorders_state() {
		return porders_state;
	}

	public void setPorders_state(int porders_state) {
		this.porders_state = porders_state;
	}

	public String getPorders_date() {
		return porders_date;
	}

	public void setPorders_date(String porders_date) {
		this.porders_date = porders_date;
	}

	public String getPorders_num() {
		return porders_num;
	}

	public void setPorders_num(String porders_num) {
		this.porders_num = porders_num;
	}
}
