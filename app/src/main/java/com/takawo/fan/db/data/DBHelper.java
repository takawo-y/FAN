package com.takawo.fan.db.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.takawo.fan.db.DaoMaster;
import com.takawo.fan.db.DaoSession;

/**
 * Created by Takawo on 2015/02/01.
 */
public class DBHelper extends DaoMaster.OpenHelper{
    private SQLiteDatabase db = null;
    private DaoMaster daoMaster = null;
    private DaoSession daoSession = null;
    private static String DB_NAME = "fandb.sqlite";

    public DBHelper(Context context, SQLiteDatabase.CursorFactory factory){
        super(context, DB_NAME, factory);
        this.getDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);

        // ローカル変数としてセッションを作成する
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }

    public SQLiteDatabase getDatabase() {
        if (this.db == null) {
            this.db = this.getWritableDatabase();
        }

        return this.db;
    }

    public DaoMaster getDaoMaster() {
        if (this.daoMaster == null) {
            this.daoMaster = new DaoMaster(this.getDatabase());
        }

        return this.daoMaster;
    }

    public DaoSession getDaoSession() {
        if (this.daoSession == null) {
            this.daoSession = this.getDaoMaster().newSession();
        }

        return this.daoSession;
    }
}
