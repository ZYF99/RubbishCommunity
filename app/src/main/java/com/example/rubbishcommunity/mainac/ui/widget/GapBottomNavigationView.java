//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.rubbishcommunity.mainac.ui.widget;

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
import android.widget.Toast;
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
public class GapBottomNavigationView extends FrameLayout {
    private static final int MENU_PRESENTER_ID = 1;
    private final MenuBuilder menu;
    private final BottomNavigationMenuView menuView;
    private final BottomNavigationPresenter presenter;
    private MenuInflater menuInflater;
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener;
    private BottomNavigationView.OnNavigationItemReselectedListener reselectedListener;

    public GapBottomNavigationView(Context context) {
        this(context, (AttributeSet) null);
    }

    public GapBottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, attr.bottomNavigationStyle);
    }


    public GapBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.presenter = new BottomNavigationPresenter();
        this.menu = new BottomNavigationMenu(context);
        this.menuView = new BottomNavigationMenuView(context);
        LayoutParams params = new LayoutParams(-2, -2);
        params.gravity = 17;
        this.menuView.setLayoutParams(params);
        this.presenter.setBottomNavigationMenuView(this.menuView);
        this.presenter.setId(1);
        this.menuView.setPresenter(this.presenter);
        this.menu.addMenuPresenter(this.presenter);
        this.presenter.initForMenu(this.getContext(), this.menu);
        TintTypedArray a = ThemeEnforcement.obtainTintedStyledAttributes(context, attrs, styleable.BottomNavigationView, defStyleAttr, style.Widget_Design_BottomNavigationView, new int[]{styleable.BottomNavigationView_itemTextAppearanceInactive, styleable.BottomNavigationView_itemTextAppearanceActive});
        if (a.hasValue(styleable.BottomNavigationView_itemIconTint)) {
            this.menuView.setIconTintList(a.getColorStateList(styleable.BottomNavigationView_itemIconTint));
        } else {
            this.menuView.setIconTintList(this.menuView.createDefaultColorStateList(16842808));
        }

        this.setItemIconSize(a.getDimensionPixelSize(styleable.BottomNavigationView_itemIconSize, this.getResources().getDimensionPixelSize(dimen.design_bottom_navigation_icon_size)));
        if (a.hasValue(styleable.BottomNavigationView_itemTextAppearanceInactive)) {
            this.setItemTextAppearanceInactive(a.getResourceId(styleable.BottomNavigationView_itemTextAppearanceInactive, 0));
        }

        if (a.hasValue(styleable.BottomNavigationView_itemTextAppearanceActive)) {
            this.setItemTextAppearanceActive(a.getResourceId(styleable.BottomNavigationView_itemTextAppearanceActive, 0));
        }

        if (a.hasValue(styleable.BottomNavigationView_itemTextColor)) {
            this.setItemTextColor(a.getColorStateList(styleable.BottomNavigationView_itemTextColor));
        }

        if (a.hasValue(styleable.BottomNavigationView_elevation)) {
            ViewCompat.setElevation(this, (float) a.getDimensionPixelSize(styleable.BottomNavigationView_elevation, 0));
        }

        this.setLabelVisibilityMode(a.getInteger(styleable.BottomNavigationView_labelVisibilityMode, -1));
        this.setItemHorizontalTranslationEnabled(a.getBoolean(styleable.BottomNavigationView_itemHorizontalTranslationEnabled, true));
        int itemBackground = a.getResourceId(styleable.BottomNavigationView_itemBackground, 0);
        this.menuView.setItemBackgroundRes(itemBackground);
        if (a.hasValue(styleable.BottomNavigationView_menu)) {
            this.inflateMenu(a.getResourceId(styleable.BottomNavigationView_menu, 0));
        }

        a.recycle();
        this.addView(this.menuView, params);
        if (VERSION.SDK_INT < 21) {
            this.addCompatibilityTopDivider(context);
        }

        this.menu.setCallback(new Callback() {
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {

                //menu必须为奇数个
                if (menu.size() % 2 != 0) {
                    //屏蔽中间按钮的点击事件
                    if ( menu.getItem(menu.size()/2).equals(item)) {
                        return true;
                    }
                }


                if (GapBottomNavigationView.this.reselectedListener != null && item.getItemId() == GapBottomNavigationView.this.getSelectedItemId()) {
                    GapBottomNavigationView.this.reselectedListener.onNavigationItemReselected(item);
                    return true;
                } else {
                    return GapBottomNavigationView.this.selectedListener != null && !GapBottomNavigationView.this.selectedListener.onNavigationItemSelected(item);
                }


            }

            public void onMenuModeChange(MenuBuilder menu) {
            }
        });
    }

    public void  setOnNavigationItemSelectedListener(@Nullable BottomNavigationView.OnNavigationItemSelectedListener listener) {
        this.selectedListener = listener;
    }

    public void setOnNavigationItemReselectedListener(@Nullable BottomNavigationView.OnNavigationItemReselectedListener listener) {
        this.reselectedListener = listener;
    }

    @NonNull
    public Menu getMenu() {
        return this.menu;
    }

    public void inflateMenu(int resId) {
        this.presenter.setUpdateSuspended(true);
        this.getMenuInflater().inflate(resId, this.menu);
        this.presenter.setUpdateSuspended(false);
        this.presenter.updateMenuView(true);
    }

    public int getMaxItemCount() {
        return 5;
    }

    @Nullable
    public ColorStateList getItemIconTintList() {
        return this.menuView.getIconTintList();
    }

    public void setItemIconTintList(@Nullable ColorStateList tint) {
        this.menuView.setIconTintList(tint);
    }

    public void setItemIconSize(@Dimension int iconSize) {
        this.menuView.setItemIconSize(iconSize);
    }

    public void setItemIconSizeRes(@DimenRes int iconSizeRes) {
        this.setItemIconSize(this.getResources().getDimensionPixelSize(iconSizeRes));
    }

    @Dimension
    public int getItemIconSize() {
        return this.menuView.getItemIconSize();
    }

    @Nullable
    public ColorStateList getItemTextColor() {
        return this.menuView.getItemTextColor();
    }

    public void setItemTextColor(@Nullable ColorStateList textColor) {
        this.menuView.setItemTextColor(textColor);
    }

    /**
     * @deprecated
     */
    @Deprecated
    @DrawableRes
    public int getItemBackgroundResource() {
        return this.menuView.getItemBackgroundRes();
    }

    public void setItemBackgroundResource(@DrawableRes int resId) {
        this.menuView.setItemBackgroundRes(resId);
    }

    @Nullable
    public Drawable getItemBackground() {
        return this.menuView.getItemBackground();
    }

    public void setItemBackground(@Nullable Drawable background) {
        this.menuView.setItemBackground(background);
    }

    @IdRes
    public int getSelectedItemId() {
        return this.menuView.getSelectedItemId();
    }

    public void setSelectedItemId(@IdRes int itemId) {
        MenuItem item = this.menu.findItem(itemId);
        if (item != null && !this.menu.performItemAction(item, this.presenter, 0)) {
            item.setChecked(true);
        }

    }

    public void setLabelVisibilityMode(int labelVisibilityMode) {
        if (this.menuView.getLabelVisibilityMode() != labelVisibilityMode) {
            this.menuView.setLabelVisibilityMode(labelVisibilityMode);
            this.presenter.updateMenuView(false);
        }

    }

    public int getLabelVisibilityMode() {
        return this.menuView.getLabelVisibilityMode();
    }

    public void setItemTextAppearanceInactive(@StyleRes int textAppearanceRes) {
        this.menuView.setItemTextAppearanceInactive(textAppearanceRes);
    }

    @StyleRes
    public int getItemTextAppearanceInactive() {
        return this.menuView.getItemTextAppearanceInactive();
    }

    public void setItemTextAppearanceActive(@StyleRes int textAppearanceRes) {
        this.menuView.setItemTextAppearanceActive(textAppearanceRes);
    }

    @StyleRes
    public int getItemTextAppearanceActive() {
        return this.menuView.getItemTextAppearanceActive();
    }

    public void setItemHorizontalTranslationEnabled(boolean itemHorizontalTranslationEnabled) {
        if (this.menuView.isItemHorizontalTranslationEnabled() != itemHorizontalTranslationEnabled) {
            this.menuView.setItemHorizontalTranslationEnabled(itemHorizontalTranslationEnabled);
            this.presenter.updateMenuView(false);
        }

    }

    public boolean isItemHorizontalTranslationEnabled() {
        return this.menuView.isItemHorizontalTranslationEnabled();
    }

    private void addCompatibilityTopDivider(Context context) {
        View divider = new View(context);
        divider.setBackgroundColor(ContextCompat.getColor(context, color.design_bottom_navigation_shadow_color));
        LayoutParams dividerParams = new LayoutParams(-1, this.getResources().getDimensionPixelSize(dimen.design_bottom_navigation_shadow_height));
        divider.setLayoutParams(dividerParams);
        this.addView(divider);
    }

    private MenuInflater getMenuInflater() {
        if (this.menuInflater == null) {
            this.menuInflater = new SupportMenuInflater(this.getContext());
        }

        return this.menuInflater;
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        GapBottomNavigationView.SavedState savedState = new GapBottomNavigationView.SavedState(superState);
        savedState.menuPresenterState = new Bundle();
        this.menu.savePresenterStates(savedState.menuPresenterState);
        return savedState;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof GapBottomNavigationView.SavedState)) {
            super.onRestoreInstanceState(state);
        } else {
            GapBottomNavigationView.SavedState savedState = (GapBottomNavigationView.SavedState) state;
            super.onRestoreInstanceState(savedState.getSuperState());
            this.menu.restorePresenterStates(savedState.menuPresenterState);
        }
    }

    static class SavedState extends AbsSavedState {
        Bundle menuPresenterState;
        public static final Creator<GapBottomNavigationView.SavedState> CREATOR = new ClassLoaderCreator<GapBottomNavigationView.SavedState>() {
            public GapBottomNavigationView.SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new GapBottomNavigationView.SavedState(in, loader);
            }

            public GapBottomNavigationView.SavedState createFromParcel(Parcel in) {
                return new GapBottomNavigationView.SavedState(in, (ClassLoader) null);
            }

            public GapBottomNavigationView.SavedState[] newArray(int size) {
                return new GapBottomNavigationView.SavedState[size];
            }
        };

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            this.readFromParcel(source, loader);
        }

        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBundle(this.menuPresenterState);
        }

        private void readFromParcel(Parcel in, ClassLoader loader) {
            this.menuPresenterState = in.readBundle(loader);
        }
    }

    public interface OnNavigationItemReselectedListener {
        void onNavigationItemReselected(@NonNull MenuItem var1);
    }

    public interface OnNavigationItemSelectedListener {
        boolean onNavigationItemSelected(@NonNull MenuItem var1);
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
