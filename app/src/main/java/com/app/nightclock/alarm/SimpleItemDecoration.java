package com.app.nightclock.alarm;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleItemDecoration extends RecyclerView.ItemDecoration {

    private int mVerSpace;
    private int mHrzSpace;

    public SimpleItemDecoration(int verticalSpaceInPixels) {
        mVerSpace = verticalSpaceInPixels;
        mHrzSpace = 0;
    }

    public SimpleItemDecoration(int verticalSpaceInPixels, int horizontalSpaceInPixels) {
        mVerSpace = verticalSpaceInPixels;
        mHrzSpace = horizontalSpaceInPixels;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        outRect.left = mHrzSpace;
        outRect.right = mHrzSpace;
        outRect.bottom = mVerSpace;
        outRect.top = mVerSpace;
    }

}
