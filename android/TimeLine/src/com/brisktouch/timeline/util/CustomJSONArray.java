package com.brisktouch.timeline.util;

import org.cjson.JSONArray;
import org.cjson.JSONException;

import java.io.Serializable;

/**
 * Created by jim on 4/2/2015.
 */
public class CustomJSONArray extends JSONArray implements Serializable{
    @Override
    public Object remove(int index) {
        JSONArray output = new JSONArray();
        int len = this.length();
        for (int i = 0; i < len; i++)   {
            if (i != index) {
                try {
                    output.put(this.get(i));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return output;
        //return this; If you need the input array in case of a failed attempt to remove an item.
    }

    public void clear(){

    }
}
