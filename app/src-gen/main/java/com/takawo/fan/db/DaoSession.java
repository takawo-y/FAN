package com.takawo.fan.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.db.FandbGame;
import com.takawo.fan.db.FandbImage;

import com.takawo.fan.db.FandbPlayerDao;
import com.takawo.fan.db.FandbGameDao;
import com.takawo.fan.db.FandbImageDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig fandbPlayerDaoConfig;
    private final DaoConfig fandbGameDaoConfig;
    private final DaoConfig fandbImageDaoConfig;

    private final FandbPlayerDao fandbPlayerDao;
    private final FandbGameDao fandbGameDao;
    private final FandbImageDao fandbImageDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        fandbPlayerDaoConfig = daoConfigMap.get(FandbPlayerDao.class).clone();
        fandbPlayerDaoConfig.initIdentityScope(type);

        fandbGameDaoConfig = daoConfigMap.get(FandbGameDao.class).clone();
        fandbGameDaoConfig.initIdentityScope(type);

        fandbImageDaoConfig = daoConfigMap.get(FandbImageDao.class).clone();
        fandbImageDaoConfig.initIdentityScope(type);

        fandbPlayerDao = new FandbPlayerDao(fandbPlayerDaoConfig, this);
        fandbGameDao = new FandbGameDao(fandbGameDaoConfig, this);
        fandbImageDao = new FandbImageDao(fandbImageDaoConfig, this);

        registerDao(FandbPlayer.class, fandbPlayerDao);
        registerDao(FandbGame.class, fandbGameDao);
        registerDao(FandbImage.class, fandbImageDao);
    }
    
    public void clear() {
        fandbPlayerDaoConfig.getIdentityScope().clear();
        fandbGameDaoConfig.getIdentityScope().clear();
        fandbImageDaoConfig.getIdentityScope().clear();
    }

    public FandbPlayerDao getFandbPlayerDao() {
        return fandbPlayerDao;
    }

    public FandbGameDao getFandbGameDao() {
        return fandbGameDao;
    }

    public FandbImageDao getFandbImageDao() {
        return fandbImageDao;
    }

}
