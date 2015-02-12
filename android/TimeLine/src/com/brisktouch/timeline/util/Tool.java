package com.brisktouch.timeline.util;

import android.content.Context;
import android.os.Environment;
import com.brisktouch.timeline.R;

import java.util.Date;
import java.util.Calendar;

/**
 * Created by jim on 12/18/2014.
 */
public class Tool {
    public static String getWeek(Context context,Date date){
        String[] weeks = {
                context.getResources().getString(R.string.Sunday),
                context.getResources().getString(R.string.Monday),
                context.getResources().getString(R.string.Tuesday),
                context.getResources().getString(R.string.Wednesday),
                context.getResources().getString(R.string.Thursday),
                context.getResources().getString(R.string.Friday),
                context.getResources().getString(R.string.Saturday),};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        return weeks[week_index];
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
