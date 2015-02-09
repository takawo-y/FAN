package com.takawo.fan.util;

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

    static public Integer getCurrentHour(){
        Calendar caledar = Calendar.getInstance();
        int hour = caledar.get(Calendar.HOUR_OF_DAY);
        return new Integer(hour);
    }

    static public Integer getCurrentMinite(){
        Calendar caledar = Calendar.getInstance();
        int minite = caledar.get(Calendar.MINUTE);
        return new Integer(minite);
    }
}
