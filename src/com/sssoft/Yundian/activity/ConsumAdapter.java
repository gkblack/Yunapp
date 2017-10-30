package com.sssoft.Yundian.activity;

import java.util.List;

import com.sssoft.Yundian.R;
import com.sssoft.Yundian.bean.Goods;
import com.sssoft.Yundian.utils.Arith;
import com.sssoft.Yundian.utils.FormatUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

// 自定义适配器
/**
 * com.sssoft.Yundian.activity
 * @author Jiang
 * 2017年10月25日下午4:32:26
 */
class ConsumAdapter extends BaseAdapter {
	private List<Goods> goods;
	private Context context;
	public ConsumAdapter(List<Goods> goods, Context context) {
		// TODO Auto-generated constructor stub
		this.goods = goods;
		this.context = context;
	}
	@Override
	public int getCount() {

		return goods.size();
	}

	@Override
	public Object getItem(int position) {
		return goods.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 获取视图
		View item = convertView != null ? convertView
				: View.inflate(context, R.layout.show_cusumption, null);

		TextView tvGoodName = (TextView) item.findViewById(R.id.tv_goodname);
		TextView tvGoodPrice = (TextView) item.findViewById(R.id.tv_goodprice);
		TextView tvGoodNum = (TextView) item.findViewById(R.id.tv_goodnum);
		TextView tvGoodSum = (TextView) item.findViewById(R.id.tv_goodsum);

		final Goods a = goods.get(position);

		tvGoodName.setText(a.getGoods_name() + "");
		tvGoodPrice.setText(a.getGoods_price() + "");
		tvGoodNum.setText(a.getSelectedNum() + "");
		double total = Arith.mul(Double.parseDouble(a.getGoods_price()), a.getSelectedNum());
		tvGoodSum.setText(FormatUtil.double2StrAmt(total) + "");
		return item;
	}
}