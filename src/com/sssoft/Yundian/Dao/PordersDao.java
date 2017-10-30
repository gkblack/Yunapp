package com.sssoft.Yundian.Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.sssoft.Yundian.bean.Goods;
import com.sssoft.Yundian.bean.Porders;
import com.sssoft.Yundian.utils.others;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * com.sssoft.Yundian.Dao
 * 挂单数据操作
 * @author Jiang
 * 2017年10月25日下午2:08:11
 */
public class PordersDao {
	private MyHelper helper;

	public PordersDao(Context context) {
		helper = new MyHelper(context);
	}

	// 返回某个挂单的所选商品
	public HashMap<Integer, Integer> findGoodId(long porder_id) {
		// List<Pgoods> pgoods = new ArrayList<Pgoods>();
		HashMap<Integer, Integer> pgoods = new HashMap<Integer, Integer>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from put_goods where porders_id=?", new String[] { porder_id + "" });
		while (cursor.moveToNext()) {
			int goods_id = cursor.getInt(cursor.getColumnIndex("goods_id"));
			int pgoods_selectnum = cursor.getInt(cursor.getColumnIndex("pgoods_selectnum"));
			pgoods.put(goods_id, pgoods_selectnum);
		}

		cursor.close();
		db.close();
		return pgoods;

	}

	// 挂单，返回挂单号，porders_state:0 未作废，1作废，2选好了标记
	public long insetInPorders(HashMap<Integer, Goods> hashGood, int sum) {
		HashMap<Integer, Integer> goodMap = getGoodsIdNum(hashGood);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("porders_state", 0);
		values.put("porders_date", new others().getDate());
		values.put("porders_num", sum);
		long id = db.insert("put_orders", null, values);
		db.close();
		insertInPgoods(goodMap, id);
		return id;
	}

	// 保存已选商品
	public void insertInPgoods(HashMap<Integer, Integer> goodMap, long id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		for (Integer goodid : goodMap.keySet()) {
			ContentValues values = new ContentValues();
			values.put("porders_id", id);
			values.put("goods_id", goodid);
			values.put("pgoods_selectnum", goodMap.get(goodid));
			db.insert("put_goods", null, values);
		}
		db.close();
	}

	// 获取商品id
	@SuppressLint("UseSparseArrays")
	public HashMap<Integer, Integer> getGoodsIdNum(HashMap<Integer, Goods> hashgoods) {
		HashMap<Integer, Integer> goodMap = new HashMap<Integer, Integer>();
		for(Entry<Integer, Goods> h:hashgoods.entrySet()) {
			goodMap.put(h.getValue().getGoods_id(), h.getValue().getSelectedNum());
		}
		return goodMap;
	}
	
	// 删除挂单
	public void delete(long id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("put_orders", "porders_id=?", new String[] { id+"" });
		db.delete("put_goods", "porders_id=?", new String[] { id+"" });
		db.close();
	}
	
	// 查
	public List<Porders> queryall() {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("put_orders", null, null, null, null, null, "porders_id desc");
		List<Porders> orders = new ArrayList<Porders>();
		while(cursor.moveToNext()) {
			Porders data = new Porders();
			data.setPorders_id(cursor.getInt(0));
			data.setPorders_state(cursor.getInt(cursor.getColumnIndex("porders_state")));
			data.setPorders_date(cursor.getString(cursor.getColumnIndex("porders_date")));
			data.setPorders_num(cursor.getString(cursor.getColumnIndex("porders_num")));
			orders.add(data);
		}
		return orders;
	}
	
}
