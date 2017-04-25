package com.srinnix.kindergarten.custom;

/*

Decorator which adds spacing around the tiles in Health Grid layout RecyclerView. Apply to Health RecyclerView with:

    SpacesItemDecoration decoration = new SpacesItemDecoration(16);
    mRecyclerView.addItemDecoration(decoration);

Feel free to add any value you wish for SpacesItemDecoration. That value determines the amount of spacing.

Source: http://blog.grafixartist.com/pinterest-masonry-layout-staggered-grid/

*/

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.srinnix.kindergarten.clazz.adapter.ImageAdapter;
import com.srinnix.kindergarten.util.UiUtils;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private RecyclerView.Adapter adapter;
    private final int mSpace;
    private final int spanCount;
    private boolean includeEdge;

    public SpacesItemDecoration(Context mContext, RecyclerView.Adapter adapter, int space, int spanCount, boolean includeEdge) {
        this.adapter = adapter;
        this.mSpace = UiUtils.dpToPixel(mContext, space);
        this.spanCount = spanCount;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (adapter.getItemViewType(position) == ImageAdapter.VIEW_TYPE_LOADING) {
            return;
        }

        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = mSpace - column * mSpace / spanCount; // mSpace - column * ((1f / spanCount) * mSpace)
            outRect.right = (column + 1) * mSpace / spanCount; // (column + 1) * ((1f / spanCount) * mSpace)

            if (position < spanCount) { // top edge
                outRect.top = mSpace;
            }
            outRect.bottom = mSpace; // item bottom
        } else {
            outRect.left = column * mSpace / spanCount; // column * ((1f / spanCount) * mSpace)
            outRect.right = mSpace - (column + 1) * mSpace / spanCount; // mSpace - (column + 1) * ((1f /    spanCount) * mSpace)
            if (position >= spanCount) {
                outRect.top = mSpace; // item top
            }
        }


    }
}
