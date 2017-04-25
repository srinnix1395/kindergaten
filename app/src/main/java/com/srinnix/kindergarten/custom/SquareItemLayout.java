package com.srinnix.kindergarten.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.srinnix.kindergarten.R;

/**
 * Created by anhtu on 4/6/2017.
 */

public class SquareItemLayout extends FrameLayout {
    private static final int TYPE_WIDTH = 1;
    private static final int TYPE_HEIGHT = 2;

    private int measureType;

    public SquareItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SquareItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareItemLayout);
        measureType = typedArray.getInteger(R.styleable.SquareItemLayout_measureType, TYPE_WIDTH);
        typedArray.recycle();
    }

    public SquareItemLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));

        int childSize;
        if (measureType == TYPE_WIDTH) {
            childSize = getMeasuredWidth();
        } else {
            childSize = getMeasuredHeight();
        }

        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childSize,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
