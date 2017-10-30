package com.sssoft.Yundian.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.sssoft.Yundian.R;
import com.sssoft.Yundian.Dao.GoodsDao;
import com.sssoft.Yundian.Dao.PordersDao;
import com.sssoft.Yundian.activity.MainActivity.CardAdapter;
import com.sssoft.Yundian.bean.Goods;
import com.sssoft.Yundian.bean.Porders;
import com.sssoft.Yundian.view.SideslipListView;
import com.sssoft.Yundian.view.SwipeMenuLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * com.sssoft.Yundian.activity 挂单显示页面
 * 
 * @author Jiang 2017年10月25日下午1:59:49
 */
public class PendingOrderActivity extends Activity {
	private PordersDao pDao;
	private List<Porders> pList;

	private ListView lvPorder;
	private PorderAdapter pAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pending_order);

		setListView();

	}

	private void setListView() {
		// 设置listview
		pDao = new PordersDao(PendingOrderActivity.this);
		pList = pDao.queryall();
		Log.d("d", pList.toString());
		// Toast.makeText(this, pList.toString(), Toast.LENGTH_SHORT).show();
		pAdapter = new PorderAdapter(pList, PendingOrderActivity.this);
		lvPorder = (ListView) findViewById(R.id.lv_porder);

		// lvPorder.setOnItemClickListener(new itemClickListener());
		lvPorder.setAdapter(pAdapter);

	}

	// public class itemClickListener implements OnItemClickListener {
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	// {
	// Log.i("ss", "11");
	// // 显示listview中的listview
	//
	// ListView lvDetail = (ListView) pAdapter.getItem(arg2);
	// if (lvDetail.getVisibility() == View.GONE) {
	// lvDetail.setVisibility(View.VISIBLE);
	// } else {
	// lvDetail.setVisibility(View.GONE);
	// }
	//
	// }
	// }

	// 返回键
	public void toback(View v) {
		finish();
	}

	public class PorderAdapter extends BaseAdapter {
		private List<Porders> pList;
		private Context mcontext;
		private GoodsDao gDao;
		private PordersDao pDao;
		private HashMap<Integer, View> mHashMap = new HashMap<Integer, View>();
		private HashMap<Integer, ListView> mListView = new HashMap<Integer, ListView>();

		public PorderAdapter(List<Porders> pList, Context context) {
			// TODO Auto-generated constructor stub
			this.pList = pList;
			this.mcontext = context;
			this.gDao = new GoodsDao(context);
			this.pDao = new PordersDao(context);
		}

		@Override
		public int getCount() {
			return pList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return pList.get(arg0);
			// return mListView.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// final View item;
			// if (mHashMap.get(position) == null) {
			// item = View.inflate(mcontext, R.layout.item_pending_sp, null);
			// mHashMap.put(position, item);
			// } else {
			// item = mHashMap.get(position);
			// }
			final View item = convertView != null ? convertView
					: View.inflate(mcontext, R.layout.item_pending_sp, null);
			TextView date = (TextView) item.findViewById(R.id.tv_porderdate);
			TextView num = (TextView) item.findViewById(R.id.tv_pordernum);
			ImageView ivRecover = (ImageView) item.findViewById(R.id.iv_recovers);
			TextView delete = (TextView) item.findViewById(R.id.txtv_delete);
//			ImageView showDetail = (ImageView) item.findViewById(R.id.iv_show_detail);
			LinearLayout showGoods = (LinearLayout) item.findViewById(R.id.ll_show_detail);
			// 设置挂单值
			final Porders order = pList.get(position);
			date.setText(order.getPorders_date() + "");
			num.setText(order.getPorders_num() + "");

			final int pos = position;
			showGoods.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					List<Goods> goods = gDao.findMarkGood(order.getPorders_id(),
							pDao.findGoodId(order.getPorders_id()));
					ConsumAdapter cAdapter = new ConsumAdapter(goods, mcontext);
					createPopUpWindow(cAdapter);
					// if (mListView.get(pos) == null) {
					// lvDetail = (ListView) item.findViewById(R.id.lv_pdetails);
					// mListView.put(pos, lvDetail);
					// List<Goods> goods = gDao.findMarkGood(order.getPorders_id(),
					// pDao.findGoodId(order.getPorders_id()));
					// ConsumAdapter cAdapter = new ConsumAdapter(goods, item.getContext());
					// lvDetail.setAdapter(cAdapter);
					// setListViewHeightBasedOnChildren(lvDetail);
					// } else {
					// lvDetail = mListView.get(pos);
					// }
					//
					// if (lvDetail.getVisibility() == View.GONE) {
					// lvDetail.setVisibility(View.VISIBLE);
					// } else {
					// lvDetail.setVisibility(View.GONE);
					// }
				}
			});

			// 恢复账单
			ivRecover.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(PendingOrderActivity.this, ConsumptionActivity.class);
					intent.putExtra("ordersId", order.getPorders_id());
					startActivity(intent);
				}
			});

			// 删除
			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pDao.delete(order.getPorders_id());
					pList.remove(pos);
					((SwipeMenuLayout) item).quickClose();
					notifyDataSetChanged();
					// lvPorder.turnNormal();
				}
			});

			return item;
		}

		// 嵌套的listView设置完全显示
		public void setListViewHeightBasedOnChildren(ListView listView) {
			// 获取适配器
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null) {
				// pre-condition
				return;
			}
			int totalHeight = 0;
			// 对所有的item高度求和
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			// 设置高度，加上分割线的高度
			params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			listView.setLayoutParams(params);
		}

		public void createPopUpWindow(ConsumAdapter cAdapter) {
			View contentView = LayoutInflater.from(PendingOrderActivity.this).inflate(R.layout.show_porder_goods, null);
			PopupWindow popWnd = new PopupWindow(PendingOrderActivity.this);
			popWnd.setContentView(contentView);
			popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
			popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
			popWnd.setFocusable(true);
			popWnd.setOutsideTouchable(true);
			popWnd.setBackgroundDrawable(new BitmapDrawable(getResources()));
			popWnd.setAnimationStyle(R.style.anim_menu_bottombar);
			View rootview = LayoutInflater.from(PendingOrderActivity.this).inflate(R.layout.activity_pending_order, null);
			popWnd.showAtLocation(rootview, Gravity.BOTTOM | Gravity.START, 0, 0);
			ListView lvDetail = (ListView) contentView.findViewById(R.id.lv_show_goods);
			lvDetail.setAdapter(cAdapter);
			lvDetail.setItemsCanFocus(false);
		}

	}
}
