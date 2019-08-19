package com.example.rubbishcommunity.mainac.ui.coordinator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MyBottomNavigationView extends LinearLayout {

    Context context;

    public MyBottomNavigationView(Context context) {
        super(context);
        this.context = context;
    }

    public MyBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public MyBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

}
