package com.example.expandabletext.ui.widget;

/**
 * Created by Carbs.Wang on 2016/7/16.
 * website: https://github.com/Carbs0126/
 * <p>
 * Thanks to :
 * 1.ReadMoreTextView
 * https://github.com/borjabravo10/ReadMoreTextView
 * 2.TouchableSpan
 * http://stackoverflow.com/questions
 * /20856105/change-the-text-color-of-a-single-clickablespan-when-pressed-without-affecting-o
 * 3.FlatUI
 * http://www.bootcss.com/p/flat-ui/
 *
 * 原始版2 简化版
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.expandabletext.R;

public class ExpandableTextViews extends androidx.appcompat.widget.AppCompatTextView {

    public static final int STATE_SHRINK = 0;
    public static final int STATE_EXPAND = 1;

    private static final int     MAX_LINES_ON_SHRINK = 4;
    private static final boolean TOGGLE_ENABLE       = true;

    private boolean mToggleEnable     = TOGGLE_ENABLE;
    private int     mMaxLinesOnShrink = MAX_LINES_ON_SHRINK;
    private int     mCurrState        = STATE_SHRINK;

    private BufferType mBufferType          = BufferType.NORMAL;
    private TextPaint  mTextPaint;
    private Layout     mLayout;
    private int        mLayoutWidth         = 0;
    private int        mFutureTextViewWidth = 0;

    //  the original text of this view
    private CharSequence mOrigText;

    //  used to judge if the listener of corresponding to the onclick event of ExpandableTextViews
    //  is specifically for inner toggle
    private ExpandableClickListener mExpandableClickListener;
    private OnExpandListener        mOnExpandListener;
    private int position;

    public ExpandableTextViews(Context context) {
        super(context);
        init();
    }

    public ExpandableTextViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public ExpandableTextViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        if (a == null) {
            return;
        }
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ExpandableTextView_etv_MaxLinesOnShrink) {
                mMaxLinesOnShrink = a.getInteger(attr, MAX_LINES_ON_SHRINK);
            }else if (attr == R.styleable.ExpandableTextView_etv_ToExpandHint) {
            } else if (attr == R.styleable.ExpandableTextView_etv_ToShrinkHint) {
            } else if (attr == R.styleable.ExpandableTextView_etv_EnableToggle) {
                mToggleEnable = a.getBoolean(attr, TOGGLE_ENABLE);
            }else if (attr == R.styleable.ExpandableTextView_etv_InitState) {
                mCurrState = a.getInteger(attr, STATE_SHRINK);
            }
        }
        a.recycle();
    }

    private void init() {
        if (mToggleEnable) {
            mExpandableClickListener = new ExpandableClickListener();
            setOnClickListener(mExpandableClickListener);
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);
                setTextInternal(getNewTextByConfig(), mBufferType);
            }
        });
    }

    /**
     * used in ListView or RecyclerView to update ExpandableTextViews
     * @param text
     *          original text
     * @param futureTextViewWidth
     *          the width of ExpandableTextViews in px unit,
     *          used to get max line number of original text by given the width
     * @param expandState
     *          expand or shrink
     */
    public void updateForRecyclerView(CharSequence text, int futureTextViewWidth, int expandState) {
        mFutureTextViewWidth = futureTextViewWidth;
        mCurrState = expandState;
        mOrigText = text;
        CharSequence newTextByConfig = getNewTextByConfig();
        setText(newTextByConfig);
    }

    /**
     * get the current state of ExpandableTextViews
     * @return
     *      STATE_SHRINK if in shrink state
     *      STATE_EXPAND if in expand state
     */
    public int getExpandState() {
        return mCurrState;
    }

    /**
     * refresh and get a will-be-displayed text by current configuration
     * @return
     *      get a will-be-displayed text
     */
    private CharSequence getNewTextByConfig() {
        if (TextUtils.isEmpty(mOrigText)) {
            return mOrigText;
        }

        mLayout = getLayout();
        if (mLayout != null) {
            mLayoutWidth = mLayout.getWidth();
        }

        if (mLayoutWidth <= 0) {
            if (getWidth() == 0) {
                if (mFutureTextViewWidth == 0) {
                    return mOrigText;
                } else {
                    mLayoutWidth = mFutureTextViewWidth - getPaddingLeft() - getPaddingRight();
                }
            } else {
                mLayoutWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            }
        }

        mTextPaint = getPaint();

        switch (mCurrState) {
            case STATE_SHRINK: {
                mLayout = new DynamicLayout(mOrigText, mTextPaint, mLayoutWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
               int lineCount = mLayout.getLineCount();
                if (lineCount <= mMaxLinesOnShrink) {
                    return mOrigText;
                }
                int indexEnd   = getValidLayout().getLineEnd(mMaxLinesOnShrink - 1);
                return mOrigText.subSequence(0, indexEnd);
            }
            case STATE_EXPAND: {
                return mOrigText;
            }
        }
        return mOrigText;
    }


    public void setExpandListener(OnExpandListener listener, int position) {
        mOnExpandListener = listener;
        this.position = position;
    }

    private Layout getValidLayout() {
        return mLayout != null ? mLayout : getLayout();
    }

    private void toggle() {
        mCurrState = mCurrState == STATE_EXPAND ? STATE_SHRINK : STATE_EXPAND;
        mOnExpandListener.onExpand(this,mCurrState == STATE_EXPAND,position);
        setTextInternal(getNewTextByConfig(), mBufferType);
    }

//    @Override
//    public void setText(CharSequence text, BufferType type) {
//        mOrigText = text;
//        mBufferType = type;
//        setTextInternal(getNewTextByConfig(), type);
//    }

    private void setTextInternal(CharSequence text, BufferType type) {
        super.setText(text, type);
    }


    public void setExpand(boolean expland) {
        mCurrState = expland ? STATE_EXPAND : STATE_SHRINK;
    }

    public interface OnExpandListener {
        void onExpand(ExpandableTextViews view, boolean expland, int position);

    }

    private class ExpandableClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            toggle();
        }
    }


}
