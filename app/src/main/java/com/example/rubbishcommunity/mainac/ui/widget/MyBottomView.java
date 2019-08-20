package com.example.rubbishcommunity.mainac.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.RequiresApi;
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        int centerRadius = getHeight() * 3/4;
        float shadowLength = 5f;

        Paint paint = new Paint();

        paint.setAntiAlias(true);


        Path path = new Path();
        path.moveTo(0, shadowLength);




        path.lineTo(getWidth() / 2f - centerRadius, shadowLength);

        path.lineTo(getWidth()/2f - centerRadius/3f * 2f ,shadowLength + centerRadius/4f);
        path.lineTo(getWidth()/2f - centerRadius/4f ,shadowLength + centerRadius * 3/4f);


        path.lineTo(getWidth()/2f + centerRadius/4f ,shadowLength + centerRadius * 3/4f);
        path.lineTo(getWidth()/2f + centerRadius/3f * 2f ,shadowLength + centerRadius/4f);

        path.lineTo(getWidth()/2f + centerRadius,shadowLength);




        path.lineTo(getWidth(), shadowLength);
        path.lineTo(getWidth(), getHeight());
        path.lineTo(0, getHeight());
        path.lineTo(0, shadowLength);
        path.close();
        paint.setPathEffect(new CornerPathEffect(centerRadius / 4f));


        //画阴影
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(1);
        paint.setMaskFilter(new BlurMaskFilter(shadowLength - 1, BlurMaskFilter.Blur.NORMAL));
        canvas.drawPath(path, paint);


        //填充白色
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setMaskFilter(null);
        canvas.drawPath(path, paint);



    }


}
