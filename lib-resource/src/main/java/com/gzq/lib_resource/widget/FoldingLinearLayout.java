package com.gzq.lib_resource.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gzq.lib_resource.R;

import timber.log.Timber;

public class FoldingLinearLayout extends LinearLayout implements View.OnTouchListener {

    private View topView;
    private View middleView;
    private View bottomView;
    private float mPosX;
    private float mPosY;
    private float mCurPosX;
    private float mCurPosY;
    private boolean isMiddleVisiable = false;
    private boolean isBottomVisiable = false;
    private int middleViewMeasureHeight;
    private int bottomViewMeasureHeight;

    public FoldingLinearLayout(Context context) {
        super(context);
    }

    public FoldingLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FoldingLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 3) {
            throw new NullPointerException("子view的数量必须为3个");
        }
        Timber.i(">>>>>>>onFinishInflate");
        topView = getChildAt(0);
        middleView = getChildAt(1);
        bottomView = getChildAt(2);
        isMiddleVisiable = middleView.getVisibility() == VISIBLE ? true : false;
        isBottomVisiable = bottomView.getVisibility() == VISIBLE ? true : false;
        topView.setOnTouchListener(this);
        middleView.setOnTouchListener(this);
        bottomView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPosX = event.getX();
                mPosY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurPosX = event.getX();
                mCurPosY = event.getY();

                break;
            case MotionEvent.ACTION_UP:
                if (mCurPosY - mPosY > 0
                        && (Math.abs(mCurPosY - mPosY) > 25)) {
                    //向下滑動
                    if (isMiddleVisiable && isBottomVisiable) {
                        bottomView.setVisibility(GONE);
                        isBottomVisiable = false;
//                        animClose(bottomView);
                    } else if (!isBottomVisiable && isMiddleVisiable) {
                        middleView.setVisibility(GONE);
                        isMiddleVisiable = false;
                        if (topView instanceof ConstraintLayout) {
                            View child = ((ConstraintLayout) topView).getChildAt(1);
                            if (child instanceof LinearLayout){
                                View linChild = ((LinearLayout) child).getChildAt(0);
                                if (linChild instanceof ImageView){
                                    ((ImageView) linChild).setImageResource(R.drawable.page_up);
                                }
                            }
                        }
//                        animClose(middleView);
                    }
                } else if (mCurPosY - mPosY < 0
                        && (Math.abs(mCurPosY - mPosY) > 25)) {
                    //向上滑动
                    if (!isMiddleVisiable && !isBottomVisiable) {
                        middleView.setVisibility(VISIBLE);
                        isMiddleVisiable = true;
//                        animOpen(middleView, middleViewMeasureHeight);
                    } else if (isMiddleVisiable && !isBottomVisiable) {
                        bottomView.setVisibility(VISIBLE);
                        isBottomVisiable = true;
                        if (topView instanceof ConstraintLayout) {
                            View child = ((ConstraintLayout) topView).getChildAt(1);
                            if (child instanceof LinearLayout){
                                View linChild = ((LinearLayout) child).getChildAt(0);
                                if (linChild instanceof ImageView){
                                    ((ImageView) linChild).setImageResource(R.drawable.page_down);
                                }
                            }
                        }
//                        animOpen(bottomView, bottomViewMeasureHeight);
                    }
                }

                break;
        }
        return true;
    }


    private void animOpen(final View view, int mHiddenViewMeasuredHeight) {
        view.setVisibility(View.VISIBLE);
        ValueAnimator va = createDropAnim(view, 0, mHiddenViewMeasuredHeight);
        va.start();
    }

    private void animClose(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator va = createDropAnim(view, origHeight, 0);
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        va.start();
    }

    /**
     * 使用动画的方式来改变高度解决visible不一闪而过出现
     *
     * @param view
     * @param start 初始状态值
     * @param end   结束状态值
     * @return
     */
    private ValueAnimator createDropAnim(final View view, int start, int end) {
        ValueAnimator va = ValueAnimator.ofInt(start, end);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();//根据时间因子的变化系数进行设置高度
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);//设置高度
            }
        });
        return va;
    }
}
