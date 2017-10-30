package com.sssoft.Yundian.view;

import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * 
 * 左侧菜单View实现
 * 
 * @author Jiang 2017.10.11
 * 
 */
public class slidingMenu extends HorizontalScrollView {
	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;

	/**
	 * 屏幕宽度
	 */
	private int mScreenWidth;

	private int mMenuWidth = 50;
	/**
	 * 菜单与屏幕右侧的距离 db
	 * 
	 */
	private int mMenuRightPadding = 50;

	private boolean once = false;

	private boolean isOpen;

	/**
	 * 未使用自定义组件调用
	 * 
	 * @param context
	 * @param attrs
	 */
	public slidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, -1);

	}

	/**
	 * 当自定义属性时，会调用次构造方法
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public slidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);

		// 屏幕宽度的像素值
		mScreenWidth = outMetrics.widthPixels;

		// 把120dp转化为像素值px
		mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120,
				context.getResources().getDisplayMetrics());

		// // 获取定义的属性
		// TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
		// R.styleable.SlidingMenu, defStyle, 0);
		// int n = a.getIndexCount();// 属性个数
		// // 遍历查找属性
		// for (int i = 0; i < n; i++) {
		// int attr = a.getIndex(i);
		// switch (attr) {
		// case R.styleable.SlidingMenu_rightPadding:
		// mMenuRightPadding = a.getDimensionPixelOffset(attr,
		// (int) TypedValue.applyDimension(
		// TypedValue.COMPLEX_UNIT_DIP, 50, context
		// .getResources().getDisplayMetrics()));
		// break;
		//
		// default:
		// break;
		// }
		// }
		// a.recycle();

	}

	public slidingMenu(Context context) {
		this(context, null);
	}

	/**
	 * 设置子view的宽和高 设置自己的宽高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		if (!once) {
			mWapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mWapper.getChildAt(0);
			mContent = (ViewGroup) mWapper.getChildAt(1);

			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
			mContent.getLayoutParams().width = mScreenWidth;
			once = true;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 通过设置偏移量，将menu隐藏
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// 将menu隐藏
			this.scrollTo(mMenuWidth, 0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		// 用户手指操作，按下，抬起等
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			// Up时，进行判断，如果显示区域大于菜单宽 度一半则完全显示，否则隐藏
			int scrollX = getScrollX();

			if (scrollX >= mMenuWidth / 2) {
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			} else {
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			return true;
		}
		


		return super.onTouchEvent(ev);

	}
	
	
	/**
	 * 滚动发生时
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / mMenuWidth; // 1 ~ 0

		/**
		 * 区别1：内容区域1.0~0.7 缩放的效果 scale : 1.0~0.0 0.7 + 0.3 * scale
		 * 
		 * 区别2：菜单的偏移量需要修改
		 * 
		 * 区别3：菜单的显示时有缩放以及透明度变化 缩放：0.7 ~1.0 1.0 - scale * 0.3 透明度 0.6 ~ 1.0 
		 * 0.6+ 0.4 * (1- scale) ;
		 * 
		 */
		float rightScale = 0.7f + 0.3f * scale;
		float leftScale = 1.0f - scale * 0.3f;
		float leftAlpha = 0.6f + 0.4f * (1 - scale);

		// 调用属性动画，设置TranslationX
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.8f);
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, leftAlpha);
		// 设置content的缩放的中心点
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);
	}

	/**
	 * 
	 */

	public void openMenu() {
		if (isOpen)
			return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	public void closeMenu() {
		if (!isOpen)
			return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = false;

	}

	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

}
