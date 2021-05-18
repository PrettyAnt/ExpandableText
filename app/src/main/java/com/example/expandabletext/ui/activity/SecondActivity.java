package com.example.expandabletext.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expandabletext.R;
import com.example.expandabletext.ui.widget.ExplandableTextView;

/**
 * 可展开、收起的TextView 进阶版
 */
public class SecondActivity extends AppCompatActivity implements ExplandableTextView.OnExplandClickListener {

    private ExplandableTextView explandTextView;
    private String[]            stringArray;
    private boolean             expland = false;
    private String              data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        stringArray = getResources().getStringArray(R.array.poems_2);
        data = getResources().getString(R.string.poem_0);
        initView();
    }


    private boolean mIspland = false;

    private void initView() {
        explandTextView = findViewById(R.id.explandview);
        explandTextView.setText(data);
        explandTextView.setOnExplandClickListener(this, 11);
    }

    @Override
    public void onExpand(View view, boolean expland, int position) {

    }
}