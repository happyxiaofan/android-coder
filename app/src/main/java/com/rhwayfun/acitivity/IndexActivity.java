package com.rhwayfun.acitivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rhwayfun.dao.QuestionDao;
import com.rhwayfun.util.Globals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Globals.init(this);
        setContentView(R.layout.activity_index);
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    //从assets文件夹中获取question.txt的InputStream
                    InputStream is = getAssets().open("abc.txt");
                    //Globals.values = loadInputStream(is);
                    //加入一个标识位表示是否是第一次访问
                    SharedPreferences preferences = getSharedPreferences("question_flag",MODE_PRIVATE);
                    //创建一个默认值是false法saved变量
                    boolean saved = preferences.getBoolean("flag",false);
                    if (saved){
                        sleep(2000);
                    }else {
                        QuestionDao.deleteAllData();
                        loadInputStream(is);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("flag",true);
                        editor.commit();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent in = new Intent(IndexActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        };
        t.start();
    }

    //读取文件流，并将数据放到集合中返回
    public List<Map<String, Object>> loadInputStream(InputStream is) throws IOException {
        List<Map<String, Object>> values = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        //存放问题
        StringBuffer question = new StringBuffer();
        //存放答案
        StringBuffer answer = new StringBuffer();

        boolean questionFlag = false;
        while((line=br.readLine())!=null) {
            if(line.equals("Start_Question_Flag")) {
                //开始读问题
                questionFlag  = true;
            }else if(line.equals("Start_Answer_Flag")) {
                //开始读答案
                questionFlag = false;
            }else if(line.equals("End")) {
                //读取结束，需要整合问题和答案并放到集合中去
                Map<String, Object> map = new HashMap<>();
                map.put("question", question.toString());
                map.put("answer", answer.toString());
                //values.add(map);
                QuestionDao.insertData(map);

                //清空question和answer
                question = new StringBuffer();
                answer = new StringBuffer();
            }else {
                //读取数据
                if(questionFlag) {
                    question.append(line);
                }else {
                    answer.append(line);
                }
            }
        }
        return values;
    }
}
