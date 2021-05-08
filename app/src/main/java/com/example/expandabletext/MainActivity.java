package com.example.expandabletext;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ExplandableTextView.OnExplandClickListener {

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

//        explandTextView.setHalfText(string);
        explandTextView.setOnExplandClickListener(this);
    }

    @Override
    public void onExpland(View view) {
//        String string = getResources().getString(R.string.poem_0);
//        explandTextView.setText(string);
    }
}