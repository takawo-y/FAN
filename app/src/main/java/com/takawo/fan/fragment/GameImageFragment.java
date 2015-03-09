package com.takawo.fan.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.activity.GameImageRegistrationActivity;
import com.takawo.fan.activity.GameUpdateActivity;
import com.takawo.fan.adapter.ImageAdapter;
import com.takawo.fan.db.FandbImage;
import com.takawo.fan.db.FandbImageDao;
import com.takawo.fan.db.data.DBHelper;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.view.GridViewScrollable;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 9004027600 on 2015/02/19.
 */
public class GameImageFragment extends Fragment{

    private Long playerId;
    private Long gameId;
    private RecyclerView.LayoutManager layoutManagerImage;

    @InjectView(R.id.list_image)
    GridViewScrollable gridImage;

    @OnClick(R.id.fab_image)
    void onClickAddImage(){
        Intent intent = new Intent(getActivity(), GameImageRegistrationActivity.class);
        intent.putExtra(FanConst.INTENT_PLAYER_ID, playerId);
        intent.putExtra(FanConst.INTENT_GAME_ID, gameId);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_image, container, false);
        ButterKnife.inject(this, view);

        playerId = getArguments().getLong(FanConst.INTENT_PLAYER_ID);
        gameId = getArguments().getLong(FanConst.INTENT_GAME_ID);
        setData();
        return view;
    }

    private ImageView dialogImageView;
    private Bitmap dialogImage;
    private void setData(){
        final List<FandbImage> list = ((MyApplication)getActivity().getApplication()).getDaoSession().getFandbImageDao()
                .queryBuilder().where(FandbImageDao.Properties.GameId.eq(gameId)).orderAsc(FandbImageDao.Properties.Id).list();
        gridImage.setAdapter(new ImageAdapter(getActivity(), list));
        gridImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FandbImage data = list.get(position);

                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);
                if(dialogImageView != null){
                    dialogImageView.setImageDrawable(null);
                }
                dialogImageView = new ImageView(getActivity());
                if(dialogImage != null){
                    dialogImage.recycle();
                    dialogImage = null;
                }
                dialogImage = BitmapFactory.decodeFile(data.getPath());
                dialogImageView.setImageBitmap(dialogImage);
                dialogImageView.setScaleType(ImageView.ScaleType.FIT_START);
                dialogImageView.setAdjustViewBounds(true);
                layout.addView(dialogImageView);

                new AlertDialog.Builder(getActivity())
                        .setTitle(data.getTitle())
                        .setView(layout)
                        .setMessage(data.getComment() + "\r\n" + data.getPath())
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
        gridImage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int index = position;
                new AlertDialog.Builder(getActivity())
                        .setTitle("画像削除")
                        .setMessage("画像を削除しますか？")
                        .setPositiveButton("はい",
                                new DialogInterface.OnClickListener(){
                                    FandbImage data = list.get(index);
                                    public void onClick(DialogInterface dialog, int which){
                                        Log.d("Delete Image", "ID:"+data.getId());
                                        (new DBHelper(getActivity(), null)).getDaoSession().getFandbImageDao().deleteByKey(data.getId());
                                        ((GameUpdateActivity)getActivity()).updateFragment(1);
                                    }
                                }
                        )
                        .setNegativeButton("いいえ",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which){}
                                }
                        )
                        .show();

                return false;
            }
        });
    }
}
