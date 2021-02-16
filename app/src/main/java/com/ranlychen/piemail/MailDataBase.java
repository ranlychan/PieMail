package com.ranlychen.piemail;


import com.raizlabs.android.dbflow.annotation.Database;

import org.jetbrains.annotations.NotNull;

@Database(name = MailDataBase.NAME, version = MailDataBase.VERSION)
public final class MailDataBase{
    //数据库名称
    public static final String NAME = "MailDataBase";
    //数据库版本号
    public static final int VERSION = 1;

}
