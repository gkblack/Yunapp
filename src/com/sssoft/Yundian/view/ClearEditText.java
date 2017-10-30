/**
 * 
 */
package com.sssoft.Yundian.view;

import com.sssoft.Yundian.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * EditText右侧删除控件
 * 
 * @author JKCoCo
 *
 */
public class ClearEditText extends EditText implements OnFocusChangeListener, TextWatcher {
	private Drawable mClearDrawable;
	private boolean hasFocus;

	public ClearEditText(Context context) {
		this(context, null);
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// 获取drawableRight的图片
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			// 如果为空，即没有设置drawableRight，使用删除图片
			mClearDrawable = getResources().getDrawable(R.drawable.ic_clear);
		}
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
		setOnFocusChangeListener(this);
		addTextChangedListener(this);
		// 默认隐藏图标
		setDrawableVisible(false);
	}

	// 通过按下的位置来模拟clear点击事件,按下的位置在图标到控件右边的间距范围内时触发
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {
				// 起始位置
				int start = getWidth() - getTotalPaddingRight() + getPaddingRight();
				// 结束位置
				int end = getWidth();
				boolean available = (event.getX() > start) && (event.getX() < end);
				if (available) {
					this.setText("");
				}
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFocus = hasFocus;
		if (hasFocus && getText().length() > 0) {
			// 有焦点且有输入值时显示图标
			setDrawableVisible(true);
		} else {
			setDrawableVisible(false);
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (hasFocus) {
			setDrawableVisible(s.length() > 0);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
	}

	protected void setDrawableVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

}
