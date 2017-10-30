/**
 * 
 */
package com.sssoft.Yundian.activity;

import java.util.LinkedList;
import java.util.List;

import com.sssoft.Yundian.R;
import com.sssoft.Yundian.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
/**
* @author Rao
* @version 创建时间：2017年10月24日
*  设置页
*/
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener {

	private Button btSignout;
	private ImageView ivBack;
	private RelativeLayout rlMyAccount;
	private RelativeLayout rlMySales;
	private static List<Activity> activitiyList = new LinkedList<Activity>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		initView();
	}

	public void initView() {
		rlMyAccount = (RelativeLayout) findViewById(R.id.rl_myaccount);
		rlMySales = (RelativeLayout) findViewById(R.id.rl_mysales);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btSignout = (Button) findViewById(R.id.bt_signout);
		// 退出登录，退出到登录页
		btSignout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("noAutoLogin", "1");
				intent.setClass(SettingActivity.this, LoginActivity.class);
				MainActivity.activity.finish(); // 销毁主界面
				startActivity(intent);
				finish();
			}
		});
		rlMyAccount.setOnClickListener(this);
		rlMySales.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_myaccount:
			
			break;

		case R.id.rl_mysales:
			break;
		default:
			break;
		}
	}

	/**
	 * 取得Acitity 并添加到List中
	 */
	public static void addActivity(Activity activity) {
		activitiyList.add(activity);
	}

	/**
	 * 遍历并finish掉所有List中的Activity
	 */
	public static void finishActivity() {

		// for (Activity activity : activitiyList) {
		// activity.finish();
		// }
		/**
		 * 判空
		 */
		if (activitiyList.size() == 0) {
			activitiyList.clear();
		}
	}
}
