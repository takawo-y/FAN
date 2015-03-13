package com.takawo.fan.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.takawo.fan.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
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
     * 日付変換
     *
     * @param date
     * @param format yyyy/MM/ddなど
     * @return
     */
    static public String getDateString(Date date, String format){
        SimpleDateFormat formatA = new SimpleDateFormat(format);
        return formatA.format(date);
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

    static public String filecopy(String file_src,String file_dist) {
        String err = "";
        FileInputStream fis;
        FileOutputStream fos;

        File fi = new File(file_src);
        File fo = new File(file_dist);
        try {
            fis=new FileInputStream(fi);
            FileChannel chi = fis.getChannel();

            fos=new FileOutputStream(fo);
            FileChannel cho = fos.getChannel();

            chi.transferTo(0, chi.size(), cho);
            chi.close();
            cho.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            err = "FileNotFoundException " + e.getMessage();
        }catch (IOException e) {
            e.printStackTrace();
            err = "FileNotFoundException " + e.getMessage();
        }
        return err;
    }

    static public String getImageCount(int count){
        switch (count){
            case 0:
                return null;
            case 1:
                return Integer.toString(R.drawable.ic_filter_1_grey600_18dp);
            case 2:
                return Integer.toString(R.drawable.ic_filter_2_grey600_18dp);
            case 3:
                return Integer.toString(R.drawable.ic_filter_3_grey600_18dp);
            case 4:
                return Integer.toString(R.drawable.ic_filter_4_grey600_18dp);
            case 5:
                return Integer.toString(R.drawable.ic_filter_5_grey600_18dp);
            case 6:
                return Integer.toString(R.drawable.ic_filter_6_grey600_18dp);
            case 7:
                return Integer.toString(R.drawable.ic_filter_7_grey600_18dp);
            case 8:
                return Integer.toString(R.drawable.ic_filter_8_grey600_18dp);
            case 9:
                return Integer.toString(R.drawable.ic_filter_9_grey600_18dp);
            default:
                return Integer.toString(R.drawable.ic_filter_9_plus_grey600_18dp);
        }
    }

    static public GameSearchKey setGameSearchKey(String gameYear, String gameYearMonth, String category, int type){
        GameSearchKey key = new GameSearchKey();
        key.setIsSet(true);
        if("".equals(gameYear)){
            key.setGameYear(null);
        }else{
            key.setGameYear(gameYear);
        }
        if("".equals(gameYearMonth)){
            key.setGameYearMonth(null);
        }else{
            key.setGameYearMonth(gameYearMonth);
        }
        if("".equals(category)){
            key.setCategory(null);
        }else{
            key.setCategory(category);
        }
        key.setType(type);
        return key;
    }
}
