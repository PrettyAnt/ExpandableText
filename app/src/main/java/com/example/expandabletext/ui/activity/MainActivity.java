package com.example.expandabletext.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expandabletext.R;
import com.example.expandabletext.ui.adapter.ExplandableAdapter;
import com.example.expandabletext.ui.widget.ExpandableTextViews;
import com.example.expandabletext.model.TestData;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ExpandableTextViews.OnExpandListener {

    private ArrayList<TestData>                                                   testDataList = new ArrayList<TestData>();
    private Button btnAdd;
    private Button btnSkip;
    private String[] stringArray;
    private ExplandableAdapter explandableAdapter;
    private RecyclerView recyclerView;
    private Button btn_skip_plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stringArray = getResources().getStringArray(R.array.poems_2);
        initView();
    }



    private boolean mIspland = false;
    private void initView() {

        btnAdd = findViewById(R.id.btn_add);
        btnSkip = findViewById(R.id.btn_skip);
        btn_skip_plus = findViewById(R.id.btn_skip_plus);


        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        explandableAdapter = new ExplandableAdapter(this,testDataList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(explandableAdapter);

        explandableAdapter.setOnExpandListener(this);
        btnAdd.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
        btn_skip_plus.setOnClickListener(this);



    }

    private int index = 0;
    private void initData() {
        Random random = new Random();
        int    i      = random.nextInt(10);
        TestData testData = new TestData();
        testData.setExpland(false);
        testData.setContent(index+"--->>>"+stringArray[i].toString());
        testDataList.add(testData);
        explandableAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(testDataList.size()-1);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            initData();
            index++;
        } else if (v.getId() == R.id.btn_skip) {
            Intent intent = new Intent(this, ActivityListView.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_skip_plus) {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onExpand(ExpandableTextViews view, boolean expland, int position) {
        testDataList.get(position).setExpland(expland);

    }
}