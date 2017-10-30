package com.sssoft.Yundian.activity;

import java.util.ArrayList;
import java.util.List;


import com.sssoft.Yundian.R;
import com.sssoft.Yundian.Dao.IncomeDao;
import com.sssoft.Yundian.Dao.IncomeSpinnerDao;

import com.sssoft.Yundian.bean.IncomeBean;
import com.sssoft.Yundian.utils.DateUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author 我的收入页
 *
 */
public class MyIncomeActivity extends Activity {
	// 后退键
	private ImageView ivBack;
	// 全部收入下拉表
	private List<String> listIncomeDate = new ArrayList<String>();
	// 支付方式下拉表
	private List<String> listPaymentMethod = new ArrayList<String>();
	private TextView tvListIncomeDate;
	// 下拉列表
	private Spinner spinnerIncomeDate;
	private Spinner spinnerPaymentMethod;
	private ArrayAdapter<String> adapterIncomeDate;
	private ArrayAdapter<String> adapterPaymentMethod;
	// 统计全部收入
	private TextView tvAllIncome;
	
	private IncomeDao incomeDao;
	private IncomeBean incomeBean;
	private DateUtil dateUtil;
	// 总收入
	private String allIncome;
	// 底部list视图
	private ListView lvButtom;

	private TextView tvOrdersNum;
	private TextView tvOrdersState;
	private TextView tvOrdersMethod;
	private TextView tvOrdersDate;
	private TextView tvOrdersActualPrice;

	private String ordNum;
	private String ordState;
	private String ordDate;
	private String ordMethod;
	private String ordTotalPrice;
	private String ordActualPrice;

	// 用于spinner点击时获得值
	private int spDate = 0;
	private int spPay = 0;

	private IncomeBean iBean;
	private BaseAdapter adapter;
	private List<IncomeBean> list;
	// 定义支付方式点击值
	private String str;

	private static final int REFRESH_CODE = 7;


	// private int currentPage = 1; // 默认在第一页
	// private static final int lineSize = 10; // 每次显示数
	// private int allRecorders = 0; // 全部记录数
	// private int pageSize = 1; // 默认共一页
	// private Aleph0 baseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_income);
		initView();

	}

	// 初始化
	public void initView() {
		incomeBean = new IncomeBean();
		dateUtil = new DateUtil();
		incomeDao = new IncomeDao(this);
		// 我的收入list
		list = incomeDao.queryAll();
		// 订单明细list
		allIncome = incomeDao.income_all();
		ivBack = (ImageView) findViewById(R.id.iv_back);
		lvButtom = (ListView) findViewById(R.id.accountLV);
		tvAllIncome = (TextView) findViewById(R.id.tv_AllIncome);
		tvAllIncome.setText(allIncome);
		// 顶部后退键
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v_back) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 顶部收入选择排序方式
		listIncomeDate.add("全部收入  ");
		listIncomeDate.add("今日收入");
		listIncomeDate.add("昨日收入");
		listIncomeDate.add("近一周收入");
		listPaymentMethod.add("全部支付");
		listPaymentMethod.add("现金");
		listPaymentMethod.add("支付宝");
		listPaymentMethod.add("微信");
		listPaymentMethod.add("银联卡");
		listPaymentMethod.add("储值卡");

		// 下拉排序表
		spinnerIncomeDate = (Spinner) findViewById(R.id.sp_income_date);
		spinnerPaymentMethod = (Spinner) findViewById(R.id.sp_payment_method);
		adapterIncomeDate = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listIncomeDate);
		adapterPaymentMethod = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listPaymentMethod);
		// 设置下拉菜单样式
		adapterIncomeDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterPaymentMethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// spinner适配器
		spinnerIncomeDate.setAdapter(adapterIncomeDate);
		spinnerPaymentMethod.setAdapter(adapterPaymentMethod);

		// 选择收入排序方式:按时间
		spinnerIncomeDate.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {// 选择item的选择点击监听事件
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				// 根据选择列表项进行排序sort
				spDate = arg2;
				// 获得排序后的数据
				incomeList(spDate, spPay, str);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				tvListIncomeDate.setText("Nothing");
			}
		});
		// 按支付方式排序
		spinnerPaymentMethod.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int method, long arg3) {
				// TODO Auto-generated method stub
				// 获得点击的item名字
				str = (String) spinnerPaymentMethod.getSelectedItem();
				// 得到点击值
				spPay = method;
				// 获得排序后的数据
				incomeList(spDate, spPay, str);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				tvListIncomeDate.setText("Nothing");
			}
		});

		// 定义适配器,存数据到listview
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = convertView != null ? convertView
						: View.inflate(getApplicationContext(), R.layout.listitem_myincome, null);
				tvOrdersNum = (TextView) view.findViewById(R.id.TV_OrdersNum);
				tvOrdersState = (TextView) view.findViewById(R.id.TV_OrdersState);
				tvOrdersMethod = (TextView) view.findViewById(R.id.TV_OrdersMethod);
				tvOrdersDate = (TextView) view.findViewById(R.id.TV_OrdersDate);
				tvOrdersActualPrice = (TextView) view.findViewById(R.id.TV_OrdersActualPrice);
				iBean = list.get(position);
				tvOrdersNum.setText(iBean.getOrders_number());
				tvOrdersState.setText(iBean.getOrders_state());
				tvOrdersMethod.setText(iBean.getOrders_method());
				tvOrdersDate.setText(iBean.getOrders_date().substring(0, 10));
				tvOrdersActualPrice.setText(iBean.getOrders_actual_price());
				return view;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see android.widget.Adapter#getCount()
			 */
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}
		};
		lvButtom.setAdapter(adapter);
		lvButtom.setOnItemClickListener(new MyOnItemClickListener());
		// 监听是否到底部
		// lvButtom.setOnScrollListener(new OnScrollListener() {
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// // TODO Auto-generated method stub
		// if (isBottom && (scrollState == OnScrollListener.SCROLL_STATE_IDLE))
		// {
		// rLayout.setVisibility(View.VISIBLE);
		// } else {
		// rLayout.setVisibility(View.GONE);
		// }
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem, int
		// visibleItemCount, int totalItemCount) {
		// // TODO Auto-generated method stub
		// isBottom = ((firstVisibleItem + visibleItemCount) == totalItemCount);
		// }
		// });
	}

//	public void Click(View v) {
//		switch (v.getId()) {
//		case R.id.rl: // listview到底部时加载加载数据
//
//			break;
//
//		default:
//			break;
//		}
//	}

	// 获取排序后的listview数据
	public void incomeList(int sortDate, int sortPay, String str) {
		// 获取今日日期
		String now_date = dateUtil.getnow_date();
		String yesterday_date = dateUtil.getyesterday_date(); // 昨日日期
		IncomeSpinnerDao iSpinnerDao = new IncomeSpinnerDao(MyIncomeActivity.this);
		if (spDate == 0 && sortPay == 0) {
			// 我的收入list
			list = incomeDao.queryAll();
			tvAllIncome.setText(allIncome);
		} else if (spDate == 0 && !(sortPay == 0)) {
			list = iSpinnerDao.all_pay_method(str);
			tvAllIncome.setText(iSpinnerDao.income_all());
		}
		// 今日
		else if (spDate == 1 && sortPay == 0) {
			// 得到今日收入数据
			list = iSpinnerDao.Income_Date(now_date);
			tvAllIncome.setText(iSpinnerDao.income_all());
		} else if (spDate == 1 && !(sortPay == 0)) {
			list = iSpinnerDao.pay_method(now_date, str);
			tvAllIncome.setText(iSpinnerDao.income_all());
		}
		// 昨日
		else if (spDate == 2 && sortPay == 0) {
			list = iSpinnerDao.Income_Date(yesterday_date);
			tvAllIncome.setText(iSpinnerDao.income_all());
		} else if (spDate == 2 && !(sortPay == 0)) {
			list = iSpinnerDao.pay_method(yesterday_date, str);
			tvAllIncome.setText(iSpinnerDao.income_all());
		}
		// 一周
		else if (spDate == 3 && sortPay == 0) {
			list = iSpinnerDao.incomeWeek();
			tvAllIncome.setText(""+iSpinnerDao.income_all());
		} else if (spDate == 3 && !(sortPay == 0)) {
			list = iSpinnerDao.week_Method(str);
			tvAllIncome.setText(iSpinnerDao.income_all());
		}
		lvButtom.setAdapter(adapter);
	}

	// listview点击跳转订单详情页
	public class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			// IncomeBean p = (IncomeBean) parent.getItemAtPosition(position);
			// 获取当前点击item的值，传入数据到收入详情页
			IncomeBean ib1 = list.get(position);
			ordNum = ib1.getOrders_number();
			ordState = ib1.getOrders_state();
			ordDate = ib1.getOrders_date();
			ordMethod = ib1.getOrders_method();
			ordTotalPrice = ib1.getOrders_total_price();
			ordActualPrice = ib1.getOrders_actual_price();
			Intent intent1 = new Intent();
			intent1.putExtra("OrdersNum", ordNum);
			intent1.putExtra("OrdersState", ordState);
			intent1.putExtra("OrdersDate", ordDate);
			intent1.putExtra("OrdersMethod", ordMethod);
			intent1.putExtra("OrdersTotalPrice", ordTotalPrice);
			intent1.putExtra("OrdersActualPrice", ordActualPrice);
			intent1.setClass(MyIncomeActivity.this, OrdersDetailsActivity.class);
			startActivityForResult(intent1, REFRESH_CODE);
		}
	}

	// 回调刷新listview
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REFRESH_CODE) {
			incomeList(spDate, spPay, str);
		}
	}

}
