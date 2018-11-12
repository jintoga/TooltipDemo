package com.jintoga.tutorialdemo.viewtooltip;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

public class BackgroundView extends View {
    public static final String BACKGROUND_VIEW_TAG = "BACKGROUND_VIEW_TAG";

    private boolean clickToHide;

    public BackgroundView(@NonNull Context context) {
        super(context);
        setTag(BACKGROUND_VIEW_TAG);
        setClickable(true);
        setFocusableInTouchMode(true);
        setFocusable(true);
    }

    public void setColor(int color) {
        this.setBackgroundColor(color);
    }

    public void setClickBackgroundToHide(boolean clickToHide) {
        this.clickToHide = clickToHide;
    }

    public Boolean isClickBackgroundToHide() {
        return this.clickToHide;
    }


}
