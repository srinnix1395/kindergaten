package com.srinnix.kindergarten.custom;

/*

Decorator which adds spacing around the tiles in Health Grid layout RecyclerView. Apply to Health RecyclerView with:

    SpacesItemDecoration decoration = new SpacesItemDecoration(16);
    mRecyclerView.addItemDecoration(decoration);

Feel free to add any value you wish for SpacesItemDecoration. That value determines the amount of spacing.

Source: http://blog.grafixartist.com/pinterest-masonry-layout-staggered-grid/

*/

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.srinnix.kindergarten.clazz.adapter.ImageAdapter;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private ImageAdapter mImageAdapter;
    private final int mSpace;
    private final int spanCount;

    public SpacesItemDecoration(ImageAdapter mImageAdapter, int space, int spanCount) {
        this.mImageAdapter = mImageAdapter;
        this.mSpace = space;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (mImageAdapter.getItemViewType(position) == ImageAdapter.VIEW_TYPE_LOADING) {
            return;
        }

        int column = position % spanCount; // item column

        outRect.left = column * mSpace / spanCount; // column * ((1f / spanCount) * mSpace)
        outRect.right = mSpace - (column + 1) * mSpace / spanCount; // mSpace - (column + 1) * ((1f /    spanCount) * mSpace)
        if (position >= spanCount) {
            outRect.top = mSpace; // item top
        }
    }
}
