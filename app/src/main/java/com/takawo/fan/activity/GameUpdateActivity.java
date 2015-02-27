package com.takawo.fan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;
import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.adapter.GameInfoTabAdapter;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.util.BitmapTransformation;
import com.takawo.fan.util.FanConst;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Takawo on 2015/01/20.
 */
public class GameUpdateActivity extends ActionBarActivity {

    private Long playerId;
    private Long gameId;
    private int fragmentPosition;
    private FandbPlayer playerData;
    private GameInfoTabAdapter adapter;

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
        Log.d("GameUpdateActivity", "FragmentPosition:"+fragmentPosition);
        setContentView(R.layout.activity_game_update);

        ButterKnife.inject(this);

        setToolbar();  //ToolBar設定

        adapter = new GameInfoTabAdapter(getSupportFragmentManager(), playerId, gameId);
        viewPager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        if(fragmentPosition != 99){
            viewPager.setCurrentItem(fragmentPosition, true);
        }
        tab.setViewPager(viewPager);
    }

    /**
     * Toolbar設定
     */
    private void setToolbar(){
        toolbar.setLogo(R.drawable.no_image);
        if(playerData.getPlayerImagePath() != null && playerData.getPlayerImagePath().isEmpty() == false){
            for (int i = 0; i < toolbar.getChildCount(); i++) {
                View child = toolbar.getChildAt(i);
                if (child != null)
                    if (child.getClass() == ImageView.class) {
                        ImageView logoView = (ImageView) child;
                        if(logoView.getId() == -1){
                            Picasso.with(this)
                                    .load(new File(playerData.getPlayerImagePath()))
                                    .transform(new BitmapTransformation())
                                    .resize(100, 100)
                                    .centerInside()
                                    .into(logoView);
                        }
                    }
            }
        }
        toolbar.setNavigationIcon(R.drawable.ic_done_grey600_36dp);
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

    public void updateFragment(int posision){
        //Fragmentから呼ばれる
        //ページのフラグメントを全て削除し再セット
        adapter.destroyAllItem(tab);
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        viewPager.setCurrentItem(posision);
    }

}
