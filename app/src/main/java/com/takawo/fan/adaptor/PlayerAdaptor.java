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
import com.takawo.fan.util.BitmapTransformation;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.activity.GameActivity;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbPlayer;

import java.io.File;
import java.util.List;

/**
 * Created by Takawo on 2014/12/31.
 */
public class PlayerAdaptor extends RecyclerView.Adapter<PlayerAdaptor.ViewHolder>{

    private LayoutInflater inf;
    private List<FandbPlayer> dataList;
    private Context context;

    public PlayerAdaptor(Context context, List<FandbPlayer> dataList){
        super();
        this.context = context;
        inf = LayoutInflater.from(context);
        this.dataList = dataList;
        data = null;
    }

    @Override
    public PlayerAdaptor.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inf.inflate(R.layout.player_list, null);
        ViewHolder viewHolder = new ViewHolder(context, view);
        return viewHolder;
    }

    FandbPlayer data;
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        data = dataList.get(i);
        viewHolder.setItem(data);
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameActivity.class);
                intent.putExtra(FanConst.INTENT_PLAYER_ID, data.getId());
                intent.putExtra(FanConst.INTENT_PLAYER_NAME, data.getPlayerName());
                intent.putExtra(FanConst.INTENT_PLAYER_IMAGE, data.getPlayerImagePath());
                intent.putExtra(FanConst.INTENT_RESULT_TYPE, data.getResultType());
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
        ImageView playerImage;
        TextView playerName;
        TextView gameEvent;

        public ViewHolder(Context context, View v) {
            super(v);
            this.context = context;
            linearLayout = (LinearLayout)v.findViewById(R.id.lily_player);

            v.setOnClickListener(this);
            playerImage = (ImageView)v.findViewById(R.id.playerImg);
            playerName = (TextView)v.findViewById(R.id.playerName);
            gameEvent = (TextView)v.findViewById(R.id.gameEvent);
        }

        public void setItem(FandbPlayer data){
            playerId = data.getId();
            if(null == data.getPlayerImagePath() || "".equals(data.getPlayerImagePath())){
                Picasso.with(context)
                        .load(R.drawable.no_image)
                        .transform(new BitmapTransformation())
                        .into(playerImage);
            }else{
                Picasso.with(context)
                        .load(new File(data.getPlayerImagePath()))
                        .transform(new BitmapTransformation())
                        .into(playerImage);
            }
            playerName.setText(data.getPlayerName());
            gameEvent.setText(data.getGameEvent());

        }

        @Override
        public void onClick(View view){
            //リストonClick処理
        }
    }

}
