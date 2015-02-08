package com.takawo.fan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.takawo.fan.FanConst;
import com.takawo.fan.MyApplication;
import com.takawo.fan.MyItemDecoration;
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
    private String playerName;
    private String playerImage;
    private long resultType;

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
        intent.putExtra(FanConst.INTENT_PLAYER_NAME, playerName);
        intent.putExtra(FanConst.INTENT_PLAYER_IMAGE, playerImage);
        intent.putExtra(FanConst.INTENT_RESULT_TYPE, resultType);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.inject(this);
        getValueFromIntent();

        setToolbar();  //ToolBar設定
        setList();  //一覧取得

    }

    /**
     * Intentから値の取得
     */
    private void getValueFromIntent(){
        Intent intent = getIntent();
        id = intent.getLongExtra(FanConst.INTENT_PLAYER_ID, 0);
        playerName = intent.getStringExtra(FanConst.INTENT_PLAYER_NAME);
        playerImage = intent.getStringExtra(FanConst.INTENT_PLAYER_IMAGE);
        resultType = intent.getLongExtra(FanConst.INTENT_RESULT_TYPE, 0);
    }

    /**
     * Toolbar設定
     */
    private void setToolbar(){
        if(playerImage == null || playerImage.isEmpty()){
            toolbar.setLogo(R.drawable.no_image);
        }else{

        }
        toolbar.setTitle(playerName);
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

}
