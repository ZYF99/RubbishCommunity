package com.example.rubbishcommunity.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.Objects;

public class FullScreenDialog extends Dialog {

    private int res;

    public FullScreenDialog(@NonNull Context context,int res) {
        super(context);
        this.res = res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置一个布局
        setContentView(res);
        //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //一定要在setContentView之后调用，否则无效
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
