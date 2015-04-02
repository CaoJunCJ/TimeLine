package com.brisktouch.timeline.util;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by jim on 4/2/2015.
 */
public class CustomJSONArray extends JSONArray {
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
}
