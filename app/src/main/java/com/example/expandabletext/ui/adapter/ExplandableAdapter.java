package com.example.expandabletext.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expandabletext.ui.widget.ExplandableTextView;
import com.example.expandabletext.R;
import com.example.expandabletext.ui.widget.ExpandableTextViews;
import com.example.expandabletext.ui.hodler.ExplandableHodler;
import com.example.expandabletext.model.TestData;

import java.util.ArrayList;

/**
 * @author chenyu
 * My personal blog  https://prettyant.github.io
 * <p>
 * Created on 11:58 PM  12/05/21
 * PackageName : com.example.expandabletext.ui.adapter
 * describle :
 */
public class ExplandableAdapter extends RecyclerView.Adapter<ExplandableHodler> {
    private Activity                             activity;
    private ArrayList<TestData>                  testDataList;
    private ExpandableTextViews.OnExpandListener       onExpandListener;
    private ExplandableTextView.OnExplandClickListener onExplandClickListener;

    public ExplandableAdapter(Activity activity, ArrayList<TestData> testDataList) {
        this.activity = activity;
        this.testDataList = testDataList;
    }

    @NonNull
    @Override
    public ExplandableHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View              inflate           = LayoutInflater.from(activity).inflate(R.layout.item_testlist, parent, false);
        ExplandableHodler explandableHodler = new ExplandableHodler(inflate, onExpandListener);
        return explandableHodler;
    }

    int width;

    @Override
    public void onBindViewHolder(@NonNull ExplandableHodler holder, int position) {
        ExpandableTextViews etv_text_accept = holder.etv_text_accept;
        ExplandableTextView etv_sxd         = holder.etv_sxd;

        TestData testData = testDataList.get(position);
        String   content  = testData.getContent();
        boolean  expland  = testData.isExpland();

        if (width == 0) {
            etv_text_accept.post(new Runnable() {
                @Override
                public void run() {
                    width = etv_text_accept.getWidth();
                }
            });
        }

        etv_text_accept.setExpandListener(onExpandListener, position);
        etv_text_accept.updateForRecyclerView(content, width, expland ? 1 : 0);


        if (width == 0) {
            etv_sxd.post(new Runnable() {
                @Override
                public void run() {
                    width = etv_sxd.getWidth();
                }
            });
        }
        etv_sxd.setOnExplandClickListener(onExplandClickListener,position);
        etv_sxd.updateForRecyclerView(content, width, expland ? 1 : 0);

    }

    @Override
    public int getItemCount() {
        return testDataList.size();
    }

    public void setOnExpandListener(ExpandableTextViews.OnExpandListener onExpandListener) {
        this.onExpandListener = onExpandListener;
    }

    public void setOnExplandClickListener(ExplandableTextView.OnExplandClickListener onExplandClickListener) {
        this.onExplandClickListener = onExplandClickListener;
    }
}
