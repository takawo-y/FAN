package com.takawo.fan;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.takawo.fan.adaptor.GameAdaptor;
import com.takawo.fan.adaptor.PlayerAdaptor;
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

    private RecyclerView.LayoutManager layoutManagerGame;

    @InjectView(R.id.tool_bar_game_list)
    Toolbar toolbar;

    @InjectView(R.id.list_game)
    RecyclerView recyclerViewGame;

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

        //Intent
        Intent intent = getIntent();
        Long id = intent.getLongExtra("playerId", 0);
        String playerName = intent.getStringExtra("playerName");
        String playerImage = intent.getStringExtra("playerImage");
        Log.d("Game","PlayerName="+playerName);

        //ToolBar設定
        if(playerImage == null || playerImage.isEmpty()){
            toolbar.setLogo(R.drawable.no_image);
        }else{

        }
        toolbar.setTitle(playerName);
        toolbar.setNavigationIcon(R.drawable.ic_done_grey600_36dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent intent = new Intent(GameActivity.this, MainActivity.class);
                                                     startActivity(intent);
                                                 }
                                             }

        );

        //DB接続
        MyApplication app = (MyApplication)getApplication();
        List<FandbGame> list = app.getDaoSession().getFandbGameDao().queryBuilder()
                .where(FandbGameDao.Properties.PlayerId.eq(id)).list();

        recyclerViewGame.setHasFixedSize(true);
        recyclerViewGame.addItemDecoration(new MyItemDecoration(this));

        layoutManagerGame = new LinearLayoutManager(this);
        recyclerViewGame.setLayoutManager(layoutManagerGame);
        recyclerViewGame.setAdapter(new GameAdaptor(this, list));

    }
}
