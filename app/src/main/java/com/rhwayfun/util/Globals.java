package com.rhwayfun.util;

import android.app.Activity;
import android.util.Log;

import com.rhwayfun.dao.BaseDao;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/15.
 */
public class Globals {

    public static List<Map<String, Object>> values;
    public static BaseDao util;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    //全局初始化
    public static void init(Activity activity){
        util = new BaseDao(activity);
        SCREEN_HEIGHT = activity.getWindowManager().getDefaultDisplay().getHeight();
        SCREEN_WIDTH = activity.getWindowManager().getDefaultDisplay().getWidth();
        Log.d("Coder","初始化成功！");
    }
}
