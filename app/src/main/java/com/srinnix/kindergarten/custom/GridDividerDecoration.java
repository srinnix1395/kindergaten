package com.srinnix.kindergarten.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.srinnix.kindergarten.util.UiUtils;

/**
 * Created by anhtu on 5/5/2017.
 */

public class GridDividerDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private Paint paint;

    public GridDividerDecoration(Context context, int space) {
        this.space = UiUtils.dpToPixel(context, space);

        paint = new Paint();
        paint.setColor(Color.parseColor("#26000000"));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, space, 0, 0);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        drawVertical(c, parent);
        drawHorizontal(c, parent);
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        if (parent.getChildCount() == 0) return;

        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            int left;
            if (i % 5 == 0) {
                left = child.getLeft() + space;
            } else {
                left = child.getLeft() - space;
            }

            int right;
            if (i % 5 == 4) {
                right = child.getRight() - space;
            } else {
                right = child.getRight() + space;
            }

            final int top = child.getBottom() + space;
            final int bottom = top + space;

            c.drawRect(new Rect(left, top, right, bottom), paint);
        }
    }

    /**
     * Draw dividers to the right of each child view
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final int left = child.getRight();
            final int right = left + space;
            int top;

            if (i < 5) {
                top = child.getTop() + space;
            } else {
                top = child.getTop() - space;
            }

            int bottom;

            if (i > childCount - 6) {
                bottom = child.getBottom() - space;
            } else {
                bottom = child.getBottom() + space;
            }


            c.drawRect(new Rect(left, top, right, bottom), paint);

        }
    }
}
