package com.example.takawo.fan.db.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.takawo.fan.R;

import java.util.ArrayList;

/**
 * Created by Takawo on 2014/12/31.
 */
public class PlayerAdaptor extends RecyclerView.Adapter<PlayerAdaptor.ViewHolder>{

    private LayoutInflater inf;
    private ArrayList<String> dataList;

    public PlayerAdaptor(Context context, ArrayList<String> dataList){
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
        String data = (String)dataList.get(i);
        viewHolder.text.setText(data);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(View v) {
            super(v);
            text = (TextView) v.findViewById(R.id.text);
        }
    }
}
