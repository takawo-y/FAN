package com.takawo.fan.util;

import com.takawo.fan.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 9004027600 on 2015/02/05.
 */
public class FanMaster {

    /**
     * 試合タイプ
     *
     * @return
     */
    static public List<KeyValuePair> getGameType(){
        LinkedList<KeyValuePair> list = new LinkedList<>();
        list.add(new KeyValuePair(0, "生観戦"));
        list.add(new KeyValuePair(1, "TV生観戦"));
        list.add(new KeyValuePair(2, "TV録画観戦"));
        list.add(new KeyValuePair(3, "出場"));
        list.add(new KeyValuePair(4, "その他"));
        return list;
    }

    /**
     * 試合タイプ(検索キー用)
     *
     * @return
     */
    static public List<KeyValuePair> getGameTypeForSearch(){
        LinkedList<KeyValuePair> list = new LinkedList<>();
        list.add(new KeyValuePair(99, "すべて"));
        list.add(new KeyValuePair(0, "現地観戦"));
        list.add(new KeyValuePair(1, "TV生観戦"));
        list.add(new KeyValuePair(2, "TV録画観戦"));
        list.add(new KeyValuePair(3, "ネット速報"));
        list.add(new KeyValuePair(4, "結果のみ"));
        list.add(new KeyValuePair(11, "出場"));
        list.add(new KeyValuePair(99, "その他"));
        return list;
    }

    static public List<KeyValuePair> getPlayerIcon(){
        LinkedList<KeyValuePair> list = new LinkedList<>();
        list.add(new KeyValuePair(0, "White"));  //白
        list.add(new KeyValuePair(1, "Grey"));  //グレイ
        return list;
    }

    static public List<KeyValuePair> getPlayerIconDelete(){
        LinkedList<KeyValuePair> list = new LinkedList<>();
        list.add(new KeyValuePair(0, Integer.toString(R.drawable.ic_delete_white_24dp)));  //白
        list.add(new KeyValuePair(1, Integer.toString(R.drawable.ic_delete_grey600_24dp)));  //グレイ
        return list;
    }

    static public List<KeyValuePair> getPlayerIconUpdate(){
        LinkedList<KeyValuePair> list = new LinkedList<>();
        list.add(new KeyValuePair(0, Integer.toString(R.drawable.ic_info_outline_white_24dp)));  //白
        list.add(new KeyValuePair(1, Integer.toString(R.drawable.ic_info_outline_grey600_24dp)));  //グレイ
        return list;
    }

    static public List<KeyValuePair> getPlayerIconDone(){
        LinkedList<KeyValuePair> list = new LinkedList<>();
        list.add(new KeyValuePair(0, Integer.toString(R.drawable.ic_done_white_24dp)));  //白
        list.add(new KeyValuePair(1, Integer.toString(R.drawable.ic_done_grey600_24dp)));  //グレイ
        return list;
    }

    static public List<KeyValuePair> getPlayerIconBack(){
        LinkedList<KeyValuePair> list = new LinkedList<>();
        list.add(new KeyValuePair(0, Integer.toString(R.drawable.ic_arrow_back_white_24dp)));  //白
        list.add(new KeyValuePair(1, Integer.toString(R.drawable.ic_arrow_back_grey600_24dp)));  //グレイ
        return list;
    }

}
