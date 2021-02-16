package com.ranlychen.piemail;

import android.graphics.drawable.Drawable;

import androidx.wear.widget.drawer.WearableNavigationDrawerView;

import java.util.List;

public class NavigationViewAdapter extends WearableNavigationDrawerView.WearableNavigationDrawerAdapter {
    private List<NavigationItem> navItemList;

    public NavigationViewAdapter(List<NavigationItem> navItemList) {
        this.navItemList = navItemList;
    }

    @Override
    public CharSequence getItemText(int i) {
        return navItemList.get(i).getText();
    }

    @Override
    public Drawable getItemDrawable(int i) {
        return navItemList.get(i).getDrawable();
    }

    @Override
    public int getCount() {
        return navItemList.size();
    }
}
