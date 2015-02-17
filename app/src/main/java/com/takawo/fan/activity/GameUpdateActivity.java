package com.takawo.fan.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.fragment.GameUpdateFragment;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.util.FanUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Takawo on 2015/01/20.
 */
public class GameUpdateActivity extends ActionBarActivity {
    private Long playerId;
    private Long gameId;
    private FandbPlayer playerData;

    private static final String FRAGMENT_TAG_UPDATE = "update_fragment_tag";
    private GameUpdateFragment fragmentUpdate;


    public GameUpdateActivity() {
        super();
    }

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;
    @InjectView(R.id.tabs_game)
    PagerSlidingTabStrip tab;
    @InjectView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playerId = getIntent().getLongExtra(FanConst.INTENT_PLAYER_ID, 0);
        playerData = ((MyApplication)getApplication()).getDaoSession().getFandbPlayerDao().load(playerId);
        gameId = getIntent().getLongExtra(FanConst.INTENT_GAME_ID, 0);
        setFragment(savedInstanceState);
        setContentView(R.layout.activity_game_update);

        ButterKnife.inject(this);

        setToolbar();  //ToolBar設定
//        tab.setViewPager(pager.setAdapter(new Test));
    }

    /**
     * Fragmentセット
     */
    private void setFragment(Bundle savedInstanceState ){
        Bundle bundle = new Bundle();
        bundle.putLong(FanConst.INTENT_PLAYER_ID, playerId);
        bundle.putLong(FanConst.INTENT_GAME_ID, gameId);
        if(savedInstanceState != null){
            fragmentUpdate = (GameUpdateFragment)getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_UPDATE);
        }else{
            fragmentUpdate = new GameUpdateFragment();
        }
        fragmentUpdate.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.game_update_fragment, fragmentUpdate, "Update Fragment").commit();
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

//    public class MyPagerAdapter extends FragmentPagerAdapter {
//
//        private final String[] TITLES = {"詳細", "写真"};
//
//        public MyPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return TITLES[position];
//        }
//
//        @Override
//        public int getCount() {
//            return TITLES.length;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return SuperAwesomeCardFragment.newInstance(position);
//        }
//    }
}