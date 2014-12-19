package com.brisktouch.timeline.util;

import java.util.Date;
import java.util.Calendar;

/**
 * Created by jim on 12/18/2014.
 */
public class Tool {
    public static String getWeek(Date date){
        String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        return weeks[week_index];
    }

}
