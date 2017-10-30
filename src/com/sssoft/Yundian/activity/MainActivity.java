package com.sssoft.Yundian.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.chainsaw.Main;

import com.readystatesoftware.viewbadger.BadgeView;
import com.sssoft.Yundian.R;
import com.sssoft.Yundian.Dao.GoodsDao;
import com.sssoft.Yundian.Dao.PordersDao;
import com.sssoft.Yundian.Dao.TypeDao;
import com.sssoft.Yundian.bean.Goods;
import com.sssoft.Yundian.utils.Arith;
import com.sssoft.Yundian.utils.FormatUtil;
import com.sssoft.Yundian.view.CustomViewPager;
import com.sssoft.Yundian.view.slidingMenu;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 主界面
 * 
 * @author Jiang 2017.10.11
 *
 */
@SuppressLint("InflateParams")
public class MainActivity extends Activity implements View.OnClickListener {
	private slidingMenu mLeftMenu;
	// private
	private List<Goods> goods;
	// private List<Goods> shoppingCard;

	static Activity activity; // 设置全局，用于结束此activity
	private HashMap<Integer, Goods> hashGood;

	private GoodsDao gDao;
	private PordersDao pDao;
	private String[] typeList;
	private String[] otherList = { "综合", "销量", "上新" };
	// 记录排序点击位置
	private int sortPosition1;
	private int sortPosition2;

	private LinearLayout llDots;
	private Button btBuy;
	private TextView tvTotal;
	private TextView tvInfo;
	private ImageView ivBuy;
	private Button btSetting;
	private RelativeLayout rlManager;
	private RelativeLayout rlAccount;
	private RelativeLayout rlCard;
	private RelativeLayout rlVipCard;
	private RelativeLayout rlRecharge;
	private RelativeLayout rlRecover;

	private LinearLayout llInfo;
	private LinearLayout llCard;
	private CustomViewPager pvGoods;
	private Spinner spinner1;
	private Spinner spinner2;

	private View target2;
	private BadgeView badge2;
	private View getListView;
	private Builder builder;
	private AlertDialog dialog;

	private MyAdapter mAdapter;
	private CardAdapter adapter;
	private ListView lView;

	// 购物车总价格
	private double sum = 0;
	// 购物车总商品数
	private int total = 0;

	private long exitTime = 0;
	private List<ImageView> dots = new ArrayList<ImageView>();
	public static final int MAINPAGE = 0;// 主页面
	public static final int REFLASH = 0;// 刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 无初始title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 初始化
		activity = this;
		initView();

	}

	public void initView() {
		mLeftMenu = (slidingMenu) findViewById(R.id.id_menu);
		llDots = (LinearLayout) findViewById(R.id.ll_dots);
		btBuy = (Button) findViewById(R.id.bt_buy);
		tvTotal = (TextView) findViewById(R.id.tv_total);
		tvInfo = (TextView) findViewById(R.id.tv_addinfo);
		ivBuy = (ImageView) findViewById(R.id.iv_buy);
		llInfo = (LinearLayout) findViewById(R.id.ll_info);
		rlManager = (RelativeLayout) findViewById(R.id.rl_manage);
		rlAccount = (RelativeLayout) findViewById(R.id.rl_account);
		rlCard = (RelativeLayout) findViewById(R.id.rl_card);
		rlVipCard = (RelativeLayout) findViewById(R.id.rl_vipcard);
		rlRecharge = (RelativeLayout) findViewById(R.id.rl_recharge);
		rlRecover = (RelativeLayout) findViewById(R.id.rl_recover);
		llCard = (LinearLayout) findViewById(R.id.ll_show_card);
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		btSetting = (Button) findViewById(R.id.setting);

		hashGood = new HashMap<Integer, Goods>();

		// 左侧菜单点击事件
		MyListener ml = new MyListener();
		rlManager.setOnClickListener(ml);
		rlAccount.setOnClickListener(ml);
		rlCard.setOnClickListener(ml);
		rlVipCard.setOnClickListener(ml);
		rlRecharge.setOnClickListener(ml);
		rlRecover.setOnClickListener(ml);

		target2 = findViewById(R.id.iv_buy);
		badge2 = new BadgeView(MainActivity.this, target2);
		pDao = new PordersDao(MainActivity.this);

		// 商品显示区域
		createViewPager();

		// 排序设置
		sortGoods();

		btSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, SettingActivity.class);
				startActivity(intent);
			}
		});
		// 选好跳转
		btBuy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 保存已选物品
				if (tvTotal.getText().toString().length() < 10) {
					if (hashGood.size() > 0) {
						long mark_id = pDao.insetInPorders(hashGood, total);
						Intent intent = new Intent(MainActivity.this, ConsumptionActivity.class);
						intent.putExtra("ordersId", mark_id);
						startActivity(intent);
					} else {
						Toast.makeText(MainActivity.this, R.string.main_activity_message01, Toast.LENGTH_SHORT).show();

					}
				} else {
					Toast.makeText(MainActivity.this, R.string.main_activity_message02, Toast.LENGTH_SHORT).show();

				}
			}
		});

		// 查看购物车
		ivBuy.setOnClickListener(new OnClickListener() {
			//
			@Override
			public void onClick(View v) {
				// CreateDialog();
				int mContentX = getContentX();
				if (mContentX < 100) {
					createPopupWindow();
				} else {
					mLeftMenu.toggle();
				}
				Log.i("ContentX", "x" + mContentX);
				// createPopupWindow();
			}
		});

		// mLeftMenu.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // 侧面菜单取消滑动只能点击
		// return true;
		// }
		// });

	}

	private void sortGoods() {
		// 设置两下拉框，排序
		typeList = gDao.queryType();
		spinner1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, typeList));
		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				sortPosition1 = position;
				// 如果不是全部，根据不同类型查询数据库排序
				sortGoodsType(position);
				// 按销量日期查询
				sortGoodsOther(spinner2.getSelectedItemPosition());
				mAdapter = new MyAdapter(MainActivity.this, goods, MainActivity.this, REFLASH);
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
				sortPosition2 = position;
				// 排序
				sortGoodsOther(sortPosition2);
				// 刷新
				mAdapter.refresh(goods, MAINPAGE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	// 创建商品九宫格显示
	public void createViewPager() {
		gDao = new GoodsDao(MainActivity.this);
		goods = gDao.queryAll();
		pvGoods = (CustomViewPager) findViewById(R.id.vp_goods);

		// 判断 是否有商品来是否显示 提示语
		if (goods.size() == 0) {
			// 无商品 显示提示语，隐藏pageviwer
			llInfo.setVisibility(View.VISIBLE);
			tvInfo.setText(R.string.main_activity_message05);
			pvGoods.setVisibility(View.GONE);
		} else {
			// 正常显示
			llInfo.setVisibility(View.GONE);
			pvGoods.setVisibility(View.VISIBLE);

			mAdapter = new MyAdapter(this, goods, this);
			pvGoods.setAdapter(mAdapter);
			initDot();

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
	}

	// 按分类查询
	public void sortGoodsType(int position) {
		if (position != 0) {
			TypeDao tDao = new TypeDao(MainActivity.this);
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

	// 左侧菜单点击事件
	class MyListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent;
			switch (v.getId()) {
			case R.id.rl_manage:
				// 管理页面
				intent = new Intent(MainActivity.this, ManageGoodsActivity.class);
				startActivityForResult(intent, REFLASH);
				break;
			case R.id.rl_account:
				// 收入页面
				intent = new Intent(MainActivity.this, MyIncomeActivity.class);
				startActivity(intent);
				break;
			case R.id.rl_card:
				//
				Toast.makeText(MainActivity.this, "gg", Toast.LENGTH_SHORT).show();
				break;
			case R.id.rl_vipcard:
				Toast.makeText(MainActivity.this, "gg", Toast.LENGTH_SHORT).show();
				break;
			case R.id.rl_recharge:
				Toast.makeText(MainActivity.this, "gg", Toast.LENGTH_SHORT).show();
				break;
			case R.id.rl_recover:
				// 恢复挂单
				intent = new Intent(MainActivity.this, PendingOrderActivity.class);
				startActivity(intent);
				break;
			default:
				break;

			}

		}
	}

	public void toggleMenu(View v) {
		mLeftMenu.toggle();
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}

	// 给点击的物品做数据汇总
	private void ishaskey(Goods good) {
		// 判断菜单是否打开，打开就关闭
		int mContentX = getContentX();
		if (mContentX > 100) {
			mLeftMenu.toggle();
		} else {

			sum = Double.parseDouble(tvTotal.getText().toString());
			sum = Arith.add(Double.parseDouble(good.getGoods_price()), sum);
			// 获取当前购物金额，超过限制提示。忽略增加
			if (sum > 999999) {
				Toast.makeText(MainActivity.this, R.string.main_activity_message02, Toast.LENGTH_SHORT).show();
			} else {
				// 有没有被选择
				if (good.isSelected()) {
					// 数量是否超过限制999
					if (good.getSelectedNum() < 999) {
						good.setSelectedNum(good.getSelectedNum() + 1);
					} else {
						Toast.makeText(MainActivity.this, R.string.main_activity_message03, Toast.LENGTH_SHORT).show();
					}
				} else {
					good.setSelected(true);
					good.setSelectedNum(1);
				}
				hashGood.put(good.getGoods_id(), good);
				total();
				mAdapter.refresh(goods, MAINPAGE);
			}
		}
	}

	private void total() {
		sum = 0;
		total = 0;
		for (Entry<Integer, Goods> hgood : hashGood.entrySet()) {
			double onePrice = Arith.mul(Double.parseDouble(hgood.getValue().getGoods_price()),
					hgood.getValue().getSelectedNum());
			sum = Arith.add(onePrice, sum);
			total = total + hgood.getValue().getSelectedNum();

		}
		// 显示价格和图标
		tvTotal.setText(FormatUtil.double2StrAmt(sum) + "");
		badge2.setText(total + "");
		badge2.show();

	}

	// 每个商品的点击事件
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int pageNum = pvGoods.getCurrentItem();
		switch (v.getId()) {
		case R.id.ll_item1:
			ishaskey(goods.get(pageNum * 9 + 0));
			break;
		case R.id.ll_item2:
			ishaskey(goods.get(pageNum * 9 + 1));
			break;
		case R.id.ll_item3:
			ishaskey(goods.get(pageNum * 9 + 2));
			break;
		case R.id.ll_item4:
			ishaskey(goods.get(pageNum * 9 + 3));
			break;
		case R.id.ll_item5:
			ishaskey(goods.get(pageNum * 9 + 4));
			break;
		case R.id.ll_item6:
			ishaskey(goods.get(pageNum * 9 + 5));
			break;
		case R.id.ll_item7:
			ishaskey(goods.get(pageNum * 9 + 6));
			break;
		case R.id.ll_item8:
			ishaskey(goods.get(pageNum * 9 + 7));
			break;
		case R.id.ll_item9:
			ishaskey(goods.get(pageNum * 9 + 8));
			break;
		default:
			break;
		}
	}

	// 创建Popupwindow
	public void createPopupWindow() {
		View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.shopping_card, null);
		final PopupWindow popWnd = new PopupWindow(MainActivity.this);
		popWnd.setContentView(contentView);
		popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		popWnd.setFocusable(true);
		popWnd.setOutsideTouchable(true);
		popWnd.setBackgroundDrawable(new BitmapDrawable(getResources()));
		popWnd.setAnimationStyle(R.style.anim_menu_bottombar);
		int height = llCard.getMeasuredHeight();
		Log.i("sdfsd", height + "s");
		popWnd.showAtLocation(llCard, Gravity.BOTTOM | Gravity.START, 0, height);
		lView = (ListView) contentView.findViewById(R.id.lv_shopping_goods);
		adapter = new CardAdapter();
		// 给listview加入适配器
		lView.setAdapter(adapter);
		lView.setItemsCanFocus(false);
		ImageView ivClear = (ImageView) contentView.findViewById(R.id.iv_clear);
		TextView tvPorder = (TextView) contentView.findViewById(R.id.tv_porder);
		// 挂单图标点击事件
		tvPorder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				if (hashGood.size() > 0) {
					// 挂单
					long mark_id = pDao.insetInPorders(hashGood, total);
					builder.setTitle(R.string.main_activity_message07); // 挂单成功
				} else {
					builder.setTitle(R.string.main_activity_message06); // 挂单失败
				}
				builder.setPositiveButton("确定", null);
				builder.show();
				// 清空
				clearPopShoppingPage(popWnd);
			}
		});
		// 购物车清空点击事件
		ivClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog2, int which) {
						// 清空
						clearPopShoppingPage(popWnd);
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle(R.string.main_activity_message04);
				builder.setPositiveButton("确定", listener);
				builder.setNegativeButton("取消", null);
				builder.show();

			}

		});
	}

	public void clearPopShoppingPage(PopupWindow popWnd) {
		for (int i = 0; i < goods.size(); i++) {
			goods.get(i).setSelected(false);
			goods.get(i).setSelectedNum(0);
		}
		hashGood.clear();
		popWnd.dismiss();
		// listview.setAdapter(adapter);
		mAdapter.refresh(goods, MAINPAGE);
		total();
	}

	// 创建购物车弹框
	public void CreateDialog() {

		// 动态加载一个listview的布局文件进来
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		getListView = inflater.inflate(R.layout.shopping_card, null);

		// 给ListView绑定内容
		lView = (ListView) getListView.findViewById(R.id.lv_shopping_goods);
		adapter = new CardAdapter();
		// 给listview加入适配器
		lView.setAdapter(adapter);
		lView.setItemsCanFocus(false);

		builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		// 设置加载的listview
		builder.setView(getListView);

		dialog = builder.create();
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		window.setGravity(Gravity.BOTTOM);
		lp.y = 130; // 新位置Y坐标
		window.setAttributes(lp);
		dialog.show();

		ImageView ivClear = (ImageView) getListView.findViewById(R.id.iv_clear);
		TextView tvPorder = (TextView) getListView.findViewById(R.id.tv_porder);
		// 挂单图标点击事件
		tvPorder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				if (hashGood.size() > 0) {
					// 挂单
					long mark_id = pDao.insetInPorders(hashGood, total);
					builder.setTitle(R.string.main_activity_message07); // 挂单成功
				} else {
					builder.setTitle(R.string.main_activity_message06); // 挂单失败
				}
				builder.setPositiveButton("确定", null);
				builder.show();
				// 清空
				clearShoppingPage();
			}
		});
		// 购物车清空点击事件
		ivClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog2, int which) {
						// 清空
						clearShoppingPage();
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle(R.string.main_activity_message04);
				builder.setPositiveButton("确定", listener);
				builder.setNegativeButton("取消", null);
				builder.show();

			}

		});
	}

	public void clearShoppingPage() {
		for (int i = 0; i < goods.size(); i++) {
			goods.get(i).setSelected(false);
			goods.get(i).setSelectedNum(0);
		}
		hashGood.clear();
		dialog.hide();
		// listview.setAdapter(adapter);
		mAdapter.refresh(goods, MAINPAGE);
		total();
	}

	class CardAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return hashGood.size();
		}

		@Override
		public Object getItem(int position) {
			int size = 0;
			Goods a = null;
			for (Entry<Integer, Goods> hgood : hashGood.entrySet()) {
				if (size == position) {
					a = hgood.getValue();
				}
				size++;
			}
			return a;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View item = convertView != null ? convertView
					: View.inflate(getApplicationContext(), R.layout.show_shopping, null);

			TextView tvShopName = (TextView) item.findViewById(R.id.tv_shop_name);
			TextView tvShopPrice = (TextView) item.findViewById(R.id.tv_shop_price);
			TextView tvShopNum = (TextView) item.findViewById(R.id.tv_shop_num);
			ImageView ivShopDown = (ImageView) item.findViewById(R.id.ib_shop_down);
			ImageView ivShopUp = (ImageView) item.findViewById(R.id.ib_shop_up);

			int num = 0;
			Goods b = null;
			for (Entry<Integer, Goods> hgood : hashGood.entrySet()) {
				if (num == position) {
					b = hgood.getValue();
				}
				num++;
			}
			final Goods a = b;

			tvShopName.setText(a.getGoods_name() + "");
			tvShopPrice.setText(a.getGoods_price() + "");
			tvShopNum.setText(a.getSelectedNum() + "");

			// +1
			ivShopUp.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					sum = Double.parseDouble(tvTotal.getText().toString());
					sum = Arith.add(Double.parseDouble(a.getGoods_price()), sum);
					if (sum > 999999) {
						Toast.makeText(MainActivity.this, R.string.main_activity_message02, Toast.LENGTH_SHORT).show();
					} else {
						if (a.getSelectedNum() < 999) {
							a.setSelectedNum(a.getSelectedNum() + 1);
							notifyDataSetChanged();
							mAdapter.refresh(goods, MAINPAGE);
							total();
						} else {
							Toast.makeText(MainActivity.this, R.string.main_activity_message03, Toast.LENGTH_SHORT)
									.show();
						}
					}
				}
			});

			// -1
			ivShopDown.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					a.setSelectedNum(a.getSelectedNum() - 1);
					if (a.getSelectedNum() == 0) {
						a.setSelected(false);
						hashGood.remove(a.getGoods_id());
					}
					notifyDataSetChanged();
					mAdapter.refresh(goods, MAINPAGE);
					total();
				}
			});
			return item;
		}
	}

	// 恢复初始选购页面
	public void initMianPager() {
		// 清空购物车
		hashGood.clear();
		total();
		// 更新导航栏
		sortGoods();
		createViewPager();
	}

	// 获取购物车的X的大小，来判断菜单是否打开
	public int getContentX() {
		int[] location = new int[2];
		ivBuy.getLocationOnScreen(location);
		return location[0];
	}

	// // 返回键监听
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	//
	// if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	// int mContentX = getContentX();
	// if (mContentX > 100) {
	// mLeftMenu.toggle();
	// }
	// return false;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REFLASH) {
			initMianPager();
		}
	}

	// 双击退出
	@Override
	public void onBackPressed() {
		int mContentX = getContentX();
		if (mContentX > 100) {
			mLeftMenu.toggle();
		} else {
			if (System.currentTimeMillis() - exitTime > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
	}
}
