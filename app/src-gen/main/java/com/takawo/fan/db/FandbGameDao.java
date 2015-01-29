package com.takawo.fan.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.takawo.fan.db.FandbGame;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table FANDB_GAME.
*/
public class FandbGameDao extends AbstractDao<FandbGame, Void> {

    public static final String TABLENAME = "FANDB_GAME";

    /**
     * Properties of entity FandbGame.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property PlayerId = new Property(0, long.class, "playerId", true, "PLAYER_ID");
        public final static Property GameId = new Property(1, long.class, "gameId", true, "GAME_ID");
        public final static Property GameType = new Property(2, long.class, "gameType", false, "GAME_TYPE");
        public final static Property GameCategory = new Property(3, String.class, "gameCategory", false, "GAME_CATEGORY");
        public final static Property Place = new Property(4, String.class, "place", false, "PLACE");
        public final static Property Weather = new Property(5, String.class, "weather", false, "WEATHER");
        public final static Property Temperature = new Property(6, String.class, "temperature", false, "TEMPERATURE");
        public final static Property GameDay = new Property(7, java.util.Date.class, "gameDay", false, "GAME_DAY");
        public final static Property StartTime = new Property(8, String.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(9, String.class, "endTime", false, "END_TIME");
        public final static Property Opposition = new Property(10, String.class, "opposition", false, "OPPOSITION");
        public final static Property Result = new Property(11, String.class, "result", false, "RESULT");
        public final static Property ResultScore = new Property(12, String.class, "resultScore", false, "RESULT_SCORE");
        public final static Property ResultTime = new Property(13, String.class, "resultTime", false, "RESULT_TIME");
        public final static Property Comment = new Property(14, String.class, "comment", false, "COMMENT");
    };


    public FandbGameDao(DaoConfig config) {
        super(config);
    }
    
    public FandbGameDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'FANDB_GAME' (" + //
                "'PLAYER_ID' INTEGER PRIMARY KEY NOT NULL ," + // 0: playerId
                "'GAME_ID' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 1: gameId
                "'GAME_TYPE' INTEGER NOT NULL ," + // 2: gameType
                "'GAME_CATEGORY' TEXT," + // 3: gameCategory
                "'PLACE' TEXT," + // 4: place
                "'WEATHER' TEXT," + // 5: weather
                "'TEMPERATURE' TEXT," + // 6: temperature
                "'GAME_DAY' INTEGER NOT NULL ," + // 7: gameDay
                "'START_TIME' TEXT," + // 8: startTime
                "'END_TIME' TEXT," + // 9: endTime
                "'OPPOSITION' TEXT," + // 10: opposition
                "'RESULT' TEXT," + // 11: result
                "'RESULT_SCORE' TEXT," + // 12: resultScore
                "'RESULT_TIME' TEXT," + // 13: resultTime
                "'COMMENT' TEXT);"); // 14: comment
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'FANDB_GAME'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, FandbGame entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getPlayerId());
        stmt.bindLong(2, entity.getGameId());
        stmt.bindLong(3, entity.getGameType());
 
        String gameCategory = entity.getGameCategory();
        if (gameCategory != null) {
            stmt.bindString(4, gameCategory);
        }
 
        String place = entity.getPlace();
        if (place != null) {
            stmt.bindString(5, place);
        }
 
        String weather = entity.getWeather();
        if (weather != null) {
            stmt.bindString(6, weather);
        }
 
        String temperature = entity.getTemperature();
        if (temperature != null) {
            stmt.bindString(7, temperature);
        }
        stmt.bindLong(8, entity.getGameDay().getTime());
 
        String startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindString(9, startTime);
        }
 
        String endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindString(10, endTime);
        }
 
        String opposition = entity.getOpposition();
        if (opposition != null) {
            stmt.bindString(11, opposition);
        }
 
        String result = entity.getResult();
        if (result != null) {
            stmt.bindString(12, result);
        }
 
        String resultScore = entity.getResultScore();
        if (resultScore != null) {
            stmt.bindString(13, resultScore);
        }
 
        String resultTime = entity.getResultTime();
        if (resultTime != null) {
            stmt.bindString(14, resultTime);
        }
 
        String comment = entity.getComment();
        if (comment != null) {
            stmt.bindString(15, comment);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public FandbGame readEntity(Cursor cursor, int offset) {
        FandbGame entity = new FandbGame( //
            cursor.getLong(offset + 0), // playerId
            cursor.getLong(offset + 1), // gameId
            cursor.getLong(offset + 2), // gameType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // gameCategory
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // place
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // weather
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // temperature
            new java.util.Date(cursor.getLong(offset + 7)), // gameDay
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // startTime
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // endTime
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // opposition
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // result
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // resultScore
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // resultTime
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14) // comment
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, FandbGame entity, int offset) {
        entity.setPlayerId(cursor.getLong(offset + 0));
        entity.setGameId(cursor.getLong(offset + 1));
        entity.setGameType(cursor.getLong(offset + 2));
        entity.setGameCategory(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPlace(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setWeather(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTemperature(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setGameDay(new java.util.Date(cursor.getLong(offset + 7)));
        entity.setStartTime(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setEndTime(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setOpposition(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setResult(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setResultScore(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setResultTime(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setComment(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(FandbGame entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(FandbGame entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
