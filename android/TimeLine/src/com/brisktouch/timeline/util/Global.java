package com.brisktouch.timeline.util;

import org.json.JSONObject;

/**
 * Created by jim on 2/13/2015.
 */
public class Global {
    /*
    {
        "data":[
            {
                "date":"2012.12.5",
                "things":[
                    {
                    "title":"title",
                    "time":"12:11:45"
                    "style": "SceneryStyleActivity",
                    "strings": [
                        "寻访 • 美景",
                        "anny",
                        "Lolita, light of my life, fire of my loins. My sin, my soul. Lo-lee-ta: the tip of the tongue taking a trip of three steps down the palate to tap, at three, on the teeth.",
                        "Jane"
                        ],
                    }
                ]
            }
        ]
    }
     */

    public static final String JSON_KEY_DATA = "data";
    public static final String JSON_KEY_DATE = "date";
    public static final String JSON_KEY_THINGS = "things";
    public static final String JSON_KEY_TITLE = "title";
    public static final String JSON_KEY_TIME = "time";
    public static final String JSON_KEY_STYLE = "style";
    public static final String JSON_KEY_STRINGS = "strings";

    private static JSONObject jsonData = new JSONObject();
    public static JSONObject getJsonData(){
        return jsonData;
    }
    public static void setJsonData(JSONObject j){
        jsonData = j;
    }
}
