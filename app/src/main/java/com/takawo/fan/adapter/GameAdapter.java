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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.takawo.fan.activity.GameUpdateActivity;
import com.takawo.fan.db.DaoSession;
import com.takawo.fan.db.data.DBHelper;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbGame;
import com.takawo.fan.util.FanUtil;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Takawo on 2014/12/31.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder>{

    private RecyclerView recyclerView;
    private LayoutInflater inf;
    private List<FandbGame> dataList;
    private Context context;

    public GameAdapter(Context context, List<FandbGame> dataList){
        super();
        this.context = context;
        inf = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inf.inflate(R.layout.list_game, null);
        ViewHolder viewHolder = new ViewHolder(context, view);
        return viewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView= recyclerView;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setItem(dataList.get(i));
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
                        .setMessage("試合情報を削除しますか？")
                        .setPositiveButton("はい",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which){
                                        Log.d("Delete Game", "ID:"+data.getId());
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

        Long playerId;
        Long id;
        TextView cardGameTitle;
        TextView opposition;
        TextView result;
        TextView resultScoreTime;

        public ViewHolder(Context context, View v) {
            super(v);
            this.context = context;
            linearLayout = (CardView)v.findViewById(R.id.lily_game);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            cardGameTitle = ButterKnife.findById(v, R.id.cardGameTitle);
            opposition = ButterKnife.findById(v, R.id.gameOpposition);
            result = ButterKnife.findById(v, R.id.gameResult);
            resultScoreTime = ButterKnife.findById(v, R.id.gameScoreTime);
        }

        public void setItem(FandbGame data){
            playerId = data.getPlayerId();
            id = data.getId();
            SimpleDateFormat formatA = new SimpleDateFormat("yyyy/MM/dd");
            String formatDate = formatA.format(data.getGameDay());
            cardGameTitle.setText(formatDate+" "+data.getGameCategory()+" "+data.getGameInfo()
                    +" ("+FanUtil.getGameTypeLabel(data.getGameType())+")");
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

}
