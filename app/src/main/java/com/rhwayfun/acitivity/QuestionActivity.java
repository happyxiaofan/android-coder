/*
package com.rhwayfun.acitivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.rhwayfun.other.QuestionDao;
import com.rhwayfun.util.BaseActivity;

import java.util.List;
import java.util.Map;

public class QuestionActivity extends BaseActivity {

    private ListView list;
    private List<Map<String, Object>> values;
    private SimpleAdapter adapter;

    //分页数据
    private int pageNum = 1;
    private int pageSize = 10;
    private int pageCount;
    private int recordCount;
    private String keyword = null;


    private int first;
    private int visible;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        super.init("全部问题", 1);

        list = (ListView) findViewById(R.id.list);

        */
/*keyword = getIntent().getStringExtra("keyword").toString();
        if (keyword == null) {
            keyword = "";
        }*//*


        //values = Globals.values;
        keyword = "";
        values = QuestionDao.loadDataPage(pageNum, pageSize, keyword);
        Log.d("提示信息","共有 " + values.size() +" 条数据");
        recordCount = QuestionDao.getCount(keyword);
        pageCount = (recordCount - 1) / pageSize + 1;

        final TextView footView = new TextView(this);
        footView.setText("数据加载中...");
        footView.setTextSize(14);
        footView.setTextColor(Color.BLACK);
        list.addFooterView(footView);

        adapter = new SimpleAdapter(this, values, R.layout.question_list, new String[]{"question"}, new int[]{R.id.list_question});
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = values.get(position);
                LinearLayout linear = (LinearLayout) view;
                //linear.getChildAt(1).setVisibility(View.VISIBLE);
                if (linear.getChildCount() == 1) {
                    //如果当前只有一行，加入答案
                    TextView answerView = new TextView(QuestionActivity.this);
                    answerView.setText(map.get("answer").toString());
                    answerView.setTextSize(14);
                    answerView.setTextColor(Color.RED);
                    linear.addView(answerView);
                } else {
                    //否则删除答案行
                    linear.removeViewAt(1);
                }
            }
        });

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && first + visible == total && total != 0) {
                    if (pageNum < pageCount) {
                        pageNum++;
                        List<Map<String, Object>> list = QuestionDao.loadDataPage(pageNum, pageSize, keyword);
                        values.addAll(list);
                        adapter.notifyDataSetChanged();
                    } else {
                        if (list.getFooterViewsCount() > 0){
                            list.removeFooterView(footView);
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


}
*/

package com.rhwayfun.acitivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.rhwayfun.dao.QuestionDao;
import com.rhwayfun.util.BaseActivity;

import java.util.List;
import java.util.Map;

public class QuestionActivity extends BaseActivity {
    //显示数据使用
    private ListView list;
    private List<Map<String, Object>> values;
    private SimpleAdapter adapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        super.init("全部问题", 1);

        list = (ListView) findViewById(R.id.list);

        keyword = getIntent().getStringExtra("keyword");
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
        adapter = new SimpleAdapter(this, values, R.layout.question_list, new String[]{"question"}, new int[]{R.id.list_question});
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = values.get(position);
                LinearLayout linear = (LinearLayout) view;
                //linear.getChildAt(1).setVisibility(View.VISIBLE);
                if (linear.getChildCount() == 1) {
                    //如果当前只有一行，加入答案
                    TextView answerView = new TextView(QuestionActivity.this);
                    answerView.setText(map.get("answer").toString());
                    answerView.setTextSize(14);
                    answerView.setTextColor(Color.RED);
                    linear.addView(answerView);
                } else {
                    //否则删除答案行
                    linear.removeViewAt(1);
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
}
