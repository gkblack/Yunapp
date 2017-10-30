package com.sssoft.Yundian.activity;

import java.util.ArrayList;

import java.util.List;

import com.readystatesoftware.viewbadger.BadgeView;
import com.sssoft.Yundian.R;
import com.sssoft.Yundian.bean.Goods;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * PagerAdapter 滑动菜单适配器
 * 
 * @author Jiang 2017.10.11
 *
 */
@SuppressLint("NewApi")
public class MyAdapter extends PagerAdapter {
	private ImageView ivIcon1;
	private ImageView ivIcon2;
	private ImageView ivIcon3;
	private ImageView ivIcon4;
	private ImageView ivIcon5;
	private ImageView ivIcon6;
	private ImageView ivIcon7;
	private ImageView ivIcon8;
	private ImageView ivIcon9;
	private TextView tvName1;
	private TextView tvName2;
	private TextView tvName3;
	private TextView tvName4;
	private TextView tvName5;
	private TextView tvName6;
	private TextView tvName7;
	private TextView tvName8;
	private TextView tvName9;
	private TextView tvUntil1;
	private TextView tvUntil2;
	private TextView tvUntil3;
	private TextView tvUntil4;
	private TextView tvUntil5;
	private TextView tvUntil6;
	private TextView tvUntil7;
	private TextView tvUntil8;
	private TextView tvUntil9;
	private TextView tvPrice1;
	private TextView tvPrice2;
	private TextView tvPrice3;
	private TextView tvPrice4;
	private TextView tvPrice5;
	private TextView tvPrice6;
	private TextView tvPrice7;
	private TextView tvPrice8;
	private TextView tvPrice9;
	private LinearLayout llItem1;
	private LinearLayout llItem2;
	private LinearLayout llItem3;
	private LinearLayout llItem4;
	private LinearLayout llItem5;
	private LinearLayout llItem6;
	private LinearLayout llItem7;
	private LinearLayout llItem8;
	private LinearLayout llItem9;

	private Context context;
	private List<View> mListViews;
	private List<Goods> goods;
	private int type;

	int pages; // 总页数
	private View pageView;
	private View.OnClickListener onClickListener;
	private List<ImageView> iconList;
	private List<TextView> nameList;
	private List<TextView> untilList;
	private List<TextView> priceList;
	private List<LinearLayout> itemList;
	private int[] icon = { R.id.iv_icon1, R.id.iv_icon2, R.id.iv_icon3, R.id.iv_icon4, R.id.iv_icon5, R.id.iv_icon6,
			R.id.iv_icon7, R.id.iv_icon8, R.id.iv_icon9 };

	// 主界面
	public MyAdapter(Context context, List<Goods> goods, View.OnClickListener onClickListener) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.goods = goods;
		this.onClickListener = onClickListener;
		mListViews = new ArrayList<View>();
		initView(goods, 0);

	}

	// 商品管理
	public MyAdapter(Context context, List<Goods> goods, View.OnClickListener onClickListener, int type) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.goods = goods;
		this.type = type;
		this.onClickListener = onClickListener;
		mListViews = new ArrayList<View>();
		initView(goods, type);

	}

	public void initView(List<Goods> goods, int type) {
		// 计算ViewPager的页数
		mListViews.clear();
		pages = goods.size() / 9;
		if (goods.size() % 9 != 0) {
			pages = pages + 1;

		}

		iconList = new ArrayList<ImageView>();
		nameList = new ArrayList<TextView>();
		untilList = new ArrayList<TextView>();
		priceList = new ArrayList<TextView>();
		itemList = new ArrayList<LinearLayout>();
		for (int i = 0; i < pages; i++) {
			pageView = View.inflate(context, R.layout.show_goods, null);
			Bindid(this, pageView);
			// 根据页码来填充数据
			if (type == 0) {
				// 主界面数据填充
				setMainPagerViewData(i);
			} else {
				// 商品管理数据填充
				setPagerViewData(i);
			}
			mListViews.add(pageView);
			initListener();
		}
	}

	public void setMainPagerViewData(int pageNum) {
		for (int i = 0; i < 9; i++) {
			if (goods.size() > pageNum * 9 + i) {
				int temp = pageNum * 9 + i;
				Goods good = goods.get(temp);
				itemList.get(i).setVisibility(View.VISIBLE);

				if (good.getGoods_img().equals("")) {
					iconList.get(i).setBackgroundResource(R.drawable.shopping);

				} else {
					Bitmap bt = BitmapFactory.decodeFile(good.getGoods_img());
					if (bt != null) {
						Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
						iconList.get(i).setImageDrawable(drawable);
					} else {
						// 如果SD里面没有则默认图片
						iconList.get(i).setBackgroundResource(R.drawable.shopping);

					}
				}

				nameList.get(i).setText(good.getGoods_name());
				untilList.get(i).setText(good.getGoods_spec());
				priceList.get(i).setText("￥ \t" + good.getGoods_price() + "/" + good.getGoods_units());
				if (good.isSelected()) {
					View target = pageView.findViewById(icon[i]);
					BadgeView badge = new BadgeView(context, target);
					// badge.setTextColor(Color.YELLOW);
					badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
					badge.setBadgeMargin(-1);
					badge.setText(good.getSelectedNum()+"");
					badge.show();
				}

			} else {
				itemList.get(i).setVisibility(View.INVISIBLE);
			}
		}
	}

	public void setPagerViewData(int pageNum) {
		for (int i = 0; i < 9; i++) {
			if (goods.size() > pageNum * 9 + i) {
				int temp = pageNum * 9 + i;
				Goods good = goods.get(temp);
				itemList.get(i).setVisibility(View.VISIBLE);
				if (good.getGoods_img().equals("")) {
					iconList.get(i).setBackgroundResource(R.drawable.shopping);

				} else {
					Bitmap bt = BitmapFactory.decodeFile(good.getGoods_img());
					if (bt != null) {
						Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
						iconList.get(i).setImageDrawable(drawable);
					} else {
						// 如果SD里面没有则默认图片
						iconList.get(i).setBackgroundResource(R.drawable.shopping);

					}
				}
				nameList.get(i).setText(good.getGoods_name());
				untilList.get(i).setText("销量：\t" + good.getGoods_sales());
				priceList.get(i).setText("");
				View target = pageView.findViewById(icon[i]);
				BadgeView badge = new BadgeView(context, target);
				// badge.setTextColor(Color.YELLOW);
				badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
				badge.setBadgeMargin(-1);
				if (good.isSelected()) {
					badge.setBackgroundResource(R.drawable.ok);

				} else {
					badge.setBackgroundResource(R.drawable.noselect);
				}
				badge.show();
			} else {
				itemList.get(i).setVisibility(View.INVISIBLE);
			}
		}
	}

	public void Bindid(MyAdapter myAdapter, View pageView) {
		iconList.clear();
		nameList.clear();
		untilList.clear();
		priceList.clear();
		itemList.clear();
		ivIcon1 = (ImageView) pageView.findViewById(R.id.iv_icon1);
		ivIcon2 = (ImageView) pageView.findViewById(R.id.iv_icon2);
		ivIcon3 = (ImageView) pageView.findViewById(R.id.iv_icon3);
		ivIcon4 = (ImageView) pageView.findViewById(R.id.iv_icon4);
		ivIcon5 = (ImageView) pageView.findViewById(R.id.iv_icon5);
		ivIcon6 = (ImageView) pageView.findViewById(R.id.iv_icon6);
		ivIcon7 = (ImageView) pageView.findViewById(R.id.iv_icon7);
		ivIcon8 = (ImageView) pageView.findViewById(R.id.iv_icon8);
		ivIcon9 = (ImageView) pageView.findViewById(R.id.iv_icon9);
		tvName1 = (TextView) pageView.findViewById(R.id.tv_name1);
		tvName2 = (TextView) pageView.findViewById(R.id.tv_name2);
		tvName3 = (TextView) pageView.findViewById(R.id.tv_name3);
		tvName4 = (TextView) pageView.findViewById(R.id.tv_name4);
		tvName5 = (TextView) pageView.findViewById(R.id.tv_name5);
		tvName6 = (TextView) pageView.findViewById(R.id.tv_name6);
		tvName7 = (TextView) pageView.findViewById(R.id.tv_name7);
		tvName8 = (TextView) pageView.findViewById(R.id.tv_name8);
		tvName9 = (TextView) pageView.findViewById(R.id.tv_name9);
		tvUntil1 = (TextView) pageView.findViewById(R.id.tv_until1);
		tvUntil2 = (TextView) pageView.findViewById(R.id.tv_until2);
		tvUntil3 = (TextView) pageView.findViewById(R.id.tv_until3);
		tvUntil4 = (TextView) pageView.findViewById(R.id.tv_until4);
		tvUntil5 = (TextView) pageView.findViewById(R.id.tv_until5);
		tvUntil6 = (TextView) pageView.findViewById(R.id.tv_until6);
		tvUntil7 = (TextView) pageView.findViewById(R.id.tv_until7);
		tvUntil8 = (TextView) pageView.findViewById(R.id.tv_until8);
		tvUntil9 = (TextView) pageView.findViewById(R.id.tv_until9);
		tvPrice1 = (TextView) pageView.findViewById(R.id.tv_price1);
		tvPrice2 = (TextView) pageView.findViewById(R.id.tv_price2);
		tvPrice3 = (TextView) pageView.findViewById(R.id.tv_price3);
		tvPrice4 = (TextView) pageView.findViewById(R.id.tv_price4);
		tvPrice5 = (TextView) pageView.findViewById(R.id.tv_price5);
		tvPrice6 = (TextView) pageView.findViewById(R.id.tv_price6);
		tvPrice7 = (TextView) pageView.findViewById(R.id.tv_price7);
		tvPrice8 = (TextView) pageView.findViewById(R.id.tv_price8);
		tvPrice9 = (TextView) pageView.findViewById(R.id.tv_price9);
		llItem1 = (LinearLayout) pageView.findViewById(R.id.ll_item1);
		llItem2 = (LinearLayout) pageView.findViewById(R.id.ll_item2);
		llItem3 = (LinearLayout) pageView.findViewById(R.id.ll_item3);
		llItem4 = (LinearLayout) pageView.findViewById(R.id.ll_item4);
		llItem5 = (LinearLayout) pageView.findViewById(R.id.ll_item5);
		llItem6 = (LinearLayout) pageView.findViewById(R.id.ll_item6);
		llItem7 = (LinearLayout) pageView.findViewById(R.id.ll_item7);
		llItem8 = (LinearLayout) pageView.findViewById(R.id.ll_item8);
		llItem9 = (LinearLayout) pageView.findViewById(R.id.ll_item9);
		iconList.add(ivIcon1);
		iconList.add(ivIcon2);
		iconList.add(ivIcon3);
		iconList.add(ivIcon4);
		iconList.add(ivIcon5);
		iconList.add(ivIcon6);
		iconList.add(ivIcon7);
		iconList.add(ivIcon8);
		iconList.add(ivIcon9);
		nameList.add(tvName1);
		nameList.add(tvName2);
		nameList.add(tvName3);
		nameList.add(tvName4);
		nameList.add(tvName5);
		nameList.add(tvName6);
		nameList.add(tvName7);
		nameList.add(tvName8);
		nameList.add(tvName9);
		untilList.add(tvUntil1);
		untilList.add(tvUntil2);
		untilList.add(tvUntil3);
		untilList.add(tvUntil4);
		untilList.add(tvUntil5);
		untilList.add(tvUntil6);
		untilList.add(tvUntil7);
		untilList.add(tvUntil8);
		untilList.add(tvUntil9);
		priceList.add(tvPrice1);
		priceList.add(tvPrice2);
		priceList.add(tvPrice3);
		priceList.add(tvPrice4);
		priceList.add(tvPrice5);
		priceList.add(tvPrice6);
		priceList.add(tvPrice7);
		priceList.add(tvPrice8);
		priceList.add(tvPrice9);
		itemList.add(llItem1);
		itemList.add(llItem2);
		itemList.add(llItem3);
		itemList.add(llItem4);
		itemList.add(llItem5);
		itemList.add(llItem6);
		itemList.add(llItem7);
		itemList.add(llItem8);
		itemList.add(llItem9);

	}

	private void initListener() {
		llItem1.setOnClickListener(onClickListener);
		llItem2.setOnClickListener(onClickListener);
		llItem3.setOnClickListener(onClickListener);
		llItem4.setOnClickListener(onClickListener);
		llItem5.setOnClickListener(onClickListener);
		llItem6.setOnClickListener(onClickListener);
		llItem7.setOnClickListener(onClickListener);
		llItem8.setOnClickListener(onClickListener);
		llItem9.setOnClickListener(onClickListener);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView((View) object);// 删除页卡
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// 这个方法用来实例化页
		container.addView(mListViews.get(position), 0);// 添加页卡
		return mListViews.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pages;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	/*
	 * 选中
	 */

	public void setIconAlpha(boolean isSelected, ImageView imageView) {
		if (isSelected) {
			imageView.setAlpha(1.0f);
		} else {
			imageView.setAlpha(0.4f);
		}
	}

	public void refresh(List<Goods> goods, int type) {
		initView(goods, type);
		notifyDataSetChanged();
	}

	/**
	 * 使Adapter能够刷新布局
	 */
	private int mChildCount = 0;

	@Override
	public void notifyDataSetChanged() {
		mChildCount = getCount();
		super.notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		if (mChildCount > 0) {
			mChildCount--;
			return POSITION_NONE;
		}
		return super.getItemPosition(object);
	}

}
