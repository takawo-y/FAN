package com.takawo.fan.activity;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.adapter.GameInfoTabAdapter;
import com.takawo.fan.db.FandbGame;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.fragment.GameUpdateFragment;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.util.FanUtil;
import com.takawo.fan.util.KeyValuePair;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Takawo on 2015/01/20.
 */
public class GameUpdateActivity extends ActionBarActivity {

    private Long playerId;
    private Long gameId;
    private int fragmentPosition;
    private FandbPlayer playerData;

    public GameUpdateActivity() {
        super();
    }

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;
    @InjectView(R.id.game_tab)
    PagerSlidingTabStrip tab;
    @InjectView(R.id.game_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playerId = getIntent().getLongExtra(FanConst.INTENT_PLAYER_ID, 0);
        playerData = ((MyApplication)getApplication()).getDaoSession().getFandbPlayerDao().load(playerId);
        gameId = getIntent().getLongExtra(FanConst.INTENT_GAME_ID, 0);
        fragmentPosition = getIntent().getIntExtra(FanConst.INTENT_FRAGMENT_POSITION, 99);
        setContentView(R.layout.activity_game_update);

        ButterKnife.inject(this);

        setToolbar();  //ToolBar設定
        viewPager.setAdapter(new GameInfoTabAdapter(getSupportFragmentManager(), playerId, gameId));
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        if(fragmentPosition != 99){
            viewPager.setCurrentItem(fragmentPosition);
        }
        tab.setViewPager(viewPager);
    }

    /**
     * Toolbar設定
     */
    private void setToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_done_grey600_36dp);
        if(playerData.getPlayerImagePath() == null || playerData.getPlayerImagePath().isEmpty()){
            toolbar.setLogo(R.drawable.no_image);
        }else{
            toolbar.setLogo(new BitmapDrawable(getResources(), FanUtil.resizeImage(playerData.getPlayerImagePath(), 100)));
        }
        toolbar.setTitle(playerData.getPlayerName());
        toolbar.setSubtitle(R.string.game_update_view_name);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent intent = new Intent(GameUpdateActivity.this, GameActivity.class);
                                                     intent.putExtra(FanConst.INTENT_PLAYER_ID, playerId);
                                                     startActivity(intent);
                                                 }
                                             }

        );
    }

}
