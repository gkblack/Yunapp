package com.sssoft.Yundian.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sssoft.Yundian.R;
import com.sssoft.Yundian.Dao.DataDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 注册页
 * 
 * @author JKCoCo
 * 
 */
public class SigninActivity extends Activity {

	// 手机号
	private EditText etPhone;
	// 密码
	private EditText etPassword;
	// 验证码
	private EditText etVerifyCode;
	// 再次确认密码
	private EditText etRePassword;
	private DataDao dao;
	private Button btReigster;
	// 跳转登录界面
	private TextView tvLogin;
	// 获取验证码
	private Button btVerifyCode;
	private ImageView ivBack;

	private String phone;
	private String verifyCode;
	private String password;
	private String rePassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signin);
		dao = new DataDao(this);
		etPhone = (EditText) findViewById(R.id.et_phone_number);
		etVerifyCode = (EditText) findViewById(R.id.et_verify_code);
		etPassword = (EditText) findViewById(R.id.et_password);
		etRePassword = (EditText) findViewById(R.id.et_re_password);

		btReigster = (Button) findViewById(R.id.bt_register);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvLogin = (TextView) findViewById(R.id.tv_login);
		btVerifyCode = (Button) findViewById(R.id.tv_verify_code);
		// 返回按钮
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SigninActivity.this.finish();
			}
		});
		// 跳转登录界面
		tvLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("noAutoLogin", "1");
				intent.setClass(SigninActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// 验证码
		// new倒计时对象,总共的时间,每隔多少秒更新一次时间
		final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);
		// 设置点击时间,触发倒计时
		btVerifyCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String phone = etPhone.getText().toString();
				if (phone.length() == 0) {
					Toast.makeText(SigninActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
				} else if (phone.length() == 11 && !dao.CheckIsDataAlreadyInDBorNot(phone)) {
					myCountDownTimer.start();
				} else if (dao.CheckIsDataAlreadyInDBorNot(phone)) {
					Toast.makeText(SigninActivity.this, "号码已被注册", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SigninActivity.this, "手机号不正确", Toast.LENGTH_SHORT).show();
				}
			}
		});
		// 注册验证
		btReigster.setOnClickListener(new Button.OnClickListener() {

			// 验证，将文本账号密码转换成字符串形式
			@Override
			public void onClick(View view) {
				phone = etPhone.getText().toString();
				verifyCode = etVerifyCode.getText().toString();
				password = etPassword.getText().toString();
				rePassword = etRePassword.getText().toString();
				// 限制手机号只能为数字
				String pattern = "^[1][0-9]{10}$";
				// 创建 Pattern 对象
				Pattern r = Pattern.compile(pattern);
				// 创建 matcher 对象
				Matcher m = r.matcher(phone);
				// 登录判断

				if (m.find()) {
					if (dao.CheckIsDataAlreadyInDBorNot(phone)) {
						Toast.makeText(SigninActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
					} else if (verifyCode.length() == 0) {
						Toast.makeText(SigninActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
					} else if (password.length() < 6) {
						Toast.makeText(SigninActivity.this, "密码长度不能小于6", Toast.LENGTH_SHORT).show();
					} else if (!(password.length() == rePassword.length())) {
						Toast.makeText(SigninActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
					} else if (password.length() == rePassword.length()) {
						if (dao.register(phone, password)) {
							Toast.makeText(SigninActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
							startActivity(intent);
							finish();
						}
					}
				} else if (phone.length() == 0) {
					Toast.makeText(SigninActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SigninActivity.this, "手机号不正确!", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	// 验证码倒计时
	private class MyCountDownTimer extends CountDownTimer {

		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		// 计时过程
		@Override
		public void onTick(long l) {
			// 防止计时过程中重复点击
			btVerifyCode.setClickable(false);
			btVerifyCode.setText("倒计时" + l / 1000 + "s");

		}

		// 计时完毕的方法
		@Override
		public void onFinish() {
			// 重新给Button设置文字
			btVerifyCode.setText("重新获取验证码");
			// 设置可点击
			btVerifyCode.setClickable(true);
		}
	}
}
