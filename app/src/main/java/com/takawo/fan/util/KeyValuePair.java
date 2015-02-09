package com.takawo.fan.util;


import android.util.Pair;

/**
 * Created by 9004027600 on 2015/02/05.
 */
public class KeyValuePair extends Pair<Integer ,String> {

    public KeyValuePair(Integer key, String value){
        super(key, value);
    }

    public Integer getKey(){
        return super.first;
    }

    public String getValue(){
        return super.second;
    }
}
