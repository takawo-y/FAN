package com.example.takawo.fan.db;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;

import java.io.IOException;

/**
 * Created by Takawo on 2014/12/28.
 */
public class DBManager {

    private final String TAG = "FAN";
    private Manager manager;
    private Database db;

    private final String dbname = "fandb";

    public DBManager(Context context){
        try{
            manager = new Manager(new AndroidContext(context),Manager.DEFAULT_OPTIONS);
        }catch (IOException e){
            Log.e(TAG, "Cannot create manager object");
            return;
        }
    }

    public Database createDB(){
        try {
            db = manager.getDatabase(dbname);
            Log.d (TAG, "Database created");

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot get database");
            return null;
        }
        return db;
    }
}
