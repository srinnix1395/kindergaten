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
    public static final int TYPE_WIDTH = 1;
    public static final int TYPE_HEIGHT = 2;

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

    public void setMeasureType(int measureType) {
        this.measureType = measureType;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

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
