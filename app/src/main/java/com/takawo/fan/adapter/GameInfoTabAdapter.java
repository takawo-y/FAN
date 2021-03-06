package com.takawo.fan.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.takawo.fan.fragment.GameImageFragment;
import com.takawo.fan.fragment.GameUpdateFragment;
import com.takawo.fan.util.FanConst;

/**
 * Created by Takawo on 2015/02/20.
 */
public class GameInfoTabAdapter extends FragmentStatePagerAdapter {

    final String[] TITLES = {"詳細", "画像"};
    private Long playerId;
    private Long gameId;

    public GameInfoTabAdapter(FragmentManager fm, Long playerId, Long gameId) {
        super(fm);
        this.playerId = playerId;
        this.gameId = gameId;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putLong(FanConst.INTENT_PLAYER_ID, playerId);
        bundle.putLong(FanConst.INTENT_GAME_ID, gameId);
        switch (position){
            case 0:
                GameUpdateFragment updateFragment = new GameUpdateFragment();
                updateFragment.setArguments(bundle);
                return updateFragment;
            case 1:
                GameImageFragment imageFragment = new GameImageFragment();
                imageFragment.setArguments(bundle);
                return imageFragment;
            default:
                return new GameUpdateFragment();
        }
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

        if(position < getCount()){
            FragmentManager manager = ((Fragment)object).getFragmentManager();
            manager.beginTransaction().remove((Fragment)object).commit();
        }
    }

    public void destroyAllItem(PagerSlidingTabStrip pager) {
        for (int i = 0; i < getCount() - 1; i++) {
            try {
                Object obj = this.instantiateItem(pager, i);
                if (obj != null)
                    destroyItem(pager, i, obj);
            } catch (Exception e) {
            }
        }
    }

}
