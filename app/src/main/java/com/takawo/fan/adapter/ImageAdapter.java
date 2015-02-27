package com.takawo.fan.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbImage;
import com.takawo.fan.util.BitmapTransformation;

import java.io.File;
import java.util.List;

/**
 * Created by Takawo on 2014/12/31.
 */
public class ImageAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;
    private List<FandbImage> dataList;

    private static class ViewHolder{
        public TextView imageTitle;
        public ImageView image;
    }

    public ImageAdapter(Context context, List<FandbImage> list){
        this.context = context;
        this.dataList = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_image, null);
            holder = new ViewHolder();
            holder.imageTitle = (TextView)convertView.findViewById(R.id.image_title);
            holder.image = (ImageView)convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        FandbImage data = dataList.get(position);
        holder.imageTitle.setText(data.getTitle());
        Picasso.with(context).load(new File(data.getPath()))
                .transform(new BitmapTransformation())
                .resize(300, 300)
                .centerInside()
                .into(holder.image);
        return convertView;
    }
}
