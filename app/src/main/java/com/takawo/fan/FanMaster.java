package com.takawo.fan;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 9004027600 on 2015/02/05.
 */
public class FanMaster {

    public Map<Integer, String> getGameType(){
        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(0, "生観戦");
        map.put(1, "TV生観戦");
        map.put(2, "TV録画観戦");
        map.put(3, "出場");
        map.put(4, "その他");
        return map;
    }
}
