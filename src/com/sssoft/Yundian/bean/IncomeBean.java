/**
 * 
 */
package com.sssoft.Yundian.bean;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.R.string;

/**
 * @author JKCoCo
 *
 */
public class IncomeBean {
	// 订单表id
	private Long Orders_id;
	// 订单号 订单表-商品详情为一对多，一个订单里可以有多个商品
	private String Orders_number;
	// 订单状态
	private String Orders_state;
	// 订单日期
	private String Orders_date;
	// 收款方式
	private String Orders_method;
	// 合计金额
	private String Orders_total_price;
	// 实收金额
	private String Orders_actual_price;
	// 找零金额
	private String Orders_details_change;

	// 商品详情id
	private Long Goods_details_id;
	// 商品名称
	private String Goods_name;
	// 商品单价
	private String Goods_price;
	// 商品数量
	private String Goods_details_num;
	// 总金额
	private String Goods_details_total_price;

	// 订单id
	public void setOrders_id(Long i) {
		this.Orders_id = i;
	}

	public Long getOrders_id() {
		return Orders_id;
	}

	// 订单号
	public void setOrders_number(String Orders_number) {
		this.Orders_number = Orders_number;
	}

	public String getOrders_number() {
		return Orders_number;
	}

	// 订单状态
	public void setOrders_state(String Orders_state) {
		this.Orders_state = Orders_state;
	}

	public String getOrders_state() {
		return Orders_state;
	}

	// 订单日期
	public void setOrders_date(String Orders_date) {
		this.Orders_date = Orders_date;
	}

	// public String getorders_date(){
	//
	// }
	public String getOrders_date() {
		return Orders_date;
	}

	// 订单状态
	public void setOrders_method(String Orders_method) {
		this.Orders_method = Orders_method;
	}

	public String getOrders_method() {
		return Orders_method;
	}

	// 合计金额
	public void setOrders_total_price(String Orders_total_price) {
		this.Orders_total_price = Orders_total_price;
	}

	public String getOrders_total_price() {
		return Orders_total_price;
	}

	// 实收金额
	public void setOrders_actual_price(String Orders_actual_price) {
		this.Orders_actual_price = Orders_actual_price;
	}

	public String getOrders_actual_price() {
		return Orders_actual_price;
	}

	// 找零金额
	public void setOrders_details_change(String Orders_details_change) {
		this.Orders_details_change = Orders_details_change;
	}

	public String getOrders_details_change() {
		return Orders_details_change;
	}

	public void Goods_toString(Long Goods_details_id, String Goods_name, String Goods_price, String Goods_details_num,
			String Goods_details_total_price) {
		this.Goods_details_id = Goods_details_id;
		this.Goods_name = Goods_name;
		this.Goods_price = Goods_price;
		this.Goods_details_num = Goods_details_num;
		this.Goods_details_total_price = Goods_details_total_price;

	}

	// 商品详情id
	public void setGoods_details_id(Long Goods_details_id) {
		this.Goods_details_id = Goods_details_id;
	}

	public Long getGoods_details_id() {
		return Goods_details_id;
	}

	// 商品名
	public void setGoods_name(String Goods_name) {
		this.Goods_name = Goods_name;
	}

	public String getGoods_name() {
		return Goods_name;
	}

	public void setGoods_price(String Goods_price) {
		this.Goods_price = Goods_price;
	}

	public String getGoods_price() {
		return Goods_price;
	}

	public void setGoods_details_num(String Goods_details_num) {
		this.Goods_details_num = Goods_details_num;
	}

	public String getGoods_details_num() {
		return Goods_details_num;
	}

	public void setGoods_details_total_price(String Goods_details_total_price) {
		this.Goods_details_total_price = Goods_details_total_price;
	}

	public String getGoods_details_total_price() {
		return Goods_details_total_price;
	}

	

	// 订单
	public IncomeBean(Long Orders_id, String Orders_number, String Orders_state, String Orders_date,
			String Orders_method, String Orders_total_price, String Orders_actual_price, String Orders_details_change) {
		super();
		this.Orders_id = Orders_id;
		this.Orders_number = Orders_number;
		this.Orders_state = Orders_state;
		this.Orders_date = Orders_date;
		this.Orders_method = Orders_method;
		this.Orders_total_price = Orders_total_price;
		this.Orders_actual_price = Orders_actual_price;
		this.Orders_details_change = Orders_details_change;
	}

	public IncomeBean() {
		super();
	}

	// //订单详情
	// public IncomeBean(String Orders_number,String Orders_state, String
	// Orders_method,String Orders_date,String Orders_actual_price){
	// this.Orders_number = Orders_number;
	// this.Orders_state = Orders_number;
	// this.Orders_method = Orders_method;
	// this.Orders_date = Orders_date;
	// this.Orders_actual_price = Orders_actual_price;
	// }
	public IncomeBean(String Goods_name, String Goods_price, String Goods_details_num,
			String Goods_details_total_price) {
		this.Goods_name = Goods_name;
		this.Goods_price = Goods_price;
		this.Goods_details_num = Goods_details_num;
		this.Goods_details_total_price = Goods_details_total_price;

	}

//	public IncomeBean(long ordersId, String platformTxnNo, String orders_state2, String txnAnsTime, String payMethod,
//			String orders_total_price2, String orders_actual_price2, String payerID) {
//		// TODO Auto-generated constructor stub
//		this.Goods_details_id = ordersId;
//		this.Orders_number = platformTxnNo;
//		this.Orders_state = orders_state2;
//		this.Orders_date = txnAnsTime;
//		this.Orders_method = payMethod;
//		this.Orders_total_price = orders_total_price2;
//		this.Orders_actual_price = orders_actual_price2;
//		this.Orders_details_change = Orders_details_change;
//	}

	/**
	 * @param i
	 * @return
	 */
	
}
