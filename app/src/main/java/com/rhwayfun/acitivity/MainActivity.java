package com.rhwayfun.acitivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.rhwayfun.adapter.MyAdapter;
import com.rhwayfun.adapter.MyPagerAdapter;
import com.rhwayfun.dao.QuestionDao;
import com.rhwayfun.util.Globals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<View> viewList;
    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;
    private String[] allTitles = new String[]{"程序员面试宝典","全部问题","搜索问题"};

    /***********************************/

    // init方法用
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

    /************************************/

    // initQuestionList方法用
    //显示数据使用
    private ListView list;
    private List<Map<String, Object>> values;
    private MyAdapter adapter;

    //分页使用
    private int pagenum = 1;
    private int pagesize = 10;
    private int recordCount;
    private int pageCount;
    private String keyword;

    //list的onScroll事件使用
    private int first;
    private int visible;
    private int total;

    /************************************/
    // initSearchProcess方法用
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init("程序员面试宝典", 0);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewList = new ArrayList<>();
        View main = LayoutInflater.from(this).inflate(R.layout.page_main,null);
        View question = LayoutInflater.from(this).inflate(R.layout.page_question,null);
        View search = LayoutInflater.from(this).inflate(R.layout.page_search,null);
        viewList.add(main);
        viewList.add(question);
        viewList.add(search);

        // init question list
        initQuestionList(question);
        // init search process
        initSearchProcess(search);

        viewPager.setOffscreenPageLimit(3);
        pagerAdapter = new MyPagerAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // make head title and bottom background
                // adapt to different pager while sliding
                headTitle.setText(allTitles[position]);
                for (int i = 0; i < btns.length; i++){
                    if (position == i){
                        btns[i].setBackgroundResource(bottomImgsSelected[i]);
                    }else {
                        btns[i].setBackgroundResource(bottomImgs[i]);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void initSearchProcess(View searchView) {
        editText = (EditText) searchView.findViewById(R.id.search_content);
        button = (Button) searchView.findViewById(R.id.search_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagenum = 1;
                keyword = editText.getText().toString();
                values.clear();
                values.addAll(QuestionDao.loadDataPage(pagenum, pagesize, keyword));
                adapter.notifyDataSetChanged();
                //滑动界面
                viewPager.setCurrentItem(1,true);
            }
        });
    }

    public void initQuestionList(View questionView) {
        list = (ListView) questionView.findViewById(R.id.list);

        if(keyword==null) {
            keyword = "";
        }
        //分页查询数据
        values = QuestionDao.loadDataPage(pagenum, pagesize, keyword);
        //查询总记录数
        recordCount = QuestionDao.getCount(keyword);
        //总页数
        pageCount = (recordCount - 1) / pagesize + 1;

        //加入尾部footerView
        final TextView footerView = new TextView(this);
        footerView.setText("正在查询数据，请稍候");
        footerView.setTextSize(12);
        footerView.setTextColor(Color.BLACK);
        list.addFooterView(footerView);
        //adapter = new MyAdapter(this, values, R.layout.question_list, new String[]{"question"}, new int[]{R.id.list_question});
        adapter = new MyAdapter(this,values);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = values.get(position);
                LinearLayout linear = (LinearLayout) view;
                //linear.getChildAt(1).setVisibility(View.VISIBLE);
                if (linear.getChildCount() == 1) {
                    //如果当前只有一行，加入答案
                    TextView answerView = new TextView(MainActivity.this);
                    answerView.setText(map.get("answer").toString());
                    answerView.setTextSize(14);
                    answerView.setTextColor(Color.RED);
                    linear.addView(answerView);
                    map.put("showFlag",true);
                } else {
                    //否则删除答案行
                    linear.removeViewAt(1);
                    map.put("showFlag",false);
                }

            }
        });

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && first + visible == total && total != 0) {
                    if(pagenum<pageCount) {
                        pagenum++;
                        //将分页数据加入到原有的数据上
                        values.addAll(QuestionDao.loadDataPage(pagenum, pagesize, keyword));
                        //通知
                        adapter.notifyDataSetChanged();
                    }else {
                        //如果到底部，去除footerView
                        if(list.getFooterViewsCount()>0) {
                            list.removeFooterView(footerView);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                first = firstVisibleItem;
                visible = visibleItemCount;
                total = totalItemCount;
            }
        });
    }

    // 原BaseActivity的init方法
    public void init(String title, int index) {
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
            }
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(temp,true);
                }
            });
        }

        settingBtn = (Button) findViewById(R.id.setting_btn);

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow = new PopupWindow(Globals.SCREEN_WIDTH / 4, Globals.SCREEN_HEIGHT / 6);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.window_list, null);
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
                        Toast.makeText(MainActivity.this, "当前为1.0.1版本", Toast.LENGTH_LONG).show();
                    }
                });

                about.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                        finish();
                    }
                });
            }
        });
    }
}
