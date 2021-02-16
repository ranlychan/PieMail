package com.ranlychen.piemail;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

public class PieMailApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        dbFlowInit();
    }
    private void dbFlowInit(){
        try {
            FlowManager.init(new FlowConfig.Builder(getApplicationContext())
                    .openDatabasesOnInit(true) .build());
            FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
