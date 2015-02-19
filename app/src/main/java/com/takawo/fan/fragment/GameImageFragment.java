package com.takawo.fan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.adapter.GameAdapter;
import com.takawo.fan.adapter.ImageAdapter;
import com.takawo.fan.db.FandbImage;
import com.takawo.fan.db.FandbImageDao;
import com.takawo.fan.util.MyItemDecoration;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 9004027600 on 2015/02/19.
 */
public class GameImageFragment extends Fragment{

    private Long playerId;
    private Long gameId;
    private RecyclerView.LayoutManager layoutManagerImage;

    @InjectView(R.id.list_image)
    RecyclerView recyclerViewImage;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_image, container, false);
        ButterKnife.inject(this, view);

        setData();
        return view;
    }

    private void setData(){
        List<FandbImage> list = ((MyApplication)getActivity().getApplication()).getDaoSession().getFandbImageDao()
                .queryBuilder().where(FandbImageDao.Properties.GameId.eq(gameId)).orderAsc(FandbImageDao.Properties.Id).list();
        recyclerViewImage.setHasFixedSize(true);
        recyclerViewImage.addItemDecoration(new MyItemDecoration(getActivity()));

        layoutManagerImage= new LinearLayoutManager(getActivity());
        recyclerViewImage.setLayoutManager(layoutManagerImage);
        recyclerViewImage.setAdapter(new ImageAdapter(getActivity(), list));
    }
}
