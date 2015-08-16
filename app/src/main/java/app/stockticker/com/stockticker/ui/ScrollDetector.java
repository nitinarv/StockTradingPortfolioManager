package app.stockticker.com.stockticker.ui;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AbsListView;

import app.stockticker.com.stockticker.R;

public abstract class ScrollDetector implements AbsListView.OnScrollListener {

    private int mLastScrollY;
    private int mPreviousFirstVisibleItem;
    private AbsListView mListView;
    private int mScrollThreshold;

    public ScrollDetector() {

    }

    public abstract void onScrollUp();

    public abstract void onScrollDown();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(totalItemCount == 0) return;
        if (isSameRow(firstVisibleItem)) {
            int newScrollY = getTopItemScrollY();
            boolean isSignificantDelta = Math.abs(mLastScrollY - newScrollY) > mScrollThreshold;
            if (isSignificantDelta) {
                if (mLastScrollY > newScrollY) {
                    onScrollUp();
                } else {
                    onScrollDown();
                }
            }
            mLastScrollY = newScrollY;
        } else {
            if (firstVisibleItem > mPreviousFirstVisibleItem) {
                onScrollUp();
            } else {
                onScrollDown();
            }

            mLastScrollY = getTopItemScrollY();
            mPreviousFirstVisibleItem = firstVisibleItem;
        }
    }

    public void setListView(@NonNull AbsListView listView) {
        mListView = listView;
        mScrollThreshold = listView.getContext().getResources().getDimensionPixelOffset(R.dimen.scroll_threshold);
    }

    private boolean isSameRow(int firstVisibleItem) {
        return firstVisibleItem == mPreviousFirstVisibleItem;
    }

    private int getTopItemScrollY() {
        if (mListView == null || mListView.getChildAt(0) == null) return 0;
        View topChild = mListView.getChildAt(0);
        return topChild.getTop();
    }
}