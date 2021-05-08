package com.example.expandabletext;

import android.content.Context;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * @author ChenYu
 * My personal blog  https://prettyant.github.io
 * <p>
 * Created on 2:12 PM  2020/6/11
 * PackageName : com.example.customizetextview.widget
 * describle :
 */
public class ExplandableTextView extends RelativeLayout implements View.OnClickListener {
    public static final int STATE_SHRINK = 0;
    public static final int STATE_EXPAND = 1;
    private              Context             context;
    private              TextView            tv_show;
    private              TextView            tv_show_total;
    private              LinearLayout        ll_loadmore;
    private              View                v_gap;
    private              TextView.BufferType mBufferType = TextView.BufferType.NORMAL;
    //  the original text of this view
    private CharSequence mOrigText;
    private Layout layout;
    private int layoutWidth;
    private int mFutureTextViewWidth = 0;
    private TextPaint paint;
    private int mTextLineCount = -1;
    private int mCurrState = STATE_SHRINK;
    private int mMaxLinesOnShrink = 3;//最大行数
    private              DynamicLayout       dynamicLayout;

    public ExplandableTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ExplandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ExplandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        View inflate = LayoutInflater
                .from(context)
                .inflate(R.layout.widget_explandabletext, this, true);
        tv_show = inflate.findViewById(R.id.tv_show);
        mOrigText=tv_show.getText();
        tv_show_total = inflate.findViewById(R.id.tv_show_total);
        ll_loadmore = inflate.findViewById(R.id.ll_loadmore);
        v_gap = inflate.findViewById(R.id.v_gap);
        ll_loadmore.setOnClickListener(this);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);
                setTextInternal(getNewTextByConfig(), mBufferType);
            }
        });
    }

    private void setTextInternal(CharSequence text, TextView.BufferType type) {
        tv_show.setText(text,type);
    }

    private CharSequence getNewTextByConfig() {
        if(TextUtils.isEmpty(mOrigText)){
            return mOrigText;
        }
        layout = tv_show.getLayout();
        if (layout != null) {
            layoutWidth = layout.getWidth();
        }
        if (layoutWidth <= 0) {
            if (getWidth() == 0) {
                if (mFutureTextViewWidth == 0) {
                    return mOrigText;
                } else {
                    layoutWidth = mFutureTextViewWidth - getPaddingLeft() - getPaddingRight();
                }
            } else {
                layoutWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            }
        }
        paint = tv_show.getPaint();
        switch (mCurrState) {
            case STATE_SHRINK:
                dynamicLayout = new DynamicLayout(mOrigText, paint, layoutWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                int lineCount = dynamicLayout.getLineCount();
                if (lineCount <= mMaxLinesOnShrink) {
                    return mOrigText;
                }
                //缩略后的计算最后一行的首尾 索引
                int indexEnd = getValidLayout().getLineEnd(mMaxLinesOnShrink - 1);
                int indexStart = getValidLayout().getLineStart(mMaxLinesOnShrink - 1);
//                int indexEndTrimmed = indexEnd
//                        - getLengthOfString(mEllipsisHint)
//                        - (mShowToExpandHint ? getLengthOfString(mToExpandHint) + getLengthOfString(mGapToExpandHint) : 0);

                return mOrigText.subSequence(0, indexEnd);
            case STATE_EXPAND:

                return mOrigText;
        }


        return mOrigText;
    }
    private Layout getValidLayout(){
        return dynamicLayout != null ? dynamicLayout : tv_show.getLayout();
    }

    /**
     * 设置显示全部
     *
     * @param text
     */
    public void setText(String text) {
        tv_show.setText(text);
        mOrigText = text;
        setTextInternal(getNewTextByConfig(), mBufferType);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    private OnExplandClickListener onExplandClickListener;
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_loadmore) {
           mCurrState= (mCurrState == STATE_SHRINK ?STATE_EXPAND: STATE_SHRINK  );
            setTextInternal(getNewTextByConfig(), mBufferType);
            if (onExplandClickListener != null) {
                onExplandClickListener.onExpland(ll_loadmore);
            }
        }
    }
    public interface OnExplandClickListener{
        void onExpland(View view);
    }

    public void setOnExplandClickListener(OnExplandClickListener onExplandClickListener) {
        this.onExplandClickListener = onExplandClickListener;
    }
}
