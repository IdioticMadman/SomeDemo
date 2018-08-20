package com.robert.somedemo;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import robert.somedemo.DaoMaster;
import robert.somedemo.DaoSession;

/**
 * Created by robert on 2017/3/5.
 */

public class MyApplication extends Application {


    public static final boolean ENCRYPTED = true;
    private static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }
}
