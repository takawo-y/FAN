package com.takawo.fan.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.takawo.fan.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

    /**
     * ResultType取得
     * 0:スコア、1:タイム
     * @return
     */
    static public long getResultType(int resultTypeId){
        long resultType = 0;
        if (resultTypeId == R.id.playerResultTypeRB0){
            resultType = 0;
        }else if (resultTypeId == R.id.playerResultTypeRB1){
            resultType = 1;
        }
        return resultType;
    }

    /**
     * ゲームタイプのラベル取得
     * @param type
     * @return
     */
    static public String getGameTypeLabel(Long type){
        List<KeyValuePair> masterList = FanMaster.getGameType();
        for(Iterator ite = masterList.iterator(); ite.hasNext();){
            KeyValuePair data = (KeyValuePair)ite.next();
            if(data.getKey() == type.intValue()){
                return data.getValue();
            }
        }
        return "";
    }

    /**
     * 削除アイコン取得
     *
     * @param colorType
     * @return
     */
    static public int getPlayerIconDelete(int colorType){
        List<KeyValuePair> list = FanMaster.getPlayerIconDelete();
        for(KeyValuePair data: list){
            if(colorType == data.getKey()){
                return new Integer(data.getValue());
            }
        }
        return new Integer(R.drawable.ic_delete_white_24dp);
    }

    /**
     * 更新アイコン取得
     *
     * @param colorType
     * @return
     */
    static public int getPlayerIconUpdate(int colorType){
        List<KeyValuePair> list = FanMaster.getPlayerIconUpdate();
        for(KeyValuePair data: list){
            if(colorType == data.getKey()){
                return new Integer(data.getValue());
            }
        }
        return new Integer(R.drawable.ic_info_outline_white_24dp);
    }

    /**
     * 実行アイコン取得
     * @param colorType
     * @return
     */
    static public int getPlayerIconDone(int colorType){
        List<KeyValuePair> list = FanMaster.getPlayerIconDone();
        for(KeyValuePair data: list){
            if(colorType == data.getKey()){
                return new Integer(data.getValue());
            }
        }
        return new Integer(R.drawable.ic_done_white_24dp);
    }

    /**
     * 戻るアイコン取得
     * @param colorType
     * @return
     */
    static public int getPlayerIconBack(int colorType){
        List<KeyValuePair> list = FanMaster.getPlayerIconBack();
        for(KeyValuePair data: list){
            if(colorType == data.getKey()){
                return new Integer(data.getValue());
            }
        }
        return new Integer(R.drawable.ic_arrow_back_white_24dp);
    }

}
