package com.example.takawo.fan.adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.takawo.fan.R;
import com.takawo.fan.db.FandbPlayer;

import java.util.ArrayList;

/**
 * Created by Takawo on 2014/12/31.
 */
public class PlayerAdaptor extends RecyclerView.Adapter<PlayerAdaptor.ViewHolder>{

    private LayoutInflater inf;
    private ArrayList<FandbPlayer> dataList;
    private Bitmap noImage;
    public void setNoImage(Bitmap noImage) {
        this.noImage = noImage;
    }

    public PlayerAdaptor(Context context, ArrayList<FandbPlayer> dataList){
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
        FandbPlayer data = dataList.get(i);
        String imgPath = data.getPlayerImagePath();
        if(imgPath == null || "".equals(imgPath)){
            viewHolder.playerImage.setImageBitmap(noImage);
        }else{
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bmp = BitmapFactory.decodeFile(data.getPlayerImagePath(), options);
            int height = options.outHeight;
            options.inSampleSize = height/600;
            options.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeFile(data.getPlayerImagePath(), options);
            viewHolder.playerImage.setImageBitmap(bmp);
        }
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
