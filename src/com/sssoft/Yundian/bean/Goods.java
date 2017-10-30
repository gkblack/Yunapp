package com.sssoft.Yundian.bean;

import java.io.Serializable;

public class Goods implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int goods_id;
	private String goods_name;
	// 分类
	private String goods_type;
	// 商品规格
	private String goods_spec;
	// 商品单位
	private String goods_units;
	// 价格
	private String goods_price;
	// 库存
	private String goods_num;
	// 备注
	private String goods_remark;
	// 销量
	private String goods_sales;
	// 图片名
	private String goods_img;
	// 增加日期
	private String goods_date;
	// 是否被选中
	private boolean isSelected = false;
	// 选中数量
	private int selectedNum = 0;
	
	private boolean isSee = true;

	public Goods(int id, String name, String type, String spec, String units, String price, String num, String remark,
			String sales, String img, String date) {
		this.goods_id = id;
		this.goods_name = name;
		this.goods_type = type;
		this.goods_spec = spec;
		this.goods_units = units;
		this.goods_price = price;
		this.goods_num = num;
		this.goods_remark = remark;
		this.goods_sales = sales;
		this.goods_img = img;
		this.goods_date = date;
		this.isSee = true;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getSelectedNum() {
		return selectedNum;
	}

	public void setSelectedNum(int selectedNum) {
		this.selectedNum = selectedNum;
	}

	public Goods() {
		super();
	}



	@Override
	public String toString() {
		return "Goods [goods_id=" + goods_id + ", goods_name=" + goods_name + ", goods_type=" + goods_type
				+ ", goods_spec=" + goods_spec + ", goods_units=" + goods_units + ", goods_price=" + goods_price
				+ ", goods_num=" + goods_num + ", goods_remark=" + goods_remark + ", goods_sales=" + goods_sales
				+ ", goods_img=" + goods_img + ", goods_date=" + goods_date + ", isSelected=" + isSelected
				+ ", selectedNum=" + selectedNum + "]";
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}

	public String getGoods_spec() {
		return goods_spec;
	}

	public void setGoods_spec(String goods_spec) {
		this.goods_spec = goods_spec;
	}

	public String getGoods_units() {
		return goods_units;
	}

	public void setGoods_units(String goods_units) {
		this.goods_units = goods_units;
	}

	public String getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}

	public String getGoods_num() {
		return goods_num;
	}

	public void setGoods_num(String goods_num) {
		this.goods_num = goods_num;
	}

	public String getGoods_remark() {
		return goods_remark;
	}

	public void setGoods_remark(String goods_remark) {
		this.goods_remark = goods_remark;
	}

	public String getGoods_sales() {
		return goods_sales;
	}

	public void setGoods_sales(String goods_sales) {
		this.goods_sales = goods_sales;
	}

	public String getGoods_img() {
		return goods_img;
	}

	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}

	public String getGoods_date() {
		return goods_date;
	}

	public void setGoods_date(String goods_date) {
		this.goods_date = goods_date;
	}

	public boolean isSee() {
		return isSee;
	}

	public void setSee(boolean isSee) {
		this.isSee = isSee;
	}

}




