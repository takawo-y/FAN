package com.takawo.fan.adapter;

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
import com.takawo.fan.R;
import com.takawo.fan.activity.GameActivity;
import com.takawo.fan.db.FandbImage;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.util.BitmapTransformation;
import com.takawo.fan.util.FanConst;

import java.io.File;
import java.util.List;

/**
 * Created by Takawo on 2014/12/31.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private RecyclerView recyclerView;
    private LayoutInflater inf;
    private List<FandbImage> dataList;
    private Context context;

    public ImageAdapter(Context context, List<FandbImage> dataList){
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
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inf.inflate(R.layout.list_image, null);
        ViewHolder viewHolder = new ViewHolder(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setItem(dataList.get(i));
//        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Click時、試合一覧に遷移する
//                Intent intent = new Intent(context, GameActivity.class);
//                FandbPlayer data = dataList.get(recyclerView.getChildPosition(v));
//                intent.putExtra(FanConst.INTENT_PLAYER_ID, data.getId());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        Context context;
        LinearLayout linearLayout;

        Long gameId;
        Long id;
        ImageView image;
        TextView title;
        TextView comment;

        public ViewHolder(Context context, View v) {
            super(v);
            this.context = context;
            linearLayout = (LinearLayout)v.findViewById(R.id.lily_player);

            v.setOnClickListener(this);
            image = (ImageView)v.findViewById(R.id.image);
            title = (TextView)v.findViewById(R.id.image_title);
            comment = (TextView)v.findViewById(R.id.image_comment);
        }

        public void setItem(FandbImage data){
            gameId = data.getId();
            id = data.getId();
            Picasso.with(context)
                    .load(new File(data.getPath()))
                    .transform(new BitmapTransformation()).resize(250, 250)
                    .into(image);
            title.setText(data.getTitle());
            comment.setText(data.getComment());
        }

        @Override
        public void onClick(View view){
            //リストonClick処理
        }
    }

}
