package com.takawo.fan.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.takawo.fan.GameActivity;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbGame;
import com.takawo.fan.db.FandbPlayer;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Takawo on 2014/12/31.
 */
public class GameAdaptor extends RecyclerView.Adapter<GameAdaptor.ViewHolder>{

    private LayoutInflater inf;
    private List<FandbGame> dataList;
    private Context context;

    public GameAdaptor(Context context, List<FandbGame> dataList){
        super();
        this.context = context;
        inf = LayoutInflater.from(context);
        this.dataList = dataList;
        data = null;
    }

    @Override
    public GameAdaptor.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inf.inflate(R.layout.player_list, null);
        ViewHolder viewHolder = new ViewHolder(context, view);
        return viewHolder;
    }

    FandbGame data;
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        data = dataList.get(i);
        viewHolder.setItem(data);
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameActivity.class);
                intent.putExtra("playerId", data.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        Context context;
        LinearLayout linearLayout;

        Long playerId;
        Long id;
        TextView gameDay;
        TextView gameCategory;
        TextView gameInfo;
        TextView opposition;
        TextView result;
        TextView resultScoreTime;

        public ViewHolder(Context context, View v) {
            super(v);
            this.context = context;
            linearLayout = (LinearLayout)v.findViewById(R.id.lily_player);

            v.setOnClickListener(this);
//            playerImage = (ImageView)v.findViewById(R.id.playerImg);
        }

        public void setItem(FandbGame data){
            playerId = data.getPlayerId();
            id = data.getId();
            SimpleDateFormat formatA = new SimpleDateFormat("yyyy/MM/dd");
            String formatDate = formatA.format(data.getGameDay());
            gameDay.setText(formatDate);
            gameCategory.setText(data.getGameCategory());
            //gameInfo.setText(data.getga);

        }

        @Override
        public void onClick(View view){
            //リストonClick処理
        }
    }

}
