package com.takawo.fan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.takawo.fan.MyApplication;
import com.takawo.fan.activity.GameUpdateActivity;
import com.takawo.fan.db.DaoSession;
import com.takawo.fan.db.FandbImage;
import com.takawo.fan.db.FandbImageDao;
import com.takawo.fan.db.data.DBHelper;
import com.takawo.fan.util.BitmapTransformation;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbGame;
import com.takawo.fan.util.FanUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by Takawo on 2014/12/31.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder>{

    private RecyclerView recyclerView;
    private LayoutInflater inf;
    private List<FandbGame> dataList;
    private int playerColor;
    private int playerFontColor;
    private Context context;
    private Map<Long, Integer> imageCountMap;

    public GameAdapter(Context context, List<FandbGame> dataList, int playerColor, int playerFontColor,
                       Map<Long, Integer> imageCountMap){
        super();
        this.context = context;
        inf = LayoutInflater.from(context);
        this.dataList = dataList;
        this.playerColor = playerColor;
        this.playerFontColor = playerFontColor;
        this.imageCountMap = imageCountMap;
    }

    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inf.inflate(R.layout.list_game, null);
        ViewHolder viewHolder = new ViewHolder(context, view, imageCountMap);
        return viewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView= recyclerView;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setItem(dataList.get(i), playerColor, playerFontColor);
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FandbGame data = dataList.get(recyclerView.getChildPosition(v));
                Intent intent = new Intent(context, GameUpdateActivity.class);
                intent.putExtra(FanConst.INTENT_PLAYER_ID, data.getPlayerId());
                intent.putExtra(FanConst.INTENT_GAME_ID, data.getId());
                context.startActivity(intent);
            }
        });
        viewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                final FandbGame data = dataList.get(recyclerView.getChildPosition(v));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("試合情報削除")
                        .setMessage("試合情報を削除しますか？\r\n紐づく画像情報も削除されます。")
                        .setPositiveButton("はい",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which){
                                        FandbImage imageEntity = new FandbImage();
                                        imageEntity.setPlayerId(data.getPlayerId());
                                        deleteImage(data.getId());
                                        (new DBHelper(context, null)).getDaoSession().getFandbGameDao().deleteByKey(data.getId());
                                        Intent intent = new Intent(context, context.getClass());
                                        intent.putExtra(FanConst.INTENT_PLAYER_ID, data.getPlayerId());
                                        intent.putExtra(FanConst.INTENT_GAME_ID, data.getId());
                                        context.startActivity(intent);
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

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener, View.OnLongClickListener{
        Context context;
        CardView linearLayout;
        Map<Long, Integer> imagecountMap;

        Long playerId;
        Long id;
        TextView cardGameTitle;
        ImageView oppositionImage;
        TextView opposition;
        TextView result;
        TextView resultScoreTime;
        ImageView imageCount;

        public ViewHolder(Context context, View v, Map<Long, Integer> imageCountMap) {
            super(v);
            this.context = context;
            this.imagecountMap = imageCountMap;
            linearLayout = (CardView)v.findViewById(R.id.lily_game);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            cardGameTitle = ButterKnife.findById(v, R.id.cardGameTitle);
            opposition = ButterKnife.findById(v, R.id.gameOpposition);
            oppositionImage = ButterKnife.findById(v, R.id.oppositionImg);
            result = ButterKnife.findById(v, R.id.gameResult);
            resultScoreTime = ButterKnife.findById(v, R.id.gameScoreTime);
            imageCount = ButterKnife.findById(v, R.id.imageCount);
        }

        public void setItem(FandbGame data, int playeColor, int fontColor){
            playerId = data.getPlayerId();
            id = data.getId();
            SimpleDateFormat formatA = new SimpleDateFormat("yyyy/MM/dd(E)", Locale.JAPAN);
            String formatDate = formatA.format(data.getGameDay());
            cardGameTitle.setText(formatDate + " " + data.getGameCategory() + " " + data.getGameInfo()
                    + " (" + FanUtil.getGameTypeLabel(data.getGameType()) + ")");
            cardGameTitle.setBackgroundColor(playeColor);
            cardGameTitle.setTextColor(fontColor);
            if(null == data.getOppositionImagePath() || "".equals(data.getOppositionImagePath())){
                Picasso.with(context)
                        .load(R.drawable.no_image)
                        .transform(new BitmapTransformation())
                        .into(oppositionImage);
            }else{
                Picasso.with(context)
                        .load(new File(data.getOppositionImagePath()))
                        .transform(new BitmapTransformation())
                        .resize(50, 50)
                        .centerInside()
                        .into(oppositionImage);
            }
            if(imagecountMap.get(data.getId()) == null){
                imageCount.setImageDrawable(null);
            }else{
                Picasso.with(context).load(Integer.parseInt(FanUtil.getImageCount(imagecountMap.get(data.getId())))).into(imageCount);
            }
            opposition.setText("vs "+data.getOpposition());
            result.setText(data.getResult());
            if(data.getResultScore() != null || TextUtils.isEmpty(data.getResultScore()) == false){
                resultScoreTime.setText(data.getResultScore());
            }else if (data.getResultTime() != null || TextUtils.isEmpty(data.getResultTime()) == false){
                resultScoreTime.setText(data.getResultTime());
            }

        }

        @Override
        public void onClick(View view){
            //リストonClick処理
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    private void deleteImage(Long gameId){
        List<FandbImage> list = (new DBHelper(context, null)).getDaoSession().getFandbImageDao().
                queryBuilder().where(FandbImageDao.Properties.GameId.eq(gameId)).list();

        for(FandbImage data: list){
            (new DBHelper(context, null)).getDaoSession().getFandbImageDao().deleteByKey(data.getId());
        }
    }
}
