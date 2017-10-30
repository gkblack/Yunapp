package com.sssoft.Yundian.activity;

import java.util.List;

import com.sssoft.Yundian.R;
import com.sssoft.Yundian.Dao.UnitDao;
import com.sssoft.Yundian.bean.Other;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddunitsActivity extends Activity {
	// 需要适配的数据集合
	private List<Other> list;
	// 数据库增删改查操作类
	private UnitDao dao;
	private EditText nameET;
	// 适配器
	private MyAdapter adapter;
	//
	private ListView accountLV;
	private Button button;

	public static final int REFLASH = 100;// 刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addunits);
		// 初始化控件
		initView();
		dao = new UnitDao(this);
		// 从数据库查询出所有数据
		list = dao.queryAll();

		adapter = new MyAdapter();
		accountLV.setAdapter(adapter);// 给LISTVIEW添加适配器(数据生成条目)
		// 完成
		button = (Button) findViewById(R.id.bt_f);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				setResult(REFLASH, intent);
				finish();
			}
		});
	}

	// 初始化控件
	private void initView() {
		accountLV = (ListView) findViewById(R.id.accountLV);
		nameET = (EditText) findViewById(R.id.nameET);

	}

	// activity_main.xml对应image view的点击事件触发的方法
	public void add(View view) {
		// 新增
		String name = nameET.getText().toString().trim();
		if (name.length() > 0) {
			// 不存在即插入
			if (!dao.isHasUnits(name)) {
				Other a = new Other(name);
				dao.insert(a); // 插入数据库
				list.add(a); // 插入集合

				accountLV.setAdapter(adapter); // 刷新界面
				// 选中最后一个
				accountLV.setSelection(accountLV.getCount() - 1);
				nameET.setText("");
			} else {
				Toast.makeText(AddunitsActivity.this, R.string.Addunits_activity_message03, Toast.LENGTH_SHORT).show();

			}
		} else {
			Toast.makeText(AddunitsActivity.this, R.string.Addunits_activity_message02, Toast.LENGTH_SHORT).show();
		}

	}

	private class MyAdapter extends BaseAdapter {
		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// 重用convertView
			View item = convertView != null ? convertView : View.inflate(getApplicationContext(), R.layout.item, null);
			// 获取该试图中的EditText
			EditText nameTV = (EditText) item.findViewById(R.id.nameTV);
			ImageView edit = (ImageView) item.findViewById(R.id.iv_edit);
			ImageView deleteIV = (ImageView) item.findViewById(R.id.deleteTV);

			// 根据当前位置获取account对象
			final Other a = list.get(position);

			nameTV.setText(a.getName());
			// 编辑 触发事件
			edit.setTag(false);
			edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					createDialog(a);
				}
			});
			nameTV.setText(a.getName());

			// 删除图片的点击事件触发的方法
			deleteIV.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					// 删除数据之前先弹出一个对话框
					AlertDialog.Builder builder = new AlertDialog.Builder(AddunitsActivity.this);
					if (dao.isHasUsed(a.getId())) {
						// 先判断是否被商品使用
						builder.setTitle(R.string.Addunits_activity_message04);
						builder.setPositiveButton("确定", null);
					} else {
						
						android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								list.remove(a); // 从集合中删除
								dao.delete(a.getId()); // 从数据库中删除
								notifyDataSetChanged();// 刷新界面
							}
						};
						// 创建对话框
						builder.setTitle("确定要删除吗？"); // 设置标题
						// 设置确定按钮的文本及监听
						builder.setPositiveButton("确定", listener);
						builder.setNegativeButton("取消", null); // 设置取消按钮
					}
					builder.show();
				}
			});
			return item;
		}

		public void createDialog(Other aUnit) {
			EditText name;
			AlertDialog.Builder builder = new AlertDialog.Builder(AddunitsActivity.this);
			View view = LayoutInflater.from(AddunitsActivity.this).inflate(R.layout.input_dialog, null);
			name = (EditText) view.findViewById(R.id.input_name);
			name.setText(aUnit.getName());
			name.setSelection(aUnit.getName().length());
			builder.setView(view);
			builder.setPositiveButton("确定", new ButtonLister(name, aUnit));
			builder.setNegativeButton("取消", null);
			builder.show();
		}

		public class ButtonLister implements DialogInterface.OnClickListener {
			TextView name;
			Other aUnit;

			public ButtonLister(TextView name, Other aUnit) {
				this.name = name;
				this.aUnit = aUnit;
			}

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 修改
				String newName = name.getText().toString().trim();
				if (newName.length() > 0) {
					// 不存在该单位 插入数据库
					if (!dao.isHasUnits(newName)) {
						// 刷新
						aUnit.setName(newName);
						dao.update(aUnit);
						list = dao.queryAll();
						accountLV.setAdapter(adapter);
					} else {
						// 已存在
						Toast.makeText(AddunitsActivity.this, R.string.Addunits_activity_message03, Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(AddunitsActivity.this, R.string.Addunits_activity_message01, Toast.LENGTH_SHORT)
							.show();
					accountLV.setAdapter(adapter);
				}

			}
		}
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

}
