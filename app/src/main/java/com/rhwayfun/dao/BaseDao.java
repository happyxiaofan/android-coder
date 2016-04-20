package com.rhwayfun.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rhwayfun on 16-4-17.
 */
public class BaseDao extends SQLiteOpenHelper {

    private static final String DB_NAME = "coder.db";
    private static final int VERSION = 1;

    public BaseDao(Context context){
        this(context,DB_NAME,null,VERSION);
    }

    public BaseDao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("提示信息","开始创建数据库...");
        //创建表
        String sql = "create table question_answer(" +
                "     id integer primary key," +
                "      question text," +
                "       answer text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
