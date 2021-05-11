package com.example.expandabletext;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ExpandableTextView2 etv;
    private ExplandableTextView explandTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        etv = (ExpandableTextView2) findViewById(R.id.etv);
        explandTextView = findViewById(R.id.expland_textview);
        String string = getResources().getString(R.string.poem_0);
        explandTextView.setText(string);
    }

}