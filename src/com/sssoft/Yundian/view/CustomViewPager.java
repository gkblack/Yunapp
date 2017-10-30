package com.sssoft.Yundian.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 滑动菜单view
 * 
 * @author Jiang 2017.10.11
 *
 */
@SuppressLint("ClickableViewAccessibility")
public class CustomViewPager extends ViewPager {

	public CustomViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 自适应高度的ViewPager
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// int height = 0;
		// for (int i = 0; i < getChildCount(); i++) {
		// View child = getChildAt(i);
		// child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0,
		// MeasureSpec.UNSPECIFIED));
		// int h = child.getMeasuredHeight();
		// if (h > height)
		// height = h;
		// }
		//
		// heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/*
	 * slidingMenu 嵌套viewPager 手势冲突解决方案
	 * 
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// x,y位置
		int[] position = new int[2];
		getLocationOnScreen(position);
		if (position[0] > 20) {
			super.dispatchTouchEvent(ev);

		} else {
			// 剥夺父view 对touch 事件的处理权
			getParent().requestDisallowInterceptTouchEvent(true);
			super.dispatchTouchEvent(ev);
		}
		Log.i("p_x",position[0]+"");
		return true;
	}

}
