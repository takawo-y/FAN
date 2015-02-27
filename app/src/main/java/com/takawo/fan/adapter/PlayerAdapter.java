package com.takawo.fan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
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
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder>{

    private RecyclerView recyclerView;
    private LayoutInflater inf;
    private List<FandbPlayer> dataList;
    private Context context;

    public PlayerAdapter(Context context, List<FandbPlayer> dataList){
        super();
        this.context = context;
        inf = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView= recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    @Override
    public PlayerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inf.inflate(R.layout.list_player, null);
        ViewHolder viewHolder = new ViewHolder(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setItem(dataList.get(i));
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click時、試合一覧に遷移する
                Intent intent = new Intent(context, GameActivity.class);
                FandbPlayer data = dataList.get(recyclerView.getChildPosition(v));
                intent.putExtra(FanConst.INTENT_PLAYER_ID, data.getId());
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
        CardView linearLayout;

        Long playerId;
        ImageView playerImage;
        TextView playerName;
        TextView gameEvent;
        TextView category;

        public ViewHolder(Context context, View v) {
            super(v);
            this.context = context;
            linearLayout = (CardView)v.findViewById(R.id.lily_player);

            v.setOnClickListener(this);
            playerImage = (ImageView)v.findViewById(R.id.playerImg);
            playerName = (TextView)v.findViewById(R.id.playerName);
            gameEvent = (TextView)v.findViewById(R.id.gameEvent);
            category = (TextView)v.findViewById(R.id.category);
        }

        public void setItem(FandbPlayer data){
            playerId = data.getId();
            if(null == data.getPlayerImagePath() || "".equals(data.getPlayerImagePath())){
                Picasso.with(context)
                        .load(R.drawable.no_image)
                        .transform(new BitmapTransformation())
//                        .resize(250, 250)
                        .into(playerImage);
            }else{
                Picasso.with(context)
                        .load(new File(data.getPlayerImagePath()))
                        .transform(new BitmapTransformation())
                        .resize(250, 250)
                        .centerInside()
                        .into(playerImage);
            }
            playerName.setText(data.getPlayerName());
            gameEvent.setText(data.getGameEvent());
            category.setText(data.getCategory());
        }

        @Override
        public void onClick(View view){
            //リストonClick処理
        }
    }

}
