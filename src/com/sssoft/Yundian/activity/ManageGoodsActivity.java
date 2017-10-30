package com.sssoft.Yundian.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.sssoft.Yundian.R;
import com.sssoft.Yundian.Dao.GoodsDao;
import com.sssoft.Yundian.Dao.TypeDao;
import com.sssoft.Yundian.bean.Goods;
import com.sssoft.Yundian.view.CustomViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Window;

/**
 * 商品管理页面Activity
 * 
 * @author Jiang 2017.10.17
 *
 */
public class ManageGoodsActivity extends Activity implements View.OnClickListener {

	private List<Goods> goods;
	private MyAdapter mAdapter;
	private GoodsDao gDao;

	private String[] otherList = { "综合", "销量", "上新" };
	private HashMap<Integer, Goods> hashGood;
	private List<ImageView> dots = new ArrayList<ImageView>();
	private boolean FLAG = true;

	private LinearLayout llDots;
	private CustomViewPager pvGoods;
	private TextView tvAdd;
	private TextView tvEdit;
	private Spinner spinner1;
	private Spinner spinner2;

	public static final int REFLASH = 0;// 刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_managegoods);

		initview();

		// 显示商品
		goods = gDao.queryAll();
		// 创建适配器
		mAdapter = new MyAdapter(ManageGoodsActivity.this, goods, ManageGoodsActivity.this, 1);
		pvGoods.setAdapter(mAdapter);
		// 商品未超过一页，导航点不显示
		if (goods.size() > 8) {
			initDot();
		}

		// 排序设置
		sortGoods();

		// 批量选择
		tvEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (FLAG) {
					// 选择设置
					FLAG = false;
					tvEdit.setText("批量删除");

				} else {
					// 删除已选商品
					FLAG = true;
					// 遍历已选商品根据ID删除
					for (Entry<Integer, Goods> h : hashGood.entrySet()) {
						gDao.deleteGood(h.getKey());
					}
					tvEdit.setText("批量选择");
					// 重新显示排序
					sortGoodsType(spinner1.getSelectedItemPosition());
					sortGoodsOther(spinner2.getSelectedItemPosition());
					// 清空
					hashGood.clear();
					// 刷新
					mAdapter = new MyAdapter(ManageGoodsActivity.this, goods, ManageGoodsActivity.this, 1);
					pvGoods.setAdapter(mAdapter);
					// 导航点重新创建
					initDot();

				}

			}
		});

		// 增加商品按钮
		tvAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (FLAG) {
					Intent intent = new Intent(ManageGoodsActivity.this, AddAndEditGoodsActivity.class);
					startActivityForResult(intent, REFLASH);
				}else {
					Toast.makeText(ManageGoodsActivity.this, R.string.Manage_activity_message01, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void initview() {
		// 初始化获取页面组建
		pvGoods = (CustomViewPager) findViewById(R.id.vp_goods);
		tvAdd = (TextView) findViewById(R.id.tv_add);
		tvEdit = (TextView) findViewById(R.id.tv_edit);
		llDots = (LinearLayout) findViewById(R.id.ll_dots);
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);

		hashGood = new HashMap<Integer, Goods>();

		gDao = new GoodsDao(ManageGoodsActivity.this);
	}

	// 返回按钮
	public void toback(View v) {
		Intent intent = new Intent();
		setResult(1, intent);
		finish();
	}

	// 返回键监听
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			setResult(REFLASH, intent);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void sortGoods() {
		// 下拉框排序设置
		String[] typeList = gDao.queryType();
		spinner1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, typeList));
		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// 按分类查询
				sortGoodsType(position);
				// 按销量日期查询
				sortGoodsOther(spinner2.getSelectedItemPosition());
				mAdapter = new MyAdapter(ManageGoodsActivity.this, goods, ManageGoodsActivity.this, 1);
				pvGoods.setAdapter(mAdapter);
				// 导航点重新创建
				initDot();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		spinner2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, otherList));
		spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				sortGoodsOther(position);
				mAdapter.refresh(goods, 1);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	// 按分类查询
	public void sortGoodsType(int position) {
		if (position != 0) {
			TypeDao tDao = new TypeDao(ManageGoodsActivity.this);
			String typeName = (String) spinner1.getSelectedItem();
			long id = tDao.queryOneId(typeName);
			goods = gDao.sortByGoods(id);
		} else {
			goods = gDao.queryAll();
		}
		for (int i = 0; i < goods.size(); i++) {
			if (hashGood.get(goods.get(i).getGoods_id()) != null) {
				Goods a = hashGood.get(goods.get(i).getGoods_id());
				goods.set(i, a);
			}
		}
	}

	// 根据不同排序不同方式0综合，1效率，2日期
	public void sortGoodsOther(int position) {
		switch (position) {
		case 0:
			Collections.sort(goods, new SortByDate());
			break;
		case 1:
			Collections.sort(goods, new SortBySales());
			break;
		case 2:
			Collections.sort(goods, new SortByDate());
			break;
		default:
			break;
		}
	}

	// 根据日期排序
	class SortByDate implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			Goods s1 = (Goods) o1;
			Goods s2 = (Goods) o2;
			if (s1.getGoods_id() < s2.getGoods_id()) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	// 根据销量排序
	class SortBySales implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			Goods s1 = (Goods) o1;
			Goods s2 = (Goods) o2;
			if (Integer.parseInt(s1.getGoods_sales()) < Integer.parseInt(s2.getGoods_sales())) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	/**
	 * 绘制导航点
	 */
	private void initDot() {
		llDots.removeAllViews();
		dots.clear();
		int size = goods.size() / 9;
		if (goods.size() % 9 != 0) {
			size = goods.size() / 9 + 1;
		}
		for (int i = 0; i < size; i++) {
			ImageView imageView = new ImageView(this);
			// 创建一个ImageView来放置圆点，并确定ImageView的宽高
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
			params.leftMargin = 10;
			params.rightMargin = 10;
			params.topMargin = 5;
			params.bottomMargin = 5;
			imageView.setLayoutParams(params);
			dots.add(imageView);
			// 由于刚进去引导页时出现的是第一个引导页，故此时的导航点设置为红色
			if (i == 0) {
				dots.get(i).setBackgroundResource(R.drawable.dot_green);
			} else {
				dots.get(i).setBackgroundResource(R.drawable.dot_gray);
			}
			llDots.addView(imageView);
		}
		// 页面切换，导航点随之改变
		pvGoods.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				for (int i = 0; i < dots.size(); i++) {
					if (i == position) {
						dots.get(i).setBackgroundResource(R.drawable.dot_green);
					} else {
						dots.get(i).setBackgroundResource(R.drawable.dot_gray);
					}
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	// 页面商品点击事件
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int pageNum = pvGoods.getCurrentItem();
		switch (v.getId()) {
		case R.id.ll_item1:
			changeGood(goods.get(pageNum * 9 + 0));
			break;
		case R.id.ll_item2:
			changeGood(goods.get(pageNum * 9 + 1));
			break;
		case R.id.ll_item3:
			changeGood(goods.get(pageNum * 9 + 2));
			break;
		case R.id.ll_item4:
			changeGood(goods.get(pageNum * 9 + 3));
			break;
		case R.id.ll_item5:
			changeGood(goods.get(pageNum * 9 + 4));
			break;
		case R.id.ll_item6:
			changeGood(goods.get(pageNum * 9 + 5));
			break;
		case R.id.ll_item7:
			changeGood(goods.get(pageNum * 9 + 6));
			break;
		case R.id.ll_item8:
			changeGood(goods.get(pageNum * 9 + 7));
			break;
		case R.id.ll_item9:
			changeGood(goods.get(pageNum * 9 + 8));
			break;
		default:
			break;
		}

	}

	// 页面跳转-修改页面-批量选择
	private void changeGood(Goods good) {
		// TODO Auto-generated method stub
		if (FLAG) {
			// 如果未在批量选择模式下
			Intent intent = new Intent(ManageGoodsActivity.this, AddAndEditGoodsActivity.class);
			intent.putExtra("good_id", good.getGoods_id());
			intent.putExtra("good", good);
			startActivityForResult(intent, REFLASH);

		} else {
			// 在批量选择下
			if (good.isSelected()) {
				good.setSelected(false);
				hashGood.remove(good.getGoods_id());
			} else {
				good.setSelected(true);
				hashGood.put(good.getGoods_id(), good);
			}

			mAdapter.refresh(goods, 1);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REFLASH) {
			// 重新刷新数据
			FLAG = true;
			hashGood.clear();
			// 刷新下拉框，商品
			sortGoods();
			mAdapter = new MyAdapter(ManageGoodsActivity.this, goods, ManageGoodsActivity.this, 1);
			pvGoods.setAdapter(mAdapter);
			// 导航点重新创建
			initDot();
		}
	}
}
