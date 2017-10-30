package com.sssoft.Yundian.utils;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class others {
	
	
	//获取日期
	@SuppressLint("SimpleDateFormat")
	public static String getDate() {
		Date date = new Date();
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
        String now_date = df2.format(date);
        return now_date;
	}
	
}
