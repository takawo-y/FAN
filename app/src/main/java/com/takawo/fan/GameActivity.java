package com.takawo.fan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.takawo.fan.db.FandbGame;
import com.takawo.fan.db.FandbGameDao;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 9004027600 on 2015/02/04.
 */
public class GameActivity extends ActionBarActivity {

    @InjectView(R.id.tool_bar_game_list)
    Toolbar toolbar;

    @OnClick(R.id.fab_game)
    void onClickAdd(){
        //Game新規登録画面を開く
//        Intent intent = new Intent(MainActivity.this, PlayerRegistrationActivity.class);
//        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.inject(this);

        //ToolBar設定
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setTitle(R.string.game_list_view_name);

        //Intent
        Intent intent = getIntent();
        Long id = intent.getLongExtra("playerId", 0);

        //DB接続
        MyApplication app = (MyApplication)getApplication();
        List<FandbGame> list = app.getDaoSession().getFandbGameDao().queryBuilder()
                .where(FandbGameDao.Properties.PlayerId.eq(id)).list();

    }
}
