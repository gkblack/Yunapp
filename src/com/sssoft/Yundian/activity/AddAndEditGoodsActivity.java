package com.sssoft.Yundian.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.sssoft.Yundian.R;
import com.sssoft.Yundian.Dao.GoodsDao;
import com.sssoft.Yundian.bean.Goods;
import com.sssoft.Yundian.utils.FileUitlity;
import com.sssoft.Yundian.utils.others;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 增加修改商品
 * 
 * @author Jiang 2017.10.18
 */
public class AddAndEditGoodsActivity extends Activity {
	private ImageView image;
	private LinearLayout llImage;
	private EditText etGoodName;
	private EditText etGoodSpecification;
	private EditText etGoodValue;
	private EditText etGoodNumber;
	private EditText etGoodNote;
	private TextView tvTitle;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private ArrayAdapter<String> adapter1;
	private ArrayAdapter<String> adapter2;
	private Spinner spinner1;
	private Spinner spinner2;

	private GoodsDao dao;
	private String[] spinnerlist1;
	private String[] spinnerlist2;
	private FileOutputStream bit;
	private String path = "";
	private boolean picHasChanged = false;
	private int goodId;
	private Goods good;

	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final int NOGOODID = -1;// 新增
	public static final int REFLASH = 0;// 返回给上个页面刷新
	public static final int REFLASHTHIS = 100;// 本页面刷新
	public static final String IMAGE_UNSPECIFIED = "image/*";

	// 选择图片方式
	private String[] getMethod = { "本地相册", "拍照" };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addgoods);

		dao = new GoodsDao(this);
		setSpinner();

		// 图片上传
		// 相机本地图片
		image = (ImageView) findViewById(R.id.iv_pic);
		llImage = (LinearLayout) findViewById(R.id.ll_upload);
		llImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CreateDialog();
			}
		});

		// 获取控件
		etGoodName = (EditText) findViewById(R.id.et_goodname);
		etGoodSpecification = (EditText) findViewById(R.id.et_goodspecification);
		etGoodValue = (EditText) findViewById(R.id.et_goodvalue);
		etGoodNumber = (EditText) findViewById(R.id.et_goodnumber);
		etGoodNote = (EditText) findViewById(R.id.et_goodnote);

		// 输入金额规范
		etGoodValue.addTextChangedListener(new TextWatcher() {

			private String numberStr; // 定义一个字符串来得到处理后的金额

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				int lenght = s.length();
				Log.e("Sun", s + "====" + start + "=======" + before + "======" + count);
				double number = 0.00; // 初始金额
				// 第一次输入初始化 金额值
				if (lenght <= 1) {
					number = Double.parseDouble(s.toString());
					number = number / 100;// 第一次 长度等于
					numberStr = number + "";
				} else {
					// 之后的输入带入算法后将值设置给 金额值
					if (s.toString().contains(".")) {
						// 处理金额
						if (lenght < 10) {
							numberStr = getMoneyString(s.toString());
						}
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				String aa = etGoodValue.getText().toString();
				// 在此判断输入框的值是否等于金额的值，如果不相同则赋值，如果不判断监听器将会出现死循环
				if (!TextUtils.isEmpty(etGoodValue.getText().toString())
						&& !etGoodValue.getText().toString().equals(numberStr)) {
					etGoodValue.setText(numberStr); // 赋值到editText上
					etGoodValue.setSelection(numberStr.length()); // 将光标定位到结尾
				}
			}

		});

		Intent intent = getIntent();
		// 如果是编辑就传物品的id，否则未-1
		goodId = intent.getIntExtra("good_id", NOGOODID);
		// 如果是编辑就显示商品信息
		if (goodId > 0) {
			tvTitle = (TextView) findViewById(R.id.tv_title);
			tvTitle.setText(R.string.title_edit_goods);
			good = (Goods) intent.getSerializableExtra("good");
			etGoodName.setText(good.getGoods_name());
			etGoodSpecification.setText(good.getGoods_spec());
			etGoodValue.setText(good.getGoods_price());
			etGoodNote.setText(good.getGoods_remark());
			etGoodNumber.setText(good.getGoods_num());
			for (int i = 0; i < spinnerlist1.length; i++) {
				if (spinnerlist1[i].equals(good.getGoods_type())) {
					spinner1.setSelection(i);
				}
			}
			for (int i = 0; i < spinnerlist2.length; i++) {
				if (spinnerlist2[i].equals(good.getGoods_units())) {
					spinner2.setSelection(i);
				}
			}

			Bitmap bt = BitmapFactory.decodeFile(good.getGoods_img());
			if (bt != null) {
				Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
				image.setImageDrawable(drawable);
			} else {
				// 如果SD里面没有则默认图片
				image.setBackgroundResource(R.drawable.shopping);

			}
		}
		// 退出
		button2 = (Button) findViewById(R.id.bt_Menu);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 回传要求更新页面
				Intent intent = new Intent();
				setResult(REFLASH, intent);
				finish();
			}
		});

		// 增加种类
		button3 = (Button) findViewById(R.id.bt_add1);
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddAndEditGoodsActivity.this, AddtypeActivity.class);
				startActivityForResult(intent, REFLASHTHIS);
			}
		});

		// 增加单位
		button4 = (Button) findViewById(R.id.bt_add2);
		button4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddAndEditGoodsActivity.this, AddunitsActivity.class);
				startActivityForResult(intent, REFLASHTHIS);
			}
		});

		// 保存按钮，增加商品
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new Button.OnClickListener() {
			// 信息转换成字符串形式
			public void onClick(View view) {
				String name = etGoodName.getText().toString().trim();
				String spec = etGoodSpecification.getText().toString().trim();
				String type = (String) spinner1.getSelectedItem();
				String units = (String) spinner2.getSelectedItem();
				String price = etGoodValue.getText().toString().trim();
				String num = etGoodNumber.getText().toString().trim();
				String remark = etGoodNote.getText().toString().trim();
				String date = new others().getDate();
				String img = "";
				// 先判断关键信息填写完成
				if (num.equals("")) {
					num = "0";
				}
				if (name.equals("") || name.length() > 8) {
					Toast.makeText(AddAndEditGoodsActivity.this, R.string.addEdit_activity_message01,
							Toast.LENGTH_SHORT).show();
				} else if (spec.equals("") || spec.length() > 8) {
					Toast.makeText(AddAndEditGoodsActivity.this, R.string.addEdit_activity_message02,
							Toast.LENGTH_SHORT).show();
				} else if (num.length() > 7) {
					Toast.makeText(AddAndEditGoodsActivity.this, R.string.addEdit_activity_message03,
							Toast.LENGTH_SHORT).show();
				} else if (price.length() == 0) {
					Toast.makeText(AddAndEditGoodsActivity.this, R.string.addEdit_activity_message04,
							Toast.LENGTH_SHORT).show();

				} else if (type == null) {
					Toast.makeText(AddAndEditGoodsActivity.this, R.string.addEdit_activity_message06,
							Toast.LENGTH_SHORT).show();
				} else if (units == null) {
					Toast.makeText(AddAndEditGoodsActivity.this, R.string.addEdit_activity_message07,
							Toast.LENGTH_SHORT).show();
				} else {
					// 在判断是增加还是修改
					if (goodId == NOGOODID) {
						// 新增
						String sales = "0";
						if (picHasChanged) {
							img = path;
						}
						dao.insert(name, type, spec, units, price, num, remark, sales, img, date);

					} else {
						// 修改
						String sales = good.getGoods_sales();
						if (picHasChanged) {
							img = path;
						} else {
							img = good.getGoods_img();
						}
						dao.update(goodId, name, type, spec, units, price, num, remark, sales, img, date);

					}
					Toast.makeText(AddAndEditGoodsActivity.this, R.string.addEdit_activity_message05,
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					setResult(REFLASH, intent);
					finish();
				}

			}
		});

		// 相机本地图片
		image = (ImageView) findViewById(R.id.iv_pic);

	}

	// 设置下拉框
	private void setSpinner() {
		// 获取界面布局中的spinner组件
		spinner1 = (Spinner) findViewById(R.id.type);
		spinnerlist1 = dao.queryAllType();

		// 创建Array适配器对象
		adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinnerlist1);
		// 为spinner设置adapter
		spinner1.setAdapter(adapter1);

		spinner2 = (Spinner) findViewById(R.id.unit);
		spinnerlist2 = dao.queryAllUnits();

		// 创建Array适配器对象
		adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinnerlist2);
		// 为spinner设置adapter
		spinner2.setAdapter(adapter2);
	}

	// 数据回传
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 增加分类等后刷新
		if (requestCode == REFLASHTHIS) {
			// 暂时存下之前所选
			String sp1 = (String) spinner1.getSelectedItem();
			String sp2 = (String) spinner2.getSelectedItem();
			// 刷新初始化
			setSpinner();
			// 恢复之前所选
			for (int i = 0; i < spinnerlist1.length; i++) {
				if (spinnerlist1[i].equals(sp1)) {
					spinner1.setSelection(i);
				}
			}
			for (int i = 0; i < spinnerlist2.length; i++) {
				if (spinnerlist2[i].equals(sp2)) {
					spinner2.setSelection(i);
				}
			}

		}
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			// 设置文件保存路径这里放在目录下
			File picture = new File(path);
			startPhotoZoom(Uri.fromFile(picture));
		}
		if (data == null)
			return;

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
			startPhotoZoom(data.getData());
		}
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				// ByteArrayOutputStream stream = new ByteArrayOutputStream();
				try {
					bit = new FileOutputStream(path);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 保存压缩文件
				photo.compress(Bitmap.CompressFormat.JPEG, 75, bit);// (0 - 100)压缩文件
				// 图片已经改变
				picHasChanged = true;
				// 显示图片
				image.setImageBitmap(photo);
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	// 裁剪图片
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	// 生成选择相册、拍照弹框
	public void CreateDialog() {
		Builder builder = new AlertDialog.Builder(this);
		// 添加内容
		builder.setItems(getMethod, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 设置监听
				Intent intent;
				// 创建文件夹
				File file = FileUitlity.getInstance(getApplicationContext()).makeDir("good_image");
				// 定义图片路径和名称 File.separatorChar="/"
				path = file.getPath() + File.separatorChar + System.currentTimeMillis() + ".jpg";
				switch (which) {
				case 0:
					// 打开本地图片选择本地图片
					intent = new Intent(Intent.ACTION_PICK, null);
					intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
					startActivityForResult(intent, PHOTOZOOM);
					break;
				case 1:
					// 打开照相机拍照
					intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// 保存图片到Intent中，并通过Intent将照片传给系统裁剪器
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
					startActivityForResult(intent, PHOTOHRAPH);
					break;
				default:
					break;
				}
			}
		});
		// 创建弹框
		AlertDialog dialog = builder.create();
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		// 设置下方出现
		window.setGravity(Gravity.BOTTOM);
		// 基于下方高30
		lp.y = 30; // 新位置Y坐标
		window.setAttributes(lp);

		dialog.show();
	}

	// 定义一个处理字符串的方法
	private String getMoneyString(String money) {
		String overMoney = "";// 结果
		String[] pointBoth = money.split("\\.");// 分隔点前点后
		String beginOne = pointBoth[0].substring(pointBoth[0].length() - 1);// 前一位
		String endOne = pointBoth[1].substring(0, 1);// 后一位
		// 小数点前一位前面的字符串，小数点后一位后面
		String beginPoint = pointBoth[0].substring(0, pointBoth[0].length() - 1);
		String endPoint = pointBoth[1].substring(1);
		Log.e("Sun", pointBoth[0] + "===" + pointBoth[1] + "====" + beginOne + "=======" + endOne + "===>" + beginPoint
				+ "==" + endPoint);
		// 根据输入输出拼点
		if (pointBoth[1].length() > 2) {// 说明输入，小数点要往右移
			overMoney = pointBoth[0] + endOne + "." + endPoint;// 拼接实现右移动
		} else if (pointBoth[1].length() < 2) {// 说明回退,小数点左移
			overMoney = beginPoint + "." + beginOne + pointBoth[1];// 拼接实现左移
		} else {
			overMoney = money;
		}
		// 去除点前面的0 或者补 0
		String overLeft = overMoney.substring(0, overMoney.indexOf("."));// 得到前面的字符串
		Log.e("Sun", "左邊:" + overLeft + "===去零前" + overMoney);
		if (overLeft == null || overLeft == "" || overLeft.length() < 1) {// 如果没有就补零
			overMoney = "0" + overMoney;
		} else if (overLeft.length() > 1 && "0".equals(overLeft.subSequence(0, 1))) {// 如果前面有俩个零
			overMoney = overMoney.substring(1);// 去第一个0
		}
		Log.e("Sun", "結果:" + overMoney);
		return overMoney;
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