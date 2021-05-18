package com.example.expandabletext.ui.hodler;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expandabletext.ui.widget.ExplandableTextView;
import com.example.expandabletext.R;
import com.example.expandabletext.ui.widget.ExpandableTextViews;

/**
 * @author chenyu
 * My personal blog  https://prettyant.github.io
 * <p>
 * Created on 11:58 PM  12/05/21
 * PackageName : com.example.expandabletext.ui.hodler
 * describle :
 */
public class ExplandableHodler extends RecyclerView.ViewHolder {

    public ExpandableTextViews etv_text_accept;
    public ExplandableTextView etv_sxd;

    public ExplandableHodler(@NonNull View itemView, ExpandableTextViews.OnExpandListener onExpandListener) {
        super(itemView);
        etv_text_accept = itemView.findViewById(R.id.etv_text_accept);
        etv_sxd = itemView.findViewById(R.id.etv_sxd);
    }
}
