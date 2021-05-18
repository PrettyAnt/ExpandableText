package com.example.expandabletext.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expandabletext.R;
import com.example.expandabletext.model.TestData;
import com.example.expandabletext.ui.widget.ExpandableTextViews;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by carbs on 2016/7/23.
 */
public class ActivityListView extends AppCompatActivity implements View.OnClickListener {

    private ListView       mListView;
    private TheBaseAdapter mAdapter;
    private Button         mButton;

    private List<TestData> mStrings = new ArrayList<>();
    private boolean        mFlag    = true;
    private CharSequence[] mPoems;
    private CharSequence[] mProses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        mListView = (ListView) this.findViewById(R.id.listview);
        mButton = (Button) this.findViewById(R.id.button_update_list);

        mPoems = getResources().getStringArray(R.array.poems_2);
        mProses = getResources().getStringArray(R.array.poems_2);

        mAdapter = new TheBaseAdapter(this, mStrings);
        mListView.setAdapter(mAdapter);
        mButton.setOnClickListener(this);
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                inflateListViews();
            }
        }, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_update_list:
                inflateListViews();
                index++;
                break;
        }
    }

    private int index = 0;

    private void inflateListViews() {
        mListView.smoothScrollByOffset(0);
//        mStrings.clear();
        Math.random();
        Random   random   = new Random();
        int      i        = random.nextInt(10);
        TestData testData = new TestData();
        testData.setContent(index + "--->>>" + mProses[i].toString());
        testData.setExpland(false);
        mStrings.add(testData);


        mAdapter.notifyDataSetChanged();
    }

    class TheBaseAdapter extends BaseAdapter implements ExpandableTextViews.OnExpandListener {

        private List<TestData>       mList;
        private LayoutInflater       inflater;

        public TheBaseAdapter(Context context, List<TestData> list) {
            mList = list;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public TestData getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        //只要在getview时为其赋值为准确的宽度值即可，无论采用何种方法
        private int etvWidth;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item, parent, false);
                viewHolder.etv = (ExpandableTextViews) convertView.findViewById(R.id.etv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            TestData item = getItem(position);
            boolean expland = item.isExpland();
            String  content = item.getContent();

            if (etvWidth == 0) {
                viewHolder.etv.post(new Runnable() {
                    @Override
                    public void run() {
                        etvWidth = viewHolder.etv.getWidth();
                    }
                });
            }
            viewHolder.etv.setExpandListener(this,position);

            viewHolder.etv.updateForRecyclerView(content, etvWidth, expland ? 1 : 0);//第一次getview时肯定为etvWidth为0

            return convertView;
        }

        @Override
        public void onExpand(ExpandableTextViews view, boolean expland, int position) {
            mList.get(position).setExpland(expland);
            
            
        }
    }

    static class ViewHolder {
        ExpandableTextViews etv;
    }
}
