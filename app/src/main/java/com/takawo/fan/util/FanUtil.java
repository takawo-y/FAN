package com.takawo.fan.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Takawo on 2015/02/08.
 */
public class FanUtil {

    static public Date getDate(DatePicker date){
        if(date == null) return null;
        String day = date.getYear()+"/"+(date.getMonth()+1)+"/"+date.getDayOfMonth();
        return new Date(day);
    }

    static public String getTimeString(TimePicker time){
        if(time == null) return null;
        return time.getCurrentHour()+":"+time.getCurrentMinute();
    }

    static public Bitmap resizeImage(String path, int size){
        Drawable img = Drawable.createFromPath(path);
        Bitmap imgBitmap = ((BitmapDrawable)img).getBitmap();
        return Bitmap.createScaledBitmap(imgBitmap, size, size, false);
    }
}
