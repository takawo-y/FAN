package com.example.takawo.fan.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.takawo.fan.R;
import com.example.takawo.fan.db.data.PlayerData;
import com.example.takawo.fan.db.view.Player;

import java.util.ArrayList;

/**
 * Created by Takawo on 2014/12/31.
 */
public class PlayerAdaptor extends RecyclerView.Adapter<PlayerAdaptor.ViewHolder>{

    private LayoutInflater inf;
    private ArrayList<PlayerData> dataList;

    public PlayerAdaptor(Context context, ArrayList<PlayerData> dataList){
        super();
        inf = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public PlayerAdaptor.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inf.inflate(R.layout.player_list, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PlayerData data = (PlayerData)dataList.get(i);
//        viewHolder.text.setText(data);
        viewHolder.playerName.setText(data.getPlayerName());
        viewHolder.gameEvent.setText(data.getGameEvent());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView playerName;
        TextView gameEvent;

        public ViewHolder(View v) {
            super(v);
            playerName = (TextView)v.findViewById(R.id.playerName);
            gameEvent = (TextView)v.findViewById(R.id.gameEvent);
        }
    }
}
