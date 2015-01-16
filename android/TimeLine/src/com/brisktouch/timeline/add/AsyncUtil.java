package com.brisktouch.timeline.add;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jim on 1/14/2015.
 */
public class AsyncUtil {
    private static AsyncUtil instance;
    private List<BitmapWorkerTask> list;
    int currentCounts = 0;
    int max = 50;

    public static AsyncUtil getInstance(){
        if(instance == null){
            synchronized (AsyncUtil.class) {
                if (instance == null) {
                    instance = new AsyncUtil();
                }
            }
        }
        return instance;
    }

    private AsyncUtil(){
        list = new ArrayList<BitmapWorkerTask>();

    }

    public BitmapWorkerTask getBitmapWorkerTask(ImageView imageView){
        if(currentCounts < max){
            BitmapWorkerTask bk = new BitmapWorkerTask(imageView);
            list.add(bk);
            currentCounts++;
            return bk;
        }

        for(int i=0; i< list.size(); i++){
            BitmapWorkerTask bk = list.get(i);
            if(!bk.isWork){
                bk.setImageView(imageView);
                return bk;
            }

            if(i == (list.size()-1)){
                try {
                    Thread.sleep(100);
                }catch (Exception e){e.printStackTrace();}
                i = 0;
            }
        }
        return null;
    }

}
