package com.example.rubbishcommunity.ui.home.mine;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class TranslucentBehavior extends CoordinatorLayout.Behavior<View> {

    /**
     * 标题栏距 '依附的滑动控件' 的高度
     */
    private float deltaY = 0;

    public TranslucentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof NestedScrollView || dependency instanceof SwipeRefreshLayout;
    }

    /**
     * 依附控件状态改变时的回调
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        if (deltaY == 0) {
            deltaY = dependency.getY() - child.getHeight();
        }

        float dy = dependency.getY() - child.getHeight();
        dy = dy < 0 ? 0 : dy;


        float alpha = 1 - (dy / deltaY);
        float y = -(dy / deltaY) * child.getHeight();

        child.setTranslationY(y);
        child.setAlpha(alpha);
        return true;
    }
}
