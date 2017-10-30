package com.sssoft.Yundian.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.sssoft.Yundian.R;
import com.sssoft.Yundian.Dao.GoodsDao;
import com.sssoft.Yundian.Dao.IncomeDao;
import com.sssoft.Yundian.Dao.PordersDao;
import com.sssoft.Yundian.bean.Goods;
import com.sssoft.Yundian.bean.IncomeBean;
import com.sssoft.Yundian.utils.Arith;
import com.sssoft.Yundian.utils.Constant;
import com.sssoft.Yundian.utils.FormatUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 收款页面
 * 
 * @author Jiang 2017.10.23
 */
public class ConsumptionActivity extends Activity {
	private List<Goods> goods;
	private HashMap<Integer, Integer> pGoods;
	private GoodsDao gDao;
	private PordersDao pDao;
	private ConsumAdapter cAdapter;

	private long ordersId;

	private ListView lvShowCus;
	private TextView tvMoney;

	private ImageView cash;
	private ImageView wxZfb;
	private ImageView bankCard;

	private String payMethod = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_consumption);

		initView();
//		xPosPayment();
	}

	public void initView() {
		lvShowCus = (ListView) findViewById(R.id.lv_show_cus);
		tvMoney = (TextView) findViewById(R.id.tv_money);

		cash = (ImageView) findViewById(R.id.XJ);
		wxZfb = (ImageView) findViewById(R.id.WXZFB);
		bankCard = (ImageView) findViewById(R.id.BANK);

		Intent intent = getIntent();
		// 获取挂单号，获取商品信息
		ordersId = intent.getLongExtra("ordersId", -1);
		if (ordersId != -1) {
			gDao = new GoodsDao(ConsumptionActivity.this);
			pDao = new PordersDao(ConsumptionActivity.this);
			pGoods = pDao.findGoodId(ordersId);
			goods = gDao.findMarkGood(ordersId, pGoods);
			cAdapter = new ConsumAdapter(goods,ConsumptionActivity.this);
			lvShowCus.setAdapter(cAdapter);
			tvMoney.setText(FormatUtil.double2StrAmt(totalMoney()) + "");
		} else {
			Toast.makeText(ConsumptionActivity.this, R.string.Consumption_activity_message01, Toast.LENGTH_SHORT)
					.show();
		}


	}

	// 调用xPos api
	public void xPosPayment() {
		// 现金支付
		cash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String TxnReqTime = format.format(new Date());
				Uri uri = Uri.parse("sssoft://sssoft.uri.activity/payTrans?TxnType=" + Constant.TxnType.TXN_TYPE_SALE
						+ "&PayMode=" + Constant.PayMode.CASH + "&MerchantTxnNo=&TxnAmt=" + totalMoney()
						+ "&CurrencyCode=" + Constant.CurrencyCode + "&TxnReqTime=" + TxnReqTime + "&PermitDisctAmt=0");
				Intent intent = new Intent("android:sssoft.schemurl.activity");
				intent.setData(uri);
				startActivityForResult(intent, 10);
			}
		});

		// 银行卡支付
		bankCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String TxnReqTime = format.format(new Date());
				Uri uri = Uri.parse("sssoft://sssoft.uri.activity/payTrans?TxnType=" + Constant.TxnType.TXN_TYPE_SALE
						+ "&PayMode=" + Constant.PayMode.MOBILE + "&MerchantTxnNo=&TxnAmt=" + totalMoney()
						+ "&CurrencyCode=" + Constant.CurrencyCode + "&TxnReqTime=" + TxnReqTime + "&PermitDisctAmt=0");
				Intent intent = new Intent("android:sssoft.schemurl.activity");
				intent.setData(uri);
				startActivityForResult(intent, 10);
			}
		});

		// 微信支付宝支付
		wxZfb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String TxnReqTime = format.format(new Date());
				Uri uri = Uri.parse("sssoft://sssoft.uri.activity/payTrans?TxnType=" + Constant.TxnType.TXN_TYPE_SALE
						+ "&PayMode=" + Constant.PayMode.BANK + "&MerchantTxnNo=&TxnAmt=" + totalMoney()
						+ "&CurrencyCode=" + Constant.CurrencyCode + "&TxnReqTime=" + TxnReqTime + "&PermitDisctAmt=0");
				Intent intent = new Intent("android:sssoft.schemurl.activity");
				intent.setData(uri);
				startActivityForResult(intent, 10);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 10) {
			Bundle bundle = data.getExtras();
			// 返回码
			String ResCode = bundle.getString("RespCode");
			// 返回描述
			String RespDesc = bundle.getString("RespDesc");
			// 授权码 银行卡支付返回
			String AuthCode = bundle.getString("AuthCode");
			// 受理时间
			String TxnAnsTime = bundle.getString("TxnAnsTime");
			// 支付渠道ID
			String ChannelID = bundle.getString("ChannelID");
			// 平台交易流水号
			String PlatformTxnNo = bundle.getString("PlatformTxnNo");
			// 收款总金额
			Double TotalAmt = bundle.getDouble("TotalAmt");
			// 商户进账金额
			Double IncomeAmt = bundle.getDouble("IncomeAmt");
			// 发票金额，用户实际所付
			Double InvoiceAmt = bundle.getDouble("InvoiceAmt");
			// 受理渠道返回的积分金额
			Double PointAmt = bundle.getDouble("PointAmt");
			// 商户折扣金额
			Double MerchantDisctAmt = bundle.getDouble("MerchandisctAmt");
			// 渠道折扣金额
			Double ChannelDisctAmt = bundle.getDouble("ChannelDisctAmt");
			// 受理渠道返回的商户名称
			String MerchantName = bundle.getString("MerchanName");
			// 支付帐号
			String PayerID = bundle.getString("PayerID");

			if (ResCode.equals(Constant.Rc.SUCC)) {
				// 成功
				switch (Integer.parseInt(ChannelID)) {
				case 2:
					payMethod = "微信";
					break;
				case 6:
					payMethod = "支付宝";
					break;
				case 10:
					payMethod = "现金";
					break;
				case 11:
					payMethod = "银行卡";
					break;
				}
				// 插入本地
//				IncomeBean income = new IncomeBean(ordersId, PlatformTxnNo, "已支付", TxnAnsTime, payMethod, TotalAmt+"", InvoiceAmt+"", PayerID);
//				IncomeDao iDao = new IncomeDao(ConsumptionActivity.this);
//				iDao.insert(income,goods);
			} else {

			}
		}
	}

	// 返回键
	public void toback(View v) {
		finish();
	}

	// 计算总共价钱
	public double totalMoney() {
		double sum = 0;
		for (int i = 0; i < goods.size(); i++) {
			if (goods.get(i).isSelected()) {
				double onePrice = Arith.mul(Double.parseDouble(goods.get(i).getGoods_price()),
						goods.get(i).getSelectedNum());
				sum = Arith.add(onePrice, sum);
			}
		}

		return sum;
	}

//	// 自定义适配器
//	class ConsumAdapter extends BaseAdapter {
//
//		@Override
//		public int getCount() {
//
//			return goods.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return goods.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// 获取视图
//			View item = convertView != null ? convertView
//					: View.inflate(getApplicationContext(), R.layout.show_cusumption, null);
//
//			TextView tvGoodName = (TextView) item.findViewById(R.id.tv_goodname);
//			TextView tvGoodPrice = (TextView) item.findViewById(R.id.tv_goodprice);
//			TextView tvGoodNum = (TextView) item.findViewById(R.id.tv_goodnum);
//			TextView tvGoodSum = (TextView) item.findViewById(R.id.tv_goodsum);
//
//			final Goods a = goods.get(position);
//
//			tvGoodName.setText(a.getGoods_name() + "");
//			tvGoodPrice.setText(a.getGoods_price() + "");
//			tvGoodNum.setText(a.getSelectedNum() + "");
//			double total = Arith.mul(Double.parseDouble(a.getGoods_price()), a.getSelectedNum());
//			tvGoodSum.setText(FormatUtil.double2StrAmt(total) + "");
//			return item;
//		}
//	}
}
