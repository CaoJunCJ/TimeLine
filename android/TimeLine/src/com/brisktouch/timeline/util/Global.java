package com.brisktouch.timeline.util;

import org.json.JSONObject;

/**
 * Created by jim on 2/13/2015.
 */
public class Global {
    private static JSONObject jsonData = new JSONObject();
    public static JSONObject getJsonData(){
        return jsonData;
    }

}
