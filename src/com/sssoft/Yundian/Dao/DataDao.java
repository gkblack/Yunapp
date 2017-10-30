package com.sssoft.Yundian.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataDao {
    private MyHelper helper;

    public DataDao(Context context){
        helper = new MyHelper(context);
    }
    //验证

    ////插入数据
    public boolean register(String username,String password){
        SQLiteDatabase db= helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("user_phone",username);
        values.put("user_passwd",password);
        db.insert("user",null,values);
        db.close();
        return true;
    }
//修改数据
	public int update(String phone, String newpassword) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from user where user_phone=? ", new String[] {phone});
		if(cursor.getCount()>0){
			Cursor cur = db.rawQuery("select * from user where user_phone=? and user_passwd=?", new String[] {phone,newpassword});
			if(cur.getCount()>0){
				cur.close();
				return 0;	//与原密码重复
			}else{
				ContentValues values = new ContentValues();
				values.put("user_passwd", newpassword);
				db.update("user", values, "user_phone=?", new String[] { phone });
				cursor.close();
				cur.close();
				db.close();
				return 1;	//成功修改密码
			}
		
		}else {
			return 2;	//修改密码失败
		}
		
	}
 
    
    //检查用户名是否存在
    public boolean CheckIsDataAlreadyInDBorNot(String value){
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor = db.query("user", null, "user_phone=?", new String[]{ value },null,null,null);
        if (cursor.getCount()>0){
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }
  
    
    //验证名字跟密码
    public boolean NameId(String username,String password) {
        Boolean result=false;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("user", new String[]{ "user_passwd" }, "user_phone=?", new String[]{ username },null,null,null);
        if(cursor.moveToNext()) {
            String password1 = cursor.getString(cursor.getColumnIndex("user_passwd"));
            if(password1.equals(password)) {
                result = true;
            }
        }
        cursor.close();
        db.close();
        return result;
    }
}

