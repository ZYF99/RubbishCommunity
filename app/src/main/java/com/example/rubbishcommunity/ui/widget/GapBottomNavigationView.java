//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.rubbishcommunity.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.*;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuBuilder.Callback;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R.attr;
import com.google.android.material.R.color;
import com.google.android.material.R.dimen;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.ThemeEnforcement;

@SuppressLint("RestrictedApi")
public class GapBottomNavigationView extends BottomNavigationView {


    public GapBottomNavigationView(Context context) {
        super(context);
    }

    public GapBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GapBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        int centerRadius = getHeight() * 3 / 4;
        float shadowLength = 5f;

        Paint paint = new Paint();

        paint.setAntiAlias(true);


        Path path = new Path();
        path.moveTo(0, shadowLength);


        path.lineTo(getWidth() / 2f - centerRadius, shadowLength);

        path.lineTo(getWidth() / 2f - centerRadius / 3f * 2f, shadowLength + centerRadius / 4f);
        path.lineTo(getWidth() / 2f - centerRadius / 4f, shadowLength + centerRadius * 3 / 4f);


        path.lineTo(getWidth() / 2f + centerRadius / 4f, shadowLength + centerRadius * 3 / 4f);
        path.lineTo(getWidth() / 2f + centerRadius / 3f * 2f, shadowLength + centerRadius / 4f);

        path.lineTo(getWidth() / 2f + centerRadius, shadowLength);


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
        //paint.setMaskFilter(new BlurMaskFilter(shadowLength - 1, BlurMaskFilter.Blur.NORMAL));
        canvas.drawPath(path, paint);


        //填充白色
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setMaskFilter(null);
        canvas.drawPath(path, paint);


    }

}
