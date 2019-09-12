package com.example.rubbishcommunity.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class CustomView extends FrameLayout {
    public CustomView(Context context) {
        super(context);

    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);



        int centerRadius = 200;
        float shadowLength = 3f;

        Paint paint = new Paint();
        paint.setAntiAlias(true);



        Path path = new Path();

        path.lineTo(getWidth()/2f - centerRadius,shadowLength);
        path.lineTo(getWidth()/2f - centerRadius/2f,centerRadius);
        path.lineTo(getWidth()/2f + centerRadius/2f,centerRadius);
        path.lineTo(getWidth()/2f + centerRadius,shadowLength);
        path.lineTo(getWidth(),0f);
        path.lineTo(getWidth(),getHeight());
        path.lineTo(0,getHeight());
        path.close();

        paint.setPathEffect(new CornerPathEffect(centerRadius));


        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        paint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.OUTER));
        canvas.drawPath(path,paint);



        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setMaskFilter(null);


        canvas.drawPath(path,paint);




    }



}
