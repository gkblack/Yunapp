package com.sssoft.Yundian.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.sssoft.Yundian.R;
import com.sssoft.Yundian.Dao.IncomeDao;
import com.sssoft.Yundian.bean.IncomeBean;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 商品详情页
 * 
 * @author JKCoCo
 *
 */
public class OrdersDetailsActivity extends Activity implements OnClickListener {
	private List<IncomeBean> detailsList;
	private IncomeDao iDao;
	private TextView tvOrdersNum;
	private TextView tvOrdersState;
	private TextView tvOrdersDate;
	private TextView tvOrdersMethod;
	private TextView tvOrdersTotalPrice;
	private TextView tvOrdersActualPrice;
	// 后退
	private ImageView ivBack;
	// 订单商品详情list
	private ListView lvGoodsDetails;

	private Button btPrintTicket;
	private Button btRefunds;

	private TextView tvGoodsName;
	// 商品单价
	private TextView tvGoodsPrice;
	private TextView tvGoodsNum;
	private TextView tvGoodsTotalPrice;

	private String ordersNum;
	private String ordersState;
	private String ordersDate;
	private String ordersMethod;
	private String ordersTotalPrice;
	private String ordersActualPrice;

	private boolean orderAlter;
	private static final int REFRESH_CODE = 7; // 刷新

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_orders_details);
		initView();
		ivBack = (ImageView) findViewById(R.id.IV_Back);
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// 返回时刷新上一个我的收入页的listview
				setResult(1, intent);
				finish();
			}
		});

	}
	public void initView(){
		iDao = new IncomeDao(this);
		lvGoodsDetails = (ListView) findViewById(R.id.lv_goods_details);
		// 获取MyIncome所传值
		Intent intent = getIntent();
		// 订单号
		ordersNum = intent.getStringExtra("OrdersNum");

		ordersState = intent.getStringExtra("OrdersState");
		ordersDate = intent.getStringExtra("OrdersDate");
		ordersMethod = intent.getStringExtra("OrdersMethod");
		ordersTotalPrice = intent.getStringExtra("OrdersTotalPrice");
		ordersActualPrice = intent.getStringExtra("OrdersActualPrice");
		tvOrdersNum = (TextView) findViewById(R.id.tv_orders_number);
		tvOrdersState = (TextView) findViewById(R.id.tv_orders_state);
		tvOrdersDate = (TextView) findViewById(R.id.tv_orders_date);
		tvOrdersMethod = (TextView) findViewById(R.id.tv_orders_method);
		tvOrdersTotalPrice = (TextView) findViewById(R.id.tv_orders_total_price);
		tvOrdersActualPrice = (TextView) findViewById(R.id.tv_orders_actual_price);
		tvOrdersNum.setText(ordersNum);
		tvOrdersState.setText(ordersState);
		tvOrdersDate.setText(ordersDate);
		tvOrdersMethod.setText(ordersMethod);
		tvOrdersTotalPrice.setText(ordersTotalPrice);
		tvOrdersActualPrice.setText(ordersActualPrice);
		// 根据订单号查询数据库,获取商品详情
		detailsList = iDao.list_goods_details(ordersNum);
		// 打印小票
		btPrintTicket = (Button) findViewById(R.id.bt_print_ticket);
		btPrintTicket.setOnClickListener(this);
		// 退款
		btRefunds = (Button) findViewById(R.id.bt_refunds);
		btRefunds.setOnClickListener(this);
		// 商品详情底部listview适配器
		BaseAdapter bAdapter = new BaseAdapter() {
			@Override
			public int getCount() {
				return detailsList.size();
			}

			@Override
			public Object getItem(int pos) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int pos) {
				// TODO Auto-generated method stub
				return pos;
			}

			@Override
			public View getView(int pos, View goods_view, ViewGroup arg2) {
				// TODO Auto-generated method stub
				View detail_view = goods_view != null ? goods_view
						: View.inflate(getApplicationContext(), R.layout.listview_details_orders, null);
				tvGoodsName = (TextView) detail_view.findViewById(R.id.tv_goods_name);
				tvGoodsPrice = (TextView) detail_view.findViewById(R.id.tv_goods_price);
				tvGoodsNum = (TextView) detail_view.findViewById(R.id.tv_goods_num);
				tvGoodsTotalPrice = (TextView) detail_view.findViewById(R.id.tv_goods_total_price);
				// 获取数据库查询商品详情
				IncomeBean dlist = detailsList.get(pos);
				tvGoodsName.setText(dlist.getGoods_name());
				tvGoodsPrice.setText(dlist.getGoods_price());
				tvGoodsNum.setText(dlist.getGoods_details_num());
				tvGoodsTotalPrice.setText(dlist.getGoods_details_total_price());
				return detail_view;
			}
		};
		lvGoodsDetails.setAdapter(bAdapter);
	}

	// 打印小票和退款button点击事件
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_refunds:
			// 改订单为退款
			orderAlter = iDao.order_alter(ordersNum);
			if (orderAlter) {
				tvOrdersState.setText("已退款");
				Toast.makeText(OrdersDetailsActivity.this, "退款成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(OrdersDetailsActivity.this, "退款失败", Toast.LENGTH_SHORT).show();
			}
//			预留退款接口调用
//			SimpleDateFormat format = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
//			String TxnReqTime = format.format(new Date());
//			//TxnType交易类型码103为退款  RefundTxnNo退款流水号 RefundAmt退款金额 CurrencyCode货币代码
//			Uri uri = Uri.parse("sssoft//sssoft.uri.activity/payTrans?TxnType=103&RefundTxnNo=11111111&"
//					+ "RefundAmt=0.01&CurrencyCode=156&TxnReqTime="+TxnReqTime);
//			Intent intent = new Intent("android.sssoft.schemeurl.activity");  
//			intent.setData(uri);  
//			startActivityForResult(intent, 10);

			break;
		case R.id.bt_print_ticket:
			break;
		}
	}
	
	//获取交易返回的数据
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if(requestCode==10){
//			//获取返回数据
//			    Bundle bundle = data.getExtras();
//				String RespCode = bundle.getString("RespCode");		//返回码，是否成功
//				String RespDesc = bundle.getString("RespDesc");
//				String AuthCode = bundle.getString("AuthCode");
//				String TxnAnsTime = bundle.getString("TxnAnsTime");
//				String ChannelID = bundle.getString("ChannelID");
//				String PlatformTxnNo = bundle.getString("PlatformTxnNo");
//				String ChannelTxnNo = bundle.getString("ChannelTxnNo");
//				Double TotalAmt = bundle.getDouble("TotalAmt");
//				Double IncomeAmt = bundle.getDouble("IncomeAmt");
//				Double InvoiceAmt = bundle.getDouble("InvoiceAmt");
//				Double PointAmt = bundle.getDouble("PointAmt");
//				Double MerchantDisctAmt = bundle.getDouble("MerchantDisctAmt");
//				Double ChannelDisctAmt = bundle.getDouble("ChannelDisctAmt");
//				String MerchantName = bundle.getString("MerchantName");
//				String PayerID = bundle.getString("PayerID");
//		}
//	}


	// 返回键监听
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			setResult(REFRESH_CODE, intent);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
