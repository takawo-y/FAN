package com.takawo.fan.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.MyApplication;
import com.takawo.fan.util.FanUtil;
import com.takawo.fan.util.MyItemDecoration;
import com.takawo.fan.R;
import com.takawo.fan.adaptor.GameAdaptor;
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

    private Long id;
    private FandbPlayer playerDate;

    private RecyclerView.LayoutManager layoutManagerGame;

    @InjectView(R.id.tool_bar_game_list)
    Toolbar toolbar;

    @InjectView(R.id.list_game)
    RecyclerView recyclerViewGame;

    @OnClick(R.id.fab_game)
    void onClickAdd(){
        //Game新規登録画面を開く
        Intent intent = new Intent(GameActivity.this, GameRegistrationActivity.class);
        intent.putExtra(FanConst.INTENT_PLAYER_ID, id);
        startActivity(intent);
    }

    @OnClick(R.id.button_player_update)
    void onClickUpdate(){
        //Player更新画面を開く
        Intent intent = new Intent(GameActivity.this, PlayerUpdateActivity.class);
        intent.putExtra(FanConst.INTENT_PLAYER_ID, id);
        startActivity(intent);
    }

    @OnClick(R.id.button_player_delete)
    void onClickDelete(){
        //Player削除する
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player削除")
                .setMessage("Playerデータを削除しますか？\n紐づく試合情報も全て削除されます。")
                .setPositiveButton("はい",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                deletePlayer();
                                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                                intent.putExtra(FanConst.INTENT_PLAYER_ID, id);
                                startActivity(intent);
                            }
                        }
                )
                .setNegativeButton("いいえ",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){}
                        }
                )
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        id = intent.getLongExtra(FanConst.INTENT_PLAYER_ID, 0);
        playerDate = getPlayerData();

        setToolbar();  //ToolBar設定
        setList();  //一覧取得

    }

    /**
     * Toolbar設定
     */
    private void setToolbar(){
        if(playerDate.getPlayerImagePath() == null || playerDate.getPlayerImagePath().isEmpty()){
            toolbar.setLogo(R.drawable.no_image);
        }else{
            toolbar.setLogo(new BitmapDrawable(getResources(), FanUtil.resizeImage(playerDate.getPlayerImagePath(), 100)));
        }
        toolbar.setTitle(playerDate.getPlayerName());
        toolbar.setSubtitle(R.string.game_list_view_name);
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
    }

    /**
     * Player検索
     *
     * @return
     */
    private FandbPlayer getPlayerData(){
        MyApplication app = (MyApplication)getApplication();
        return app.getDaoSession().getFandbPlayerDao().load(id);

    }

    private void setList(){
        MyApplication app = (MyApplication)getApplication();
        List<FandbGame> list = app.getDaoSession().getFandbGameDao().queryBuilder()
                .where(FandbGameDao.Properties.PlayerId.eq(id)).orderDesc(FandbGameDao.Properties.GameDay).list();

        recyclerViewGame.setHasFixedSize(true);
        recyclerViewGame.addItemDecoration(new MyItemDecoration(this));

        layoutManagerGame = new LinearLayoutManager(this);
        recyclerViewGame.setLayoutManager(layoutManagerGame);
        recyclerViewGame.setAdapter(new GameAdaptor(this, list));
    }

    /**
     * Playerデータ削除
     */
    private void deletePlayer(){
        MyApplication app = (MyApplication)getApplication();
        app.getDaoSession().getFandbPlayerDao().deleteByKey(id);
    }
}
