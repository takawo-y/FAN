package com.takawo.fan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.takawo.fan.R;
import com.takawo.fan.db.data.PlayerDemoData;

import java.util.ArrayList;

/**
 * Created by Takawo on 2014/12/31.
 */
public class PlayerDemoAdapter extends RecyclerView.Adapter<PlayerDemoAdapter.ViewHolder>{

    private LayoutInflater inf;
    private ArrayList<PlayerDemoData> dataList;

    public PlayerDemoAdapter(Context context, ArrayList<PlayerDemoData> dataList){
        super();
        inf = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public PlayerDemoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inf.inflate(R.layout.list_player, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PlayerDemoData data = dataList.get(i);
//        viewHolder.text.setText(data);
        viewHolder.playerImage.setImageBitmap(data.getPlayerImg());
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
