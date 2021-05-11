package com.example.expandabletext;

import android.content.Context;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * @author chenyu
 * My personal blog  https://prettyant.github.io
 * <p>
 * Created on 12:12 PM  11/05/21
 * PackageName : com.example.expandabletext
 * describle :
 */
public class ExplandableTextView extends RelativeLayout implements View.OnClickListener {
    public static final int                 STATE_SHRINK         = 0;
    public static final int                 STATE_EXPAND         = 1;
    private             Context             context;
    private             TextView            tv_show;
    private             LinearLayout        ll_loadmore;
    private             View                gapView;
    private             TextView.BufferType mBufferType          = TextView.BufferType.NORMAL;
    private             CharSequence        mOrigText;
    private             Layout              layout;
    private             int                 layoutWidth;
    private             int                 mFutureTextViewWidth = 0;
    private             TextPaint           paint;
    private             int                 mCurrState           = STATE_SHRINK;
    private             int                 mMaxLinesOnShrink    = 4;//最大行数
    private             DynamicLayout       dynamicLayout;
    private             TextView            tvExplandshrink;
    private             ImageView           ivArrow;
    private             boolean             canShrink            = true;//true 展开后可以收起,false 展开后可以收起
    private             boolean             canGradient          = true;//true 可以渐变,false 不可以渐变

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
        mOrigText = tv_show.getText();
        ll_loadmore = inflate.findViewById(R.id.ll_loadmore);
        gapView = inflate.findViewById(R.id.v_gap);
        tvExplandshrink = inflate.findViewById(R.id.tv_explandshrink);//显示的文字,展开、收起
        ivArrow = inflate.findViewById(R.id.iv_arrow);//箭头
        if (canGradient) {
            gapView.setBackgroundResource(R.drawable.shap_background_cangrdient);
        } else {
            gapView.setBackgroundResource(R.drawable.shap_background_normal);
        }
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
        tv_show.setText(text, type);
    }

    private CharSequence getNewTextByConfig() {
        if (TextUtils.isEmpty(mOrigText)) {
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
                    gapView.setVisibility(GONE);
                    return mOrigText;
                }
                //缩略后的计算最后一行的首尾 索引
                int indexEnd = getValidLayout().getLineEnd(mMaxLinesOnShrink - 1);
                return mOrigText.subSequence(0, indexEnd);
            case STATE_EXPAND:
                return mOrigText;
        }


        return mOrigText;
    }

    /**
     * 获取textView的高度
     *
     * @param textView
     * @return
     */
    private int getTextViewHeight(TextView textView) {
        Layout layout  = textView.getLayout();
        int    desired = layout.getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() +
                textView.getCompoundPaddingBottom();
        return desired + padding;
    }

    private Layout getValidLayout() {
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

        if (mMaxLinesOnShrink > tv_show.getLineCount()) {
            ll_loadmore.setVisibility(GONE);
            return;
        }
        switch (mCurrState) {
            case STATE_SHRINK:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_loadmore.getLayoutParams();
                layoutParams.topMargin = -getTextViewHeight(tv_show) / mMaxLinesOnShrink;
                ll_loadmore.setLayoutParams(layoutParams);
                ViewGroup.LayoutParams gapShrinkParams = gapView.getLayoutParams();
                gapShrinkParams.height = getTextViewHeight(tv_show) / mMaxLinesOnShrink;
                gapView.setLayoutParams(gapShrinkParams);
                tvExplandshrink.setText("展开");
                ivArrow.setImageResource(R.drawable.arrow_expland);
                break;
            case STATE_EXPAND:
                if (!canShrink) {//如果不能收起,则直接隐藏
                    ll_loadmore.setVisibility(GONE);
                    return;
                }
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) ll_loadmore.getLayoutParams();
                layoutParams2.topMargin = 0;
                ll_loadmore.setLayoutParams(layoutParams2);
                ViewGroup.LayoutParams gapExpandParams = gapView.getLayoutParams();
                gapExpandParams.height = 0;
                gapView.setLayoutParams(gapExpandParams);
                tvExplandshrink.setText("收起");
                ivArrow.setImageResource(R.drawable.arrow_shrink);
                break;
        }
    }


    private OnExplandClickListener onExplandClickListener;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_loadmore) {
            mCurrState = (mCurrState == STATE_SHRINK ? STATE_EXPAND : STATE_SHRINK);
            setTextInternal(getNewTextByConfig(), mBufferType);
            if (onExplandClickListener != null) {
                onExplandClickListener.onExpland(ll_loadmore);
            }
        }
    }

    public interface OnExplandClickListener {
        void onExpland(View view);
    }

    public void setOnExplandClickListener(OnExplandClickListener onExplandClickListener) {
        this.onExplandClickListener = onExplandClickListener;
    }

    /**
     * 设置是否可以收起
     *
     * @param canShrink
     */
    public void setCanShrink(boolean canShrink) {
        this.canShrink = canShrink;
    }

    /**
     * 设置是否渐变
     *
     * @param canGradient
     */
    public void setCanGradient(boolean canGradient) {
        this.canGradient = canGradient;
    }
}
