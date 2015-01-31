package com.takawo.fan.adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbPlayer;

import java.util.List;

/**
 * Created by Takawo on 2014/12/31.
 */
public class PlayerAdaptor extends RecyclerView.Adapter<PlayerAdaptor.ViewHolder>{

    private LayoutInflater inf;
    private List<FandbPlayer> dataList;
    private Context context;
    private Drawable noImage;

    public PlayerAdaptor(Context context, List<FandbPlayer> dataList){
        super();
        this.context = context;
        inf = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    public PlayerAdaptor(Context context, List<FandbPlayer> dataList, Drawable noImage){
        super();
        this.context = context;
        inf = LayoutInflater.from(context);
        this.dataList = dataList;
        this.noImage = noImage;
    }

    @Override
    public PlayerAdaptor.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inf.inflate(R.layout.player_list, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        FandbPlayer data = dataList.get(i);
        Picasso.with(context)
                .load(data.getPlayerImagePath())
                .error(noImage)  //存在しない場合、NoImage
                .into(viewHolder.playerImage);
        viewHolder.playerName.setText(data.getPlayerName());
        viewHolder.gameEvent.setText(data.getGameEvent());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView playerImage;
        TextView playerName;
        TextView gameEvent;

        public ViewHolder(View v) {
            super(v);
            playerImage = (ImageView)v.findViewById(R.id.playerImg);
            playerName = (TextView)v.findViewById(R.id.playerName);
            gameEvent = (TextView)v.findViewById(R.id.gameEvent);
        }
    }
}
