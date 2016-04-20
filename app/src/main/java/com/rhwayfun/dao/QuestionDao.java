/*
package com.rhwayfun.dao;

import android.database.Cursor;
import android.util.Log;

import com.rhwayfun.util.Globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by rhwayfun on 16-4-17.
 *//*

public class QuestionDao {

    //插入数据
    public static void insert(Map<String, Object> data) {
        String sql = "insert into question_answer (question,answer) values (?,?)";
        Globals.baseDao.getWritableDatabase().execSQL(sql,
                new Object[]{data.get("question").toString(),
                        data.get("answer").toString()});
    }

    //读取数据
    public static List<Map<String, Object>> get() {
        String sql = "select id,question,answer from question_answer";
        List<Map<String, Object>> dataList = new ArrayList<>();
        Cursor cursor = Globals.baseDao.getReadableDatabase().rawQuery(sql, null);
        //将当前游标移动到第一条记录的位置
        cursor.moveToFirst();
        Log.d("提示信息", "read " + cursor.getCount() + " records");
        while (!cursor.moveToLast()) {
            Map<String, Object> map = new HashMap<>();
            map.put("question", cursor.getInt(0) + ". " + cursor.getString(1));
            map.put("answer", cursor.getString(2));
            dataList.add(map);

            //移到下一条记录
            cursor.moveToNext();
        }
        //关闭游标
        cursor.close();
        return dataList;
    }

    //分页查询数据
    public static List<Map<String, Object>> getByPage(int pageNum, int pageSize, String keyword) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        String sql = "select id,question,answer from question_answer where question like ? limit ?,?";
        int start = pageNum * pageSize - pageSize;
        Cursor cursor = Globals.baseDao.getReadableDatabase().rawQuery(sql,
                new String[]{"%" + keyword + "%", start + "", pageSize + ""});
        cursor.moveToFirst();
        Log.d("提示信息", "read page " + cursor.getCount() + " records");
        while (!cursor.moveToLast()){
            Map<String,Object> map = new HashMap<>();
            map.put("question", cursor.getInt(0) + ". " + cursor.getString(1));
            map.put("answer", cursor.getString(2));

            dataList.add(map);
            cursor.moveToNext();
        }
        cursor.close();
        return dataList;
    }

    //获取总记录数
    public static int getCount(){
        String sql = "select count(*) from question_answer";
        int recordCount = 0;
        Cursor cursor = Globals.baseDao.getReadableDatabase().rawQuery(sql,null);
        cursor.moveToFirst();
        recordCount = cursor.getInt(0) > 0 ? cursor.getInt(0) : 0;
        cursor.close();
        return recordCount;
    }

    //删除全部数据
    public static void delete(){
        String sql = "delete from question_answer";
        Globals.baseDao.getWritableDatabase().execSQL(sql);
    }

    public static int getCount(String keyword) {
        String sql = "select count(*) from question_answer where question like ?";
        int recordCount = 0;
        Cursor cursor = Globals.baseDao.getReadableDatabase().rawQuery(sql,new String[]{"%"+ keyword +"%"});
        cursor.moveToFirst();
        recordCount = cursor.getInt(0) > 0 ? cursor.getInt(0) : 0;
        cursor.close();
        return recordCount;
    }
}
*/
package com.rhwayfun.dao;

import android.database.Cursor;
import android.util.Log;

import com.rhwayfun.util.Globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/17.
 */
public class QuestionDao {

    //插入数据
    public static void insertData(Map<String, Object> map) {
        String sql = "insert into question_answer (question, answer) values (?, ?)";
        Globals.util.getWritableDatabase().execSQL(sql, new Object[]{map.get("question").toString(), map.get("answer").toString()});
    }

    //查询全部
    public static List<Map<String, Object>> loadData() {
        List<Map<String, Object>> values = new ArrayList<>();
        String sql = "select id, question, answer from question_answer";
        Cursor c = Globals.util.getReadableDatabase().rawQuery(sql, null);
        c.moveToFirst();
        Log.d("提示信息", c.getCount() + "=============");
        while (!c.isAfterLast()) {
            Map<String, Object> map = new HashMap<>();
            map.put("question", c.getInt(0) + " " + c.getString(1));
            map.put("answer", c.getString(2));
            map.put("showFlag",false);
            values.add(map);
            c.moveToNext();
        }
        c.close();
        return values;
    }

    //分页查询
    public static List<Map<String, Object>> loadDataPage(int pagenum, int pagesize, String keyword) {
        List<Map<String, Object>> values = new ArrayList<>();
        String sql = "select id, question, answer from question_answer where question like ? limit ?, ?";
        Cursor c = Globals.util.getReadableDatabase().rawQuery(sql, new String[]{"%" + keyword + "%", pagesize * pagenum - pagesize + "", pagesize + ""});
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Map<String, Object> map = new HashMap<>();
            map.put("question", c.getInt(0) + " " + c.getString(1));
            map.put("answer", c.getString(2));
            map.put("showFlag",false);
            values.add(map);
            c.moveToNext();
        }
        c.close();
        return values;
    }

    //查询全部记录数
    public static int getCount(String keyword) {
        int recordCount = 0;
        String sql = "select count(*) from question_answer where question like ?";
        Cursor c = Globals.util.getReadableDatabase().rawQuery(sql, new String[]{"%" + keyword + "%"});
        c.moveToFirst();
        recordCount = c.getInt(0);
        c.close();
        return recordCount;
    }

    //删除全部
    public static void deleteAllData() {
        String sql = "delete from question_answer";
        Globals.util.getWritableDatabase().execSQL(sql);
    }
}
