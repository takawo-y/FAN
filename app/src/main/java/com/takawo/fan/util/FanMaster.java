package com.takawo.fan.util;

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
        list.add(new KeyValuePair(99, ""));
        list.add(new KeyValuePair(0, "生観戦"));
        list.add(new KeyValuePair(1, "TV生観戦"));
        list.add(new KeyValuePair(2, "TV録画観戦"));
        list.add(new KeyValuePair(3, "出場"));
        list.add(new KeyValuePair(4, "その他"));
        return list;
    }

}
