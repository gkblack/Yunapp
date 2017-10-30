package com.sssoft.Yundian.bean;

public class Pgoods {
	private int pgoods_id;
	private int porders_id;
	private int goods_id;
	private int pgoods_selectnum;
	private String pgoods_name;

	public Pgoods() {
		super();
	}

	public Pgoods(int pgoods_id, int porders_id, int goods_id, int pgoods_selectnum) {
		this.pgoods_id = pgoods_id;
		this.goods_id = goods_id;
		this.pgoods_id = porders_id;
		this.pgoods_selectnum = pgoods_selectnum;
	}
	
	public Pgoods(int goods_id, int pgoods_selectnum) {
		this.goods_id = goods_id;
		this.pgoods_selectnum = pgoods_selectnum;
	}

	public int getPgoods_id() {
		return pgoods_id;
	}

	public void setPgoods_id(int pgoods_id) {
		this.pgoods_id = pgoods_id;
	}

	public int getPorders_id() {
		return porders_id;
	}

	public void setPorders_id(int porders_id) {
		this.porders_id = porders_id;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public int getPgoods_selectnum() {
		return pgoods_selectnum;
	}

	public void setPgoods_selectnum(int pgoods_selectnum) {
		this.pgoods_selectnum = pgoods_selectnum;
	}

	public String getPgoods_name() {
		return pgoods_name;
	}

	public void setPgoods_name(String pgoods_name) {
		this.pgoods_name = pgoods_name;
	}

}
