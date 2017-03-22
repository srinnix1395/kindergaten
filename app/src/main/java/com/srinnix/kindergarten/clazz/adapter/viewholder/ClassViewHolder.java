package com.srinnix.kindergarten.clazz.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.clazz.adapter.ClassAdapter;
import com.srinnix.kindergarten.model.Class;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2/21/2017.
 */

public class ClassViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textview_icon)
    TextView tvIcon;

    @BindView(R.id.textview_class_name)
    TextView tvClassName;

    @BindView(R.id.textview_number_member)
    TextView tvNumberMember;

    private int position;

    public ClassViewHolder(View itemView, ClassAdapter.OnClickClassItemListener itemListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> itemListener.onClick(position));
    }

    public void bindData(Class aClass, int position) {
        this.position = position;

        tvClassName.setText(aClass.getName());
        tvIcon.setText(String.valueOf(aClass.getName().charAt(0)));
        tvNumberMember.setText(String.format(Locale.getDefault(),
                "%d %s", aClass.getNumberMember(), itemView.getContext().getString(R.string.member)));
    }
}
