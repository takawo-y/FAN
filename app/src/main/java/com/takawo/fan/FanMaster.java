package com.takawo.fan;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
}
