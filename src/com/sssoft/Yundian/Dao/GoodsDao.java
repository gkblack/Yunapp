package com.sssoft.Yundian.Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.sssoft.Yundian.bean.Goods;
import com.sssoft.Yundian.bean.Other;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class GoodsDao {
	private MyHelper helper;

	public GoodsDao(Context context) {
		helper = new MyHelper(context);
	}

	// 获取所有商品
	public List<Goods> queryAll() {
		HashMap<Integer, String> types = getType();
		HashMap<Integer, String> utils = getUnits();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("goods", null, null, null, null, null, "goods_id Desc");
		List<Goods> list = new ArrayList<Goods>();
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("goods_id"));
			String name = cursor.getString(cursor.getColumnIndex("goods_name"));
			int type_id = cursor.getInt(cursor.getColumnIndex("goods_type_id"));
			String spec = cursor.getString(cursor.getColumnIndex("goods_spec"));
			int units_id = cursor.getInt(cursor.getColumnIndex("goods_units_id"));
			String price = cursor.getString(cursor.getColumnIndex("goods_price"));
			String num = cursor.getString(cursor.getColumnIndex("goods_num"));
			String remark = cursor.getString(cursor.getColumnIndex("goods_remark"));
			String sales = cursor.getString(cursor.getColumnIndex("goods_sales"));
			String img = cursor.getString(cursor.getColumnIndex("goods_img"));
			String date = cursor.getString(cursor.getColumnIndex("goods_date"));
			String type = types.get(type_id);
			String unit = utils.get(units_id);
			list.add(new Goods(id, name, type, spec, unit, price, num, remark, sales, img, date));
		}
		cursor.close();
		db.close();
		return list;
	}

	// 获取某个挂单号内所有商品
	public List<Goods> findMarkGood(long porders_id, HashMap<Integer, Integer> pgoods) {
		HashMap<Integer, String> types = getType();
		HashMap<Integer, String> utils = getUnits();
		List<Goods> list = new ArrayList<Goods>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from goods where goods_id in (select goods_id from put_goods where porders_id=?)",
				new String[] { porders_id + "" });
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("goods_id"));
			String name = cursor.getString(cursor.getColumnIndex("goods_name"));
			int type_id = cursor.getInt(cursor.getColumnIndex("goods_type_id"));
			String spec = cursor.getString(cursor.getColumnIndex("goods_spec"));
			int units_id = cursor.getInt(cursor.getColumnIndex("goods_units_id"));
			String price = cursor.getString(cursor.getColumnIndex("goods_price"));
			String num = cursor.getString(cursor.getColumnIndex("goods_num"));
			String remark = cursor.getString(cursor.getColumnIndex("goods_remark"));
			String sales = cursor.getString(cursor.getColumnIndex("goods_sales"));
			String img = cursor.getString(cursor.getColumnIndex("goods_img"));
			String date = cursor.getString(cursor.getColumnIndex("goods_date"));
			String type = types.get(type_id);
			String unit = utils.get(units_id);
			Goods a = new Goods(id, name, type, spec, unit, price, num, remark, sales, img, date);
			a.setSelected(true);
			a.setSelectedNum(pgoods.get(a.getGoods_id()));
			list.add(a);
		}

		cursor.close();
		db.close();
		return list;

	}

	// 获取所有hashmap类型的种类
	@SuppressLint("UseSparseArrays")
	public HashMap<Integer, String> getType() {
		HashMap<Integer, String> types = new HashMap<Integer, String>();

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("goods_type", null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("goods_type_id"));
			String goods_type = cursor.getString(cursor.getColumnIndex("goods_type_name"));
			types.put(id, goods_type);
		}
		cursor.close();
		db.close();
		return types;
	}

	// 获取所有hashmap类型的单位
	@SuppressLint("UseSparseArrays")
	public HashMap<Integer, String> getUnits() {
		HashMap<Integer, String> units = new HashMap<Integer, String>();

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("goods_units", null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("goods_units_id"));
			String goods_units = cursor.getString(cursor.getColumnIndex("goods_units_name"));
			units.put(id, goods_units);
		}
		cursor.close();
		db.close();
		return units;

	}

	// 增加商品
	public boolean insert(String name, String type, String spec, String unit, String price, String num, String remark,
			String sales, String img, String date) {
		HashMap<Integer, String> types = getType();
		HashMap<Integer, String> utils = getUnits();
		int typeNum = 0;
		int unitsNum = 0;
		for (Entry<Integer, String> t : types.entrySet()) {
			if (t.getValue().equals(type)) {
				typeNum = t.getKey();
			}
		}
		for (Entry<Integer, String> t : utils.entrySet()) {
			if (t.getValue().equals(unit)) {
				unitsNum = t.getKey();
			}
		}

		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("goods_name", name);
		values.put("goods_spec", spec);
		values.put("goods_type_id", typeNum);
		values.put("goods_units_id", unitsNum);
		values.put("goods_price", price);
		values.put("goods_sales", sales);
		values.put("goods_remark", remark);
		values.put("goods_sales", sales);
		values.put("goods_num", num);
		values.put("goods_img", img);
		values.put("goods_date", date);

		db.insert("goods", null, values);
		db.close();
		return true;
	}

	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	// 获取所有的单位
	public String[] queryAllType() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query("goods_type", null, null, null, null, null, "goods_type_id");
		List<Other> list = new ArrayList<Other>();
		while (c.moveToNext()) {
			long id = c.getLong(c.getColumnIndex("goods_type_id"));
			String name = c.getString(1);
			list.add(new Other(id, name));
		}
		c.close();
		db.close();
		String[] a = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			a[i] = list.get(i).getName();
		}
		return a;
	}

	// 下拉框种类获取
	public String[] queryType() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query("goods_type", null, null, null, null, null, "goods_type_id");
		List<Other> list = new ArrayList<Other>();
		while (c.moveToNext()) {
			long id = c.getLong(c.getColumnIndex("goods_type_id"));
			String name = c.getString(1);
			list.add(new Other(id, name));
		}
		c.close();
		db.close();
		String[] a = new String[list.size() + 1];
		a[0] = "全部";
		for (int i = 1; i < list.size() + 1; i++) {
			a[i] = list.get(i - 1).getName();
		}
		return a;
	}

	// 获取所有的单位
	public String[] queryAllUnits() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query("goods_units", null, null, null, null, null, "goods_units_id");
		List<Other> list = new ArrayList<Other>();
		while (c.moveToNext()) {
			long id = c.getLong(c.getColumnIndex("goods_units_id"));
			String name = c.getString(1);
			list.add(new Other(id, name));
		}
		c.close();
		db.close();
		String[] a = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			a[i] = list.get(i).getName();
		}
		return a;
	}

	// 排序
	public List<Goods> sortByGoods(long selected) {
		HashMap<Integer, String> types = getType();
		HashMap<Integer, String> utils = getUnits();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from goods where goods_type_id =?", new String[] { selected + "" });
		List<Goods> list = new ArrayList<Goods>();
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("goods_id"));
			String name = cursor.getString(cursor.getColumnIndex("goods_name"));
			int type_id = cursor.getInt(cursor.getColumnIndex("goods_type_id"));
			String spec = cursor.getString(cursor.getColumnIndex("goods_spec"));
			int units_id = cursor.getInt(cursor.getColumnIndex("goods_units_id"));
			String price = cursor.getString(cursor.getColumnIndex("goods_price"));
			String num = cursor.getString(cursor.getColumnIndex("goods_num"));
			String remark = cursor.getString(cursor.getColumnIndex("goods_remark"));
			String sales = cursor.getString(cursor.getColumnIndex("goods_sales"));
			String img = cursor.getString(cursor.getColumnIndex("goods_img"));
			String date = cursor.getString(cursor.getColumnIndex("goods_date"));
			String type = types.get(type_id);
			String unit = utils.get(units_id);
			list.add(new Goods(id, name, type, spec, unit, price, num, remark, sales, img, date));
		}
		cursor.close();
		db.close();
		return list;

	}

	// 删除某个good
	public void deleteGood(int id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("goods", "goods_id=?", new String[] { id + "" });
		db.close();
	}

	// 更新一个商品
	public void update(int goodId, String name, String type, String spec, String unit, String price, String num,
			String remark, String sales, String img, String date) {
		// TODO Auto-generated method stub
		HashMap<Integer, String> types = getType();
		HashMap<Integer, String> utils = getUnits();
		int typeNum = 0;
		int unitsNum = 0;
		for (Entry<Integer, String> t : types.entrySet()) {
			if (t.getValue().equals(type)) {
				typeNum = t.getKey();
			}
		}
		for (Entry<Integer, String> t : utils.entrySet()) {
			if (t.getValue().equals(unit)) {
				unitsNum = t.getKey();
			}
		}
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("goods_name", name);
		values.put("goods_spec", spec);
		values.put("goods_type_id", typeNum);
		values.put("goods_units_id", unitsNum);
		values.put("goods_price", price);
		values.put("goods_sales", sales);
		values.put("goods_remark", remark);
		values.put("goods_sales", sales);
		values.put("goods_num", num);
		values.put("goods_img", img);
		values.put("goods_date", date);
		db.update("goods", values, "goods_id=?", new String[] { goodId + "" });

	}

}
