package com.gzq.lib_resource.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.ListViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gzq.lib_core.utils.ToastUtils;

public class CollapsePageView extends LinearLayout {
    private View viewTop;
    private View viewMiddle;
    private View viewBottom;
    private ViewDragHelper viewDragHelper;
    private int mDownY;
    private boolean mIsMenuOpen = false;

    public CollapsePageView(@NonNull Context context) {
        this(context, null);
    }

    public CollapsePageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsePageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                // 返回true 表示这个view可以滑动
                return viewBottom == child;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                return 0;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - child.getHeight() - topBound;
                return Math.min(Math.max(top, topBound), bottomBound);
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
//                if (viewMiddle.getTop() < viewTop.getHeight() / 2) {
//                    viewDragHelper.settleCapturedViewAt(0, 0);
//                    mIsMenuOpen = false;
//                } else {
//                    viewDragHelper.settleCapturedViewAt(0, viewTop.getHeight());
//                    mIsMenuOpen = true;
//                }
                invalidate();
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        viewTop = getChildAt(0);
        viewMiddle = getChildAt(1);
        viewBottom = getChildAt(2);
        viewMiddle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("我点击了中间的布局");
            }
        });

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                viewDragHelper.processTouchEvent(ev);
                mDownY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) ev.getY();
                // 如果后方的View被打开或者用户向下拖动，
                if ((mIsMenuOpen || y - mDownY > 0) && !canChildScrollUp()) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    // 判断当前的展示控件滚动条能否继续向上移动
    public boolean canChildScrollUp() {
        if (viewMiddle instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) viewMiddle, -1);
        }
        return viewMiddle.canScrollVertically(-1);
    }
}
