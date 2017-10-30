package com.sssoft.Yundian.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {

	public MyHelper(Context context) {
		super(context, "Saas.db", null, 1);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table user(user_id integer primary key autoincrement,user_phone varchar(15),"
				+ "user_passwd varchar(20))");

		db.execSQL(
				"create table goods(goods_id integer primary key autoincrement,goods_name text,goods_type_id integer,goods_spec text,"
						+ "goods_units_id integer,goods_price text,goods_num text,goods_remark text,"
						+ "goods_sales text,goods_img text,goods_date text)");

		db.execSQL("create table goods_type(goods_type_id integer primary key autoincrement,goods_type_name text)");

		db.execSQL("create table goods_units(goods_units_id integer primary key autoincrement,"
				+ "goods_units_name text)");

		db.execSQL("create table goods_details(goods_details_id integer primary key autoincrement,"
				+ "orders_number text,goods_name text,goods_price text,"
				+ "goods_details_num text,goods_details_total_price text)");

		db.execSQL("create table orders(orders_id integer primary key autoincrement,"
				+ "orders_number text,orders_state text,orders_date text,"
				+ "orders_method text,orders_total_price text,orders_actual_price text,"
				+ "orders_details_change text)");
		
		db.execSQL("create table put_orders("+ 
				"porders_id integer primary key autoincrement," + 
				"porders_state integer," + 
				"porders_date text," + 
				"porders_num text)");
		
		db.execSQL("create table put_goods("+
				"pgoods_id integer primary key autoincrement," + 
				"porders_id integer," + 
				"goods_id integer,pgoods_selectnum integer)");
		
		db.execSQL("INSERT INTO `goods` VALUES (1, '冰激凌', 1, '25g', 1, '12.05', '200', '', '10', '', '2017.11.1 16:37:59')");
		db.execSQL("INSERT INTO `goods` VALUES (2, '冰棍', 2, '30g', 2, '3.50', '200', '', '20', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods` VALUES (3, '牛奶', 2, '30g', 2, '6.50', '200', '', '30', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods` VALUES (4, '茄子', 2, '30g', 2, '12.50', '200', '', '0', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods` VALUES (5, '雪糕', 2, '30g', 2, '22.50', '200', '', '0', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods` VALUES (6, '蛋糕', 2, '30g', 2, '32.50', '200', '', '10', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods` VALUES (7, '香蕉', 2, '30g', 2, '42.50', '200', '', '20', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods` VALUES (8, '苹果', 2, '30g', 2, '62.50', '200', '', '31110', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods` VALUES (9, '草莓', 2, '30g', 2, '72.50', '200', '', '1111', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods` VALUES (10, '梅子', 2, '30g', 2, '92.50', '200', '', '5111', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods` VALUES (11, '西瓜', 2, '30g', 2, '12.50', '200', '', '0', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods` VALUES (12, '冬瓜', 2, '30g', 2, '22.50', '200', '', '8111', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods` VALUES (13, '南瓜', 2, '30g', 2, '32.50', '200', '', '0', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods` VALUES (14, '核桃', 2, '30g', 2, '52.50', '200', '', '9022', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods` VALUES (15, '木瓜', 2, '30g', 2, '2005.50', '200', '', '11112', '', '2017.11.1 16:40:01')");
		db.execSQL("INSERT INTO `goods_type` VALUES (1, '甜品')");
		db.execSQL("INSERT INTO `goods_type` VALUES (2, '冷饮')");
		db.execSQL("INSERT INTO `goods_units` VALUES (1, '个')");
		db.execSQL("INSERT INTO `goods_units` VALUES (2, '杯')");
		db.execSQL("INSERT INTO `orders` VALUES (1, '11', '已支付' ,'2017-11-01 16:37:59', '现金', '1.5', '2.5', '200' )");
		db.execSQL("INSERT INTO `orders` VALUES (2, '12', '已支付' ,'2017-11-01 16:38:30', '支付宝', '1.5', '2.5', '200' )");
		db.execSQL("INSERT INTO `orders` VALUES (4, '13', '已支付' ,'2017-11-01 16:39:59', '银行卡', '2.5', '2.5', '200' )");
		db.execSQL("INSERT INTO `orders` VALUES (5, '342665', '已退款' ,'2017-11-01 16:50:59', '微信', '35', '35', '35' )");
		db.execSQL("INSERT INTO `orders` VALUES (6, 'wer4535435', '已支付' ,'2017-10-16 16:37:19', '现金', '1', '2.5', '200' )");
		db.execSQL("INSERT INTO `orders` VALUES (7, '15', '已支付' ,'2017-10-23 16:37:29', '现金', '1', '2.5', '200' )");
		db.execSQL("INSERT INTO `orders` VALUES (8, '16', '已支付' ,'2017-10-16 16:37:39', '微信', '1', '2.5', '200' )");
		db.execSQL("INSERT INTO `orders` VALUES (9, '17', '已退款' ,'2017-10-16 16:37:39', '微信', '1', '2.5', '200' )");
		db.execSQL("INSERT INTO `orders` VALUES (10, '18', '已支付' ,'2017-10-17 16:37:39', '支付宝', '1', '2.5', '200' )");
		db.execSQL("INSERT INTO `orders` VALUES (11, '20', '已支付' ,'2017-10-16 16:37:39', '微信', '1', '2.5', '200' )");
		db.execSQL("INSERT INTO `orders` VALUES (12, '21', '已支付' ,'2017-10-18 16:37:39', '微信', '1', '2.5', '200' )");
		db.execSQL("INSERT INTO `orders` VALUES (13, '22', '已支付' ,'2017-10-23 16:37:39', '微信', '1', '2.5', '200' )");
		db.execSQL("INSERT INTO `orders` VALUES (14, '23', '已支付' ,'2017-10-23 16:37:39', '银联卡', '1', '2.5', '200' )");
		db.execSQL("INSERT INTO `orders` VALUES (15, '24', '已支付' ,'2017-10-23 16:37:39', '银联卡', '3.5', '2.5', '200' )");
		db.execSQL("insert into `goods_details` values (1,'22','可乐','2.5','2','5')");
		db.execSQL("insert into `goods_details` values (2,'11','可乐','2.5','2','5')");
		db.execSQL("insert into `goods_details` values (3,'12','可乐','2.5','2','5')");
		db.execSQL("insert into `goods_details` values (4,'11','水果','2.5','2','5')");
		db.execSQL("insert into `goods_details` values (5,'342665','牛奶','2.5','2','5')");
		db.execSQL("insert into `goods_details` values (6,'22','牛奶','2.5','2','5')");
		db.execSQL("insert into `goods_details` values (7,'wer4535435','牛奶','2.5','2','5')");
		db.execSQL("insert into `goods_details` values (8,'23','奶茶','2.5','2','5')");
		db.execSQL("insert into `goods_details` values (9,'24','咖啡','2.5','2','5')");
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
