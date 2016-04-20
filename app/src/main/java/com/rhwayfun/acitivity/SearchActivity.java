package com.rhwayfun.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rhwayfun.util.BaseActivity;


public class SearchActivity extends BaseActivity {

    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        super.init("搜索问题", 2);

        editText = (EditText) findViewById(R.id.search_content);
        button = (Button) findViewById(R.id.search_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SearchActivity.this,QuestionActivity.class);
                in.putExtra("keyword",editText.getText().toString());
                startActivity(in);
            }
        });
    }
}
