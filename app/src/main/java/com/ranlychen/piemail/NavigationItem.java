package com.ranlychen.piemail;

import android.graphics.drawable.Drawable;

public class NavigationItem {
    private String text;
    private Drawable drawable;

    public NavigationItem() {
    }

    public NavigationItem(String text, Drawable drawable) {
        this.text = text;
        this.drawable = drawable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
