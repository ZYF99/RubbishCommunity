package com.example.rubbishcommunity.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;


public class EditTextRightClear extends AppCompatEditText {
    public EditTextRightClear(Context context) {
        super(context);
        initEditText();
    }

    public EditTextRightClear(Context context, AttributeSet attrs) {
        super(context, attrs);
        initEditText();
    }

    public EditTextRightClear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initEditText();
    }
    private Drawable dBottom;
    private Drawable dRight;
    private Rect rBounds;

    private void initEditText() {
        setEditTextDrawable();
        setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                setEditTextDrawable();
            }
        });
        addTextChangedListener( new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setEditTextDrawable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**控制图片的显示*/
    private void setEditTextDrawable() {
        if (isFocused()) {
            if (getText().toString().length() == 0) {
                setCompoundDrawables(null, null, null, this.dBottom);
            } else {
                setCompoundDrawables(null, null, this.dRight, this.dBottom);
            }
        } else{
            setCompoundDrawables(null, null, null, this.dBottom);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.dRight = null;
        this.dBottom = null;
        this.rBounds = null;
    }

    // 添加触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        if (paramMotionEvent.getAction() == MotionEvent.ACTION_UP && dRight != null) {
            rBounds = dRight.getBounds();
            float x = paramMotionEvent.getRawX();
            int width = this.getRight();

            if (x >= (width - rBounds.width() - getPaddingRight())) {
                this.setText("");
                paramMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
            }
        }
        return super.onTouchEvent(paramMotionEvent);
    }

    // 设置显示的图片资源
    @Override
    public void setCompoundDrawables(Drawable paramDrawable1, Drawable paramDrawable2,
                                     Drawable paramDrawable3, Drawable paramDrawable4) {
        if (paramDrawable3 != null) {

            this.dRight = paramDrawable3;
        }
        if (paramDrawable4 != null) {
            this.dBottom = paramDrawable4;
        }
        super.setCompoundDrawables(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
    }
}