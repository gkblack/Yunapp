/**
 * 
 */
package com.sssoft.Yundian.Dao;

import java.util.ArrayList;
import java.util.List;

import com.sssoft.Yundian.bean.IncomeBean;
import com.sssoft.Yundian.utils.Arith;
import com.sssoft.Yundian.utils.FormatUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author 我的收入页及订单详情页数据查询
 *
 */
public class IncomeDao {
	private MyHelper IncomeHelper;
	private SQLiteDatabase db;
//	private int pageSize=15; //每页显示数据数

	public IncomeDao(Context context) {
		IncomeHelper = new MyHelper(context);
	}

	// 获取总收入
	public String income_all() {
		Double AllIncome = 0.0;
		String s = "";
		db = IncomeHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from orders where orders_state=?",new String[]{ "已支付"} );
		while (cursor.moveToNext()) {
			String ord_income = cursor.getString(cursor.getColumnIndex("orders_actual_price"));
			// 计算收入
			Double a = Double.parseDouble(ord_income); // 金额转double
			AllIncome = Arith.add(AllIncome, a);
			s = FormatUtil.double2StrAmt(AllIncome); // 格式化金额
		}
		cursor.close();
		db.close();
		return s;
	}
	// 查询记录的总数  
    public int getCount() {  
        SQLiteDatabase db = IncomeHelper.getWritableDatabase();  
        String sql = "select count(*) from orders";  
        Cursor c = db.rawQuery(sql, null);  
        c.moveToFirst();  
        int length = c.getInt(0);  
        c.close();  
        return length;  
    }  
	
//	/**
//     * 分页查询
//     * 
//     * @param currentPage 当前页
//     * @param pageSize 每页显示的记录
//     * @return 当前页的记录
//     */
//    public ArrayList<IncomeBean> getAllItems(int currentPage, int pageSize) {
//        int firstResult = (currentPage - 1) * pageSize;
//        int maxResult = currentPage * pageSize;
//        db = IncomeHelper.getWritableDatabase();
//        Cursor c = db.rawQuery( "select * from database order by  _id desc limit ?,? ",
//        		new String[] { String.valueOf(firstResult),String.valueOf(pageSize) });
//        ArrayList<IncomeBean> items = new ArrayList<IncomeBean>();
//        int columnCount  = c.getColumnCount();
//        while (c.moveToNext()) {
//        	long id = c.getLong(c.getColumnIndex("orders_id"));
//			String orders_number = c.getString(c.getColumnIndex("orders_number"));
//			String orders_state = c.getString(c.getColumnIndex("orders_state"));
//			String orders_date = c.getString(c.getColumnIndex("orders_date"));
//			String orders_method = c.getString(c.getColumnIndex("orders_method"));
//			String orders_total_price = c.getString(c.getColumnIndex("orders_total_price"));
//			String orders_actual_price = c.getString(c.getColumnIndex("orders_actual_price"));
//			String orders_details_change = c.getString(c.getColumnIndex("orders_details_change"));
//			IncomeBean ib = new IncomeBean(id, orders_number, orders_state, orders_date, orders_method,
//					orders_total_price, orders_actual_price, orders_details_change);
//            items.add(ib);
//             
//        }
//        //不关闭数据库,下拉时再次加载数据
//        return items;
//    }

	// 获取全部收入数据
	public List<IncomeBean> queryAll() {
		db = IncomeHelper.getWritableDatabase();
		
		Cursor c = db.query("orders", null, null, null, null, null, "orders_id DESC");
		List<IncomeBean> list = new ArrayList<IncomeBean>();
		while (c.moveToNext()) {
			long id = c.getLong(c.getColumnIndex("orders_id"));
			String orders_number = c.getString(c.getColumnIndex("orders_number"));
			String orders_state = c.getString(c.getColumnIndex("orders_state"));
			String orders_date = c.getString(c.getColumnIndex("orders_date"));
			String orders_method = c.getString(c.getColumnIndex("orders_method"));
			String orders_total_price = c.getString(c.getColumnIndex("orders_total_price"));
			String orders_actual_price = c.getString(c.getColumnIndex("orders_actual_price"));
			String orders_details_change = c.getString(c.getColumnIndex("orders_details_change"));
			IncomeBean ib = new IncomeBean(id, orders_number, orders_state, orders_date, orders_method,
					orders_total_price, orders_actual_price, orders_details_change);
			list.add(ib);

		}
		c.close();
		db.close();
		return list;
	}

	// 获取订单详情listview数据-订单详情页
	public List<IncomeBean> list_goods_details(String orders_number) {
		db = IncomeHelper.getWritableDatabase();
		String sql = "select * from goods_details where orders_number=?";
		Cursor cu = db.rawQuery(sql, new String[] { orders_number });
		List<IncomeBean> goods_list = new ArrayList<IncomeBean>();
		while (cu.moveToNext()) {
			long goods_id = cu.getLong(cu.getColumnIndex("goods_details_id"));
			String goods_name = cu.getString(cu.getColumnIndex("goods_name"));
			String goods_price = cu.getString(cu.getColumnIndex("goods_price"));
			String goods_details_num = cu.getString(cu.getColumnIndex("goods_details_num"));
			String goods_details_total_price = cu.getString(cu.getColumnIndex("goods_details_total_price"));
			IncomeBean ibean = new IncomeBean(goods_name, goods_price, goods_details_num, goods_details_total_price);
			goods_list.add(ibean);

		}
		cu.close();
		db.close();
		return goods_list;

	}

	// 退款时修改数据
	public boolean order_alter(String ordersNum) {
		db = IncomeHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("orders_state", "已退款");
		db.update("orders", values, "orders_number=?", new String[] { ordersNum });
		Cursor cursor = db.rawQuery("select * from orders where orders_number=? and orders_state=?",
				new String[] { ordersNum, "已退款" });
		if (cursor.getCount() > 0) {
			cursor.close();
			db.close();
			return true;
		} else {
			cursor.close();
			db.close();
			return false;
		}
	}

}
