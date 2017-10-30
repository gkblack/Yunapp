package com.sssoft.Yundian.Dao;

import java.util.ArrayList;
import java.util.List;

import com.sssoft.Yundian.bean.Other;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * com.sssoft.Yundian.Dao
 * @author Jiang
 * 2017年10月25日下午1:41:36
 */
public class UnitDao {
	private MyHelper helper;

	public UnitDao(Context context) {
		// 创建DAO时创建helper
		helper = new MyHelper(context);
	}

	public void insert(Other account) {
		// 获取数据库对象
		SQLiteDatabase db = helper.getWritableDatabase();
		// 用来装载要插入数据的map<列名，列的值>
		ContentValues values = new ContentValues();
		values.put("goods_units_name", account.getName());
		// 向g表插入数据values
		long id = db.insert("goods_units", null, values);
		account.setId(id);// 得到id
		db.close();// 关闭数据库
	}

	// 根据id删除数据
	public int delete(long id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		// 按条件删除指定表中的数据，返回受影响的行数
		int count = db.delete("goods_units", "goods_units_id=?", new String[] { id + "" });
		db.close();
		return count;
	}

	// 更新数据
	public int update(Other account) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();// 要修改的数据
		values.put("goods_units_name", account.getName());
		int count = db.update("goods_units", values, "goods_units_id=?", new String[] { account.getId() + "" });
		db.close();
		return count;
	}

	// 查询所有数据倒叙排列
	public List<Other> queryAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query("goods_units", null, null, null, null, null, "goods_units_id");
		List<Other> list = new ArrayList<Other>();
		while (c.moveToNext()) {
			// 可以根据列名获取索引
			long id = c.getLong(c.getColumnIndex("goods_units_id"));
			String name = c.getString(1);
			list.add(new Other(id, name));
		}
		c.close();
		db.close();
		return list;
	}

	// 是否存在该单位
	public boolean isHasUnits(String unitsName) {
		boolean bool = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from goods_units where goods_units_name=?", new String[] { unitsName });
		if (c.moveToNext()) {
			bool = true;
		}
		c.close();
		db.close();
		return bool;
	}

	
	public boolean isHasUsed(long id) {
		// 该单位是否被使用
		boolean bool = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from goods where goods_units_id=?", new String[] { id+"" });
		if(cursor.moveToNext()) {
			bool = true;
		}
		cursor.close();
		db.close();
		return bool;
	}

}
