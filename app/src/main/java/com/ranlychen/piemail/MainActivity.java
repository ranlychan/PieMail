package com.ranlychen.piemail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.wear.ambient.AmbientModeSupport;
import androidx.wear.widget.drawer.WearableDrawerLayout;
import androidx.wear.widget.drawer.WearableNavigationDrawerView;

import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements AmbientModeSupport.AmbientCallbackProvider {

    private WearableDrawerLayout mWearableDrawerLayout;
    private WearableNavigationDrawerView mWearableNavigationDrawerView;
    private List<NavigationItem> navItemList;
    private List<Fragment> fragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AmbientModeSupport.attach(this);

        initFragment();
        initView();

    }

    private void initView(){
        mWearableDrawerLayout = findViewById(R.id.mWearableDrawerLayout);
        mWearableNavigationDrawerView = findViewById(R.id.mWearableNavigationDrawerView);

        navItemList = new ArrayList<>();
        navItemList.add(new NavigationItem("收件箱",getDrawable(R.drawable.ic_mailbox)));
        navItemList.add(new NavigationItem("发件",getDrawable(R.drawable.ic_mail_compose)));
        navItemList.add(new NavigationItem("设置",getDrawable(R.drawable.ic_setting)));

        for(int j=0;j<fragmentList.size();j++){
            getSupportFragmentManager().beginTransaction().add(R.id.boxInsetLayout,fragmentList.get(j)).commit();
            getSupportFragmentManager().beginTransaction().hide(fragmentList.get(j)).commit();
        }
        getSupportFragmentManager().beginTransaction().show(fragmentList.get(0)).commit();

        mWearableNavigationDrawerView.setAdapter(new NavigationViewAdapter(navItemList));
        mWearableNavigationDrawerView.addOnItemSelectedListener(new WearableNavigationDrawerView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();

                for(int j=0;j<fragmentList.size();j++){
                    transaction.hide(fragmentList.get(j));
                }
                transaction.show(fragmentList.get(i));
                transaction.commit();

                //transaction.replace(R.id.boxInsetLayout,fragmentList.get(i)).commit();
                }
        });

    }

    private void initFragment() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new MailBoxFragment());
        fragmentList.add(new ComposeMailFragment());
        fragmentList.add(new SettingFragment());
    }

    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return null;
    }
}