package com.example.rubbishcommunity.ui.home.mine;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;

/**
 * Behavior which should be used by {@link View}s which can scroll vertically and support nested
 * scrolling to automatically scroll any {@link AppBarLayout} siblings.
 */
public class HomeScrollingBehavior {

    public static class HomeChildTabScrollingBehavior extends AppBarLayout.ScrollingViewBehavior {

        public HomeChildTabScrollingBehavior() {
        }

        public HomeChildTabScrollingBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
            // We depend on any AppBarLayouts
            return dependency instanceof AppBarLayout;
        }

        @Override
        public boolean onDependentViewChanged(
                @NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
            return super.onDependentViewChanged(parent,child,dependency);
        }

    }
}

