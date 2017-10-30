package com.sssoft.Yundian.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sssoft.Yundian.R;
import com.sssoft.Yundian.Dao.DataDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ForgetActivity extends Activity {
	private DataDao dao;
	// 手机号
	private EditText etPhoneNum;
	// 输入验证码
	private EditText etInputVerificationCode;
	private EditText etNewPassword;
	private Button btVerifyCode;
	private Button btRegister;
	private ImageView btBack;

	private String phone;
	// 短信验证码
	private String verifyCode;
	private String newpassword;

	private String pattern;
	private Pattern r;
	private Matcher m;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forget);
		// 手机号限制为11位数字
		pattern = "^[1][0-9]{10}$";
		// 创建 Pattern 对象
		r = Pattern.compile(pattern);
		// 顶部返回图标
		btBack = (ImageView) findViewById(R.id.iv_back);
		btBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ForgetActivity.this.finish();
			}
		});

		dao = new DataDao(this);

		etPhoneNum = (EditText) findViewById(R.id.et_register_phone);
		etInputVerificationCode = (EditText) findViewById(R.id.et_input_verification_code);
		etNewPassword = (EditText) findViewById(R.id.et_newpassword);
		btVerifyCode = (Button) findViewById(R.id.bt_verification_code);

		// 验证码点击倒计时
		final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);
		// 给Button设置点击时间,触发倒计时
		btVerifyCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				phone = etPhoneNum.getText().toString();
				m = r.matcher(phone);
				if (m.find()) {
					if (!dao.CheckIsDataAlreadyInDBorNot(phone)) {
						Toast.makeText(ForgetActivity.this, "手机号未注册", Toast.LENGTH_SHORT).show();
					} else {
						myCountDownTimer.start();
					}
				} else {
					Toast.makeText(ForgetActivity.this, "手机号不正确!", Toast.LENGTH_SHORT).show();
				}

			}
		});

		// 提交按钮
		btRegister = (Button) findViewById(R.id.bt_register);
		btRegister.setOnClickListener(new Button.OnClickListener() {

			// 验证，将文本账号密码转换成字符串形式
			@Override
			public void onClick(View view) {
				phone = etPhoneNum.getText().toString();
				verifyCode = etInputVerificationCode.getText().toString();
				newpassword = etNewPassword.getText().toString();
				// 创建 matcher 对象
				m = r.matcher(phone);
				if (verifyCode.length() == 0) {
					Toast.makeText(ForgetActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
				} else if (m.find()) {
					if (dao.CheckIsDataAlreadyInDBorNot(phone)) {
						int a = dao.update(phone, newpassword);
						if (a == 1) {
							Toast.makeText(ForgetActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent();
							intent.putExtra("noAutoLogin", "1");
							intent.setClass(ForgetActivity.this, LoginActivity.class);
							startActivity(intent);
							ForgetActivity.this.finish();
						} else if (a == 0) {
							Toast.makeText(ForgetActivity.this, "新密码不能与原密码相同", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(ForgetActivity.this, "密码修改失败", Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					Toast.makeText(ForgetActivity.this, "手机号不正确!", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	// 复写倒计时
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
