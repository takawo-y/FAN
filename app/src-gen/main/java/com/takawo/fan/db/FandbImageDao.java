package com.takawo.fan.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.takawo.fan.db.FandbImage;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table FANDB_IMAGE.
*/
public class FandbImageDao extends AbstractDao<FandbImage, Long> {

    public static final String TABLENAME = "FANDB_IMAGE";

    /**
     * Properties of entity FandbImage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property PlayerId = new Property(0, long.class, "playerId", false, "PLAYER_ID");
        public final static Property GameId = new Property(1, long.class, "gameId", false, "GAME_ID");
        public final static Property Id = new Property(2, Long.class, "id", true, "_id");
        public final static Property Path = new Property(3, String.class, "path", false, "PATH");
        public final static Property Title = new Property(4, String.class, "title", false, "TITLE");
        public final static Property Comment = new Property(5, String.class, "comment", false, "COMMENT");
    };


    public FandbImageDao(DaoConfig config) {
        super(config);
    }
    
    public FandbImageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'FANDB_IMAGE' (" + //
                "'PLAYER_ID' INTEGER NOT NULL ," + // 0: playerId
                "'GAME_ID' INTEGER NOT NULL ," + // 1: gameId
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 2: id
                "'PATH' TEXT," + // 3: path
                "'TITLE' TEXT," + // 4: title
                "'COMMENT' TEXT);"); // 5: comment
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'FANDB_IMAGE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, FandbImage entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getPlayerId());
        stmt.bindLong(2, entity.getGameId());
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(3, id);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(4, path);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(5, title);
        }
 
        String comment = entity.getComment();
        if (comment != null) {
            stmt.bindString(6, comment);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2);
    }    

    /** @inheritdoc */
    @Override
    public FandbImage readEntity(Cursor cursor, int offset) {
        FandbImage entity = new FandbImage( //
            cursor.getLong(offset + 0), // playerId
            cursor.getLong(offset + 1), // gameId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // path
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // title
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // comment
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, FandbImage entity, int offset) {
        entity.setPlayerId(cursor.getLong(offset + 0));
        entity.setGameId(cursor.getLong(offset + 1));
        entity.setId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setPath(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTitle(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setComment(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(FandbImage entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(FandbImage entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
