package com.example.rubbishcommunity.ui.home.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class TransferHeaderBehavior extends CoordinatorLayout.Behavior<ImageView> {

    /**
     * 处于中心时候原始X轴
     */
    private int mOriginalHeaderX = 0;
    /**
     * 处于中心时候原始Y轴
     */
    private int mOriginalHeaderY = 0;


    public TransferHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @SuppressLint("LogNotTimber")
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {

        if (mOriginalHeaderX == 0) {
            // 计算X轴坐标
            this.mOriginalHeaderX = dependency.getWidth() / 2 - child.getWidth() / 2;
        }

        if (mOriginalHeaderY == 0) {
            // 计算Y轴坐标
            mOriginalHeaderY = dependency.getHeight() - child.getHeight();
        }

        //X轴百分比
        float mPercentX = dependency.getY() / mOriginalHeaderX;
        if (mPercentX >= 1) {
            mPercentX = 1;
        }
        //Y轴百分比
        float mPercentY = (dependency.getY() - (child.getHeight()/3f)) / mOriginalHeaderY;
        if (mPercentY >= 1) {
            mPercentY = 1;
        }


        float x = mOriginalHeaderX - mOriginalHeaderX * mPercentX;
        if (x <= child.getWidth() / 2) {
            x = child.getWidth() / 2f;
        }

        child.setX(x);
        child.setY(mOriginalHeaderY - mOriginalHeaderY * mPercentY);

        // 头像的放大和缩小
        child.setScaleX(1 - mPercentY / 2);
        child.setScaleY(1 - mPercentY / 2);

        return true;
    }
}