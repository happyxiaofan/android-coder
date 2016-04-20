package com.rhwayfun.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.rhwayfun.acitivity.MainActivity;
import com.rhwayfun.acitivity.QuestionActivity;
import com.rhwayfun.acitivity.R;
import com.rhwayfun.acitivity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/15.
 */
public class BaseActivity extends AppCompatActivity {

    private TextView headTitle;
    private Button[] btns = new Button[3];
    private Class[] targetActivities = new Class[]{MainActivity.class, QuestionActivity.class, SearchActivity.class};
    private int[] bottomImgs = new int[]{R.drawable.bottombtn01b, R.drawable.bottombtn02b, R.drawable.bottombtn03b};
    private int[] bottomImgsSelected = new int[]{R.drawable.bottombtn01a, R.drawable.bottombtn02a, R.drawable.bottombtn03a};
    private int i;

    //设置按钮
    private Button settingBtn;
    //弹出窗口
    private PopupWindow popupWindow;
    //所有活动的activity
    List<Activity> liveActivities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        liveActivities.add(this);

    }

    protected void init(String title, int index) {
        headTitle = (TextView) findViewById(R.id.head_title);
        headTitle.setText(title);

        btns[0] = (Button) findViewById(R.id.bottombtn_01);
        btns[1] = (Button) findViewById(R.id.bottombtn_02);
        btns[2] = (Button) findViewById(R.id.bottombtn_03);

        for (i = 0; i < btns.length; i++) {
            final int temp = i;
            if (index == i) {
                btns[i].setBackgroundResource(bottomImgsSelected[i]);
            } else {
                btns[i].setBackgroundResource(bottomImgs[i]);
                btns[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(BaseActivity.this, targetActivities[temp]);
                        startActivity(in);
                    }
                });
            }
        }

        settingBtn = (Button) findViewById(R.id.setting_btn);

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow = new PopupWindow(Globals.SCREEN_WIDTH / 4, Globals.SCREEN_HEIGHT / 6);
                View view = LayoutInflater.from(BaseActivity.this).inflate(R.layout.window_list, null);
                popupWindow.setContentView(view);
                //如果点击设置按钮时，浮动窗口正在展开，那么隐藏，否则显示
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    //在设置按钮下方展示
                    popupWindow.showAsDropDown(settingBtn);
                }

                TextView version = (TextView) view.findViewById(R.id.win_version);
                TextView about = (TextView) view.findViewById(R.id.win_about);
                TextView exit = (TextView) view.findViewById(R.id.win_exit);

                version.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        Toast.makeText(BaseActivity.this, "当前为1.0.1版本", Toast.LENGTH_LONG).show();
                    }
                });

                about.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
                        builder.setTitle("版权信息");
                        builder.setMessage("本应用所有权归rhwayfun所有");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create().show();
                    }
                });

                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        for (Activity a : liveActivities) {
                            a.finish();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        liveActivities.remove(this);
    }


}
