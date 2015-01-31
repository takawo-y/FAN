package com.takawo.fan;

import android.app.Application;

import com.takawo.fan.db.DaoSession;
import com.takawo.fan.db.data.DBHelper;

/**
 * Created by Takawo on 2015/02/01.
 */
public class MyApplication extends Application {

    private DBHelper dbHelper;
    @Override
    public void onCreate() {
        super.onCreate();

        this.dbHelper = new DBHelper(this, null);
    }

    public DaoSession getDaoSession() {
        return this.dbHelper.getDaoSession();
    }

}
