package com.example.rubbishcommunity.mainac.ui.coordinator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyBottomView extends BottomNavigationView {

    Context context;


    public MyBottomView(Context context) {
        super(context);
        this.context = context;

    }

    public MyBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {


        super.onDraw(canvas);

        int centerRadius = 100;
        canvas.save();

        Path path1 = new Path();
        path1.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CCW);
        canvas.clipPath(path1, Region.Op.UNION);


        Path path2 = new Path();
        path2.addCircle(getWidth() / 2f, getHeight()/2f, centerRadius, Path.Direction.CCW);
        canvas.clipPath(path2, Region.Op.XOR);


        canvas.drawColor(Color.WHITE);
        canvas.restore();


    }


}
