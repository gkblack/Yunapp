/**
 * 
 */
package com.sssoft.Yundian.Dao;

import java.util.ArrayList;
import java.util.List;

import com.sssoft.Yundian.bean.IncomeBean;
import com.sssoft.Yundian.utils.Arith;
import com.sssoft.Yundian.utils.FormatUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author JKCoCo 收入排序方式
 */
public class IncomeSpinnerDao {
	private SQLiteDatabase sqldb;
	private MyHelper SpinnerHelper;
	private IncomeBean iBean;
	// 收入
	private String income;
	private String count_income;
	// 选择收入方式和日期排序后，重新统计listview收入
	private Double AllIncome;

	public IncomeSpinnerDao(Context context) {
		SpinnerHelper = new MyHelper(context);
	}

	// 收入排序(昨日和今日)
	public List<IncomeBean> Income_Date(String date) {
		income = "";
		AllIncome = 0.00;
		sqldb = SpinnerHelper.getReadableDatabase();
		String sql = "select * from orders where substr(orders_date,1,10)=?";
		Cursor cur = sqldb.rawQuery(sql, new String[] { date });
		List<IncomeBean> list = new ArrayList<IncomeBean>();
		while (cur.moveToNext()) {
			long id = cur.getLong(cur.getColumnIndex("orders_id"));
			String orders_number = cur.getString(cur.getColumnIndex("orders_number"));
			String orders_state = cur.getString(cur.getColumnIndex("orders_state"));
			String orders_date = cur.getString(cur.getColumnIndex("orders_date"));
			String orders_method = cur.getString(cur.getColumnIndex("orders_method"));
			String orders_total_price = cur.getString(cur.getColumnIndex("orders_total_price"));
			String orders_actual_price = cur.getString(cur.getColumnIndex("orders_actual_price"));
			String orders_details_change = cur.getString(cur.getColumnIndex("orders_details_change"));
			count_income = count_income(orders_state, orders_actual_price);
			IncomeBean ib = new IncomeBean(id, orders_number, orders_state, orders_date, orders_method,
					orders_total_price, orders_actual_price, orders_details_change);
			list.add(ib);
		}

		cur.close();
		sqldb.close();
		return list;

	}

	// 选择近一周收入
	public List<IncomeBean> incomeWeek() {
		income = "";
		AllIncome = 0.00;
		sqldb = SpinnerHelper.getReadableDatabase();
		String sql = "select *  from orders where orders_date between "
				+ "datetime(date(datetime('now',strftime('-%w day','now'))),'+1 second') "
				+ "and datetime(date(datetime('now',(6 - strftime('%w day','now'))||' day','1 day')),'-1 second')";
		// Cursor cur = sqldb.query("orders", null, selection, null, null, null,
		// null)
		Cursor cur = sqldb.rawQuery(sql, null);
		List<IncomeBean> week_list = new ArrayList<IncomeBean>();
		while (cur.moveToNext()) {
			long orders_id = cur.getLong(cur.getColumnIndex("orders_id"));
			String orders_number = cur.getString(cur.getColumnIndex("orders_number"));
			String orders_state = cur.getString(cur.getColumnIndex("orders_state"));
			String orders_date = cur.getString(cur.getColumnIndex("orders_date"));
			String orders_method = cur.getString(cur.getColumnIndex("orders_method"));
			String orders_total_price = cur.getString(cur.getColumnIndex("orders_total_price"));
			String orders_actual_price = cur.getString(cur.getColumnIndex("orders_actual_price"));
			String orders_details_change = cur.getString(cur.getColumnIndex("orders_details_change"));

			count_income = count_income(orders_state, orders_actual_price);

			IncomeBean ib = new IncomeBean(orders_id, orders_number, orders_state, orders_date, orders_method,
					orders_total_price, orders_actual_price, orders_details_change);
			week_list.add(ib);
		}
		cur.close();
		sqldb.close();
		return week_list;
	}

	// 全部收入的支付方式排序
	public List<IncomeBean> all_pay_method(String method) {
		income = "";
		AllIncome = 0.00;
		sqldb = SpinnerHelper.getReadableDatabase();
		Cursor cur = sqldb.rawQuery("select * from orders where orders_method=?", new String[] { method });
		List<IncomeBean> method_list = new ArrayList<IncomeBean>();
		while (cur.moveToNext()) {
			long orders_id = cur.getLong(cur.getColumnIndex("orders_id"));
			String orders_number = cur.getString(cur.getColumnIndex("orders_number"));
			String orders_state = cur.getString(cur.getColumnIndex("orders_state"));
			String orders_date = cur.getString(cur.getColumnIndex("orders_date"));
			String orders_method = cur.getString(cur.getColumnIndex("orders_method"));
			String orders_total_price = cur.getString(cur.getColumnIndex("orders_total_price"));
			String orders_actual_price = cur.getString(cur.getColumnIndex("orders_actual_price"));
			String orders_details_change = cur.getString(cur.getColumnIndex("orders_details_change"));
			count_income = count_income(orders_state, orders_actual_price);
			IncomeBean ib = new IncomeBean(orders_id, orders_number, orders_state, orders_date, orders_method,
					orders_total_price, orders_actual_price, orders_details_change);
			method_list.add(ib);
		}
		cur.close();
		sqldb.close();
		return method_list;
	}

	// 今日和昨日的支付方式排序(不包含全部支付方式)
	public List<IncomeBean> pay_method(String date, String method) {
		income = "";
		AllIncome = 0.00;
		sqldb = SpinnerHelper.getReadableDatabase();
		Cursor cur = sqldb.rawQuery("select * from orders where " + "substr(orders_date,1,10)=? and orders_method=?",
				new String[] { date, method });
		List<IncomeBean> method_list = new ArrayList<IncomeBean>();
		while (cur.moveToNext()) {
			long orders_id = cur.getLong(cur.getColumnIndex("orders_id"));
			String orders_number = cur.getString(cur.getColumnIndex("orders_number"));
			String orders_state = cur.getString(cur.getColumnIndex("orders_state"));
			String orders_date = cur.getString(cur.getColumnIndex("orders_date"));
			String orders_method = cur.getString(cur.getColumnIndex("orders_method"));
			String orders_total_price = cur.getString(cur.getColumnIndex("orders_total_price"));
			String orders_actual_price = cur.getString(cur.getColumnIndex("orders_actual_price"));
			String orders_details_change = cur.getString(cur.getColumnIndex("orders_details_change"));
			count_income = count_income(orders_state, orders_actual_price);
			IncomeBean ib = new IncomeBean(orders_id, orders_number, orders_state, orders_date, orders_method,
					orders_total_price, orders_actual_price, orders_details_change);
			method_list.add(ib);
		}
		cur.close();
		sqldb.close();
		return method_list;
	}

	// 今日和昨日的全部支付方式排序
	public List<IncomeBean> pay_method1(String date) {
		income = "";
		AllIncome = 0.00;
		sqldb = SpinnerHelper.getReadableDatabase();
		Cursor cur = sqldb.rawQuery("select * from orders where " + "substr(orders_date,1,10)=?",
				new String[] { date });
		List<IncomeBean> method_list = new ArrayList<IncomeBean>();
		while (cur.moveToNext()) {
			long orders_id = cur.getLong(cur.getColumnIndex("orders_id"));
			String orders_number = cur.getString(cur.getColumnIndex("orders_number"));
			String orders_state = cur.getString(cur.getColumnIndex("orders_state"));
			String orders_date = cur.getString(cur.getColumnIndex("orders_date"));
			String orders_method = cur.getString(cur.getColumnIndex("orders_method"));
			String orders_total_price = cur.getString(cur.getColumnIndex("orders_total_price"));
			String orders_actual_price = cur.getString(cur.getColumnIndex("orders_actual_price"));
			String orders_details_change = cur.getString(cur.getColumnIndex("orders_details_change"));
			count_income = count_income(orders_state, orders_actual_price);
			IncomeBean ib = new IncomeBean(orders_id, orders_number, orders_state, orders_date, orders_method,
					orders_total_price, orders_actual_price, orders_details_change);
			method_list.add(ib);
		}
		cur.close();
		sqldb.close();
		return method_list;
	}

	// 一周的支付方式排序(不包括全部支付方式)
	public List<IncomeBean> week_Method(String method) {
		income = "";
		AllIncome = 0.00;
		sqldb = SpinnerHelper.getReadableDatabase();
		List<IncomeBean> money_list = new ArrayList<IncomeBean>();
		String sql = "select *  from orders where orders_date between "
				+ "datetime(date(datetime('now',strftime('-%w day','now'))),'+1 second') "
				+ "and datetime(date(datetime('now',(6 - strftime('%w day','now'))||' day','1 day')),'-1 second') and orders_method=?";
		Cursor cur = sqldb.rawQuery(sql, new String[] { method });
		List<IncomeBean> week_Method_List = new ArrayList<IncomeBean>();
		while (cur.moveToNext()) {
			long orders_id = cur.getLong(cur.getColumnIndex("orders_id"));
			String orders_number = cur.getString(cur.getColumnIndex("orders_number"));
			String orders_state = cur.getString(cur.getColumnIndex("orders_state"));
			String orders_date = cur.getString(cur.getColumnIndex("orders_date"));
			String orders_method = cur.getString(cur.getColumnIndex("orders_method"));
			String orders_total_price = cur.getString(cur.getColumnIndex("orders_total_price"));
			String orders_actual_price = cur.getString(cur.getColumnIndex("orders_actual_price"));
			String orders_details_change = cur.getString(cur.getColumnIndex("orders_details_change"));
			count_income = count_income(orders_state, orders_actual_price);
			IncomeBean ib = new IncomeBean(orders_id, orders_number, orders_state, orders_date, orders_method,
					orders_total_price, orders_actual_price, orders_details_change);
			week_Method_List.add(ib);
		}
		cur.close();
		sqldb.close();
		return week_Method_List;
	}

	// 收入统计
	private String count_income(String orders_state, String orders_actual_price) {
		if (orders_state.equals("已支付")) {
			// 计算收入
			Double a = Double.parseDouble(orders_actual_price); // 金额转double
			AllIncome = Arith.add(AllIncome, a);
			income = FormatUtil.double2StrAmt(AllIncome); // 格式化金额
			return income;
		} else {
			return income;
		}
	}

	// 获取排序后的收入额统计值
	public String income_all() {

		return count_income;
	}

}
