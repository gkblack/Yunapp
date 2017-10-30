package com.sssoft.Yundian.activity;

import com.sssoft.Yundian.R;
import com.sssoft.Yundian.Dao.DataDao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 *  登录页
 */

public class LoginActivity extends Activity implements OnClickListener {
	// 登录文本框
	private EditText etPhoneNumber;
	private EditText etPhonePassword;
	private CheckBox cbRememberMe; // 记住帐号、密码
	private CheckBox cbAutoLogin; // 自动登录
	// 存储帐号
	private SharedPreferences sPreferences;
	protected int finishCount = 0;
	// 账号
	private String phone;

	// 密码
	private String password;
	// 进度条
	private ProgressDialog pd = null;
	private String phoneValue;
	private String passwordValue;
	private Button btLogin;
	private TextView tvRegister;
	private TextView tvForget;
	private DataDao dao;
	private long exitTime = 0;
	private String noAutoLogin = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		tvRegister = (TextView) findViewById(R.id.tv_register);
		tvForget = (TextView) findViewById(R.id.tv_forget);
		etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
		etPhonePassword = (EditText) findViewById(R.id.et_Password);
		cbRememberMe = (CheckBox) findViewById(R.id.cb_remember_me);
		cbAutoLogin = (CheckBox) findViewById(R.id.cb_auto_login);

		// 获取退出或跳转到此界面所得值，用来取消登录界面会默认自动登录的问题
		Intent intentLogin = getIntent();
		noAutoLogin = intentLogin.getStringExtra("noAutoLogin");

		dao = new DataDao(this);
		// 获取保存在SharePreferences里面的账号信息，实现自动登录s
		sPreferences = getSharedPreferences("accountInfo", Context.MODE_WORLD_READABLE);
		if (sPreferences.getBoolean("ISCHECK", false)) {
			cbRememberMe.setChecked(true);
			// 获取保存的手机号
		        try{  
		        	phoneValue = sPreferences.getString("ACCOUNTVALUE", ""); 
		        }catch(Exception ex){  
		           
		        }  
		        try{  
		        	passwordValue = sPreferences.getString("PASSWORDVALUE", "");
		        }catch(Exception ex){  
		           
		        }  
		    //将账号和密码显示在EditText控件上。  
			etPhoneNumber.setText(phoneValue);
			etPhonePassword.setText(passwordValue);
			// 自动登录
			if (sPreferences.getBoolean("AUTO_ISCHECK", false)) {
				cbAutoLogin.setChecked(true);
				if ("1".equals(noAutoLogin)) {
					// 取消自动登录
				}  else {
					initLogin();
					
				}
			}

		}
		// 记住帐号密码
		cbRememberMe.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (cbRememberMe.isChecked()) {
					sPreferences.edit().putBoolean("ISCHECK", true).commit();							  
				} else {
					// System.out.println("记住账号框未选中");
					sPreferences.edit().putBoolean("ISCHECK", false).commit();
				}
			}
		});
		// 自动登录
		cbAutoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (cbAutoLogin.isChecked()) {
					// 开启自动登录功能
					sPreferences.edit().putBoolean("AUTO_ISCHECK", true).commit();
				} else {
					// 关闭自动登录
					sPreferences.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});
		btLogin = (Button) findViewById(R.id.bt_login);
		btLogin.setOnClickListener(this);

		// 跳转注册界面
		tvRegister.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
				startActivity(intent);
			}
		});
		// 跳转忘记密码界面
		tvForget.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
				startActivity(intent);
			}
		});

	}

	/**
	 * 重载的按钮点击事件
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_login:
			initLogin();
			break;
		default:
			break;
		}
	}

	// 初始化
	public void initLogin() {
		phone = etPhoneNumber.getText().toString();
		password = etPhonePassword.getText().toString();

		if (phone.length() == 0 || password.length() == 0) {
			Toast.makeText(LoginActivity.this, "请输入帐号或密码", Toast.LENGTH_SHORT).show();
		} else {
			// 登录进度条
			pd = new ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("登录中...");
			pd.show();
			if (cbRememberMe.isChecked()) {
				Editor editor = sPreferences.edit();
				try {
					editor.putString("ACCOUNTVALUE", phone);
//					System.out.println("账号" + phone);
				} catch (Exception e) {
					Toast.makeText(LoginActivity.this, "账号保存异常", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				try {
					editor.putString("PASSWORDVALUE", password);
//					System.out.println("密码" + password);
				} catch (Exception e) {
					Toast.makeText(LoginActivity.this, "密码保存异常", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				editor.commit();
			}
			loginCheck(phone, password);
		}
	}

	/**
	 * 登录验证
	 * 
	 */
	public void loginCheck(String phone, String password) {
		if (!dao.NameId(phone, password)) {
			Toast.makeText(LoginActivity.this, "帐号或密码错误", Toast.LENGTH_SHORT).show();
			pd.dismiss();
		} else if (dao.NameId(phone, password)) {
			Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 关闭页面时关闭进度条
		if (pd != null) {
			pd.dismiss();
		}
	}

	// 双击退出
	@Override
	public void onBackPressed() {
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
