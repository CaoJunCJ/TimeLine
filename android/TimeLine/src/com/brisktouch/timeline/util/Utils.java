/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brisktouch.timeline.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.StrictMode;
import com.brisktouch.timeline.add.Style1;
import com.brisktouch.timeline.ui.ImageGridActivity;


/*
API等级1：  Android 1.0
API等级2：  Android 1.1 Petit Four  花式小蛋糕
API等级3：  Android 1.5 Cupcake  纸杯蛋糕
API等级4：  Android 1.6 Donut  甜甜圈
API等级5：  Android 2.0 Éclair   松饼
API等级6：  Android 2.0.1 Éclair  松饼
API等级7：  Android 2.1 Éclair   松饼
API等级8：  Android 2.2 - 2.2.3 Froyo  冻酸奶
API等级9：  Android 2.3 - 2.3.2 Gingerbread  姜饼
API等级10：Android 2.3.3-2.3.7 Gingerbread   姜饼
API等级11：Android 3.0 Honeycomb 蜂巢
API等级12：Android 3.1 Honeycomb 蜂巢
API等级13：Android 3.2 Honeycomb 蜂巢
API等级14：Android 4.0 - 4.0.2 Ice Cream Sandwich  冰激凌三明治
API等级15：Android 4.0.3 - 4.0.4 Ice Cream Sandwich  冰激凌三明治
API等级16：Android 4.1 Jelly Bean  糖豆
API等级17：Android 4.2 Jelly Bean  糖豆
API等级18：Android 4.3 Jelly Bean  糖豆
API等级19：Android 4.4 KitKat 奇巧巧克力棒
API等级20 : Android 4.4W KitKat with wearable extensions 奇巧巧克力棒
API等级21：Android 5.0-5.0.2 Lollipop  棒棒糖

1.1  	[2]		Build.VERSION_CODES.BASE_1_1
        1.5  	[3]		Build.VERSION_CODES.CUPCAKE
        1.6  	[4]		Build.VERSION_CODES.DONUT
        2.0  	[5]		Build.VERSION_CODES.ECLAIR
        2.0.1  	[6]		Build.VERSION_CODES.ECLAIR_0_1
        2.1  	[7]		Build.VERSION_CODES.ECLAIR_MR1
        2.2  	[8]		Build.VERSION_CODES.FROYO
        2.3  	[9]		Build.VERSION_CODES.GINGERBREAD
        2.3.3  	[10]	Build.VERSION_CODES.GINGERBREAD_MR1
        3.0  	[11]	Build.VERSION_CODES.HONEYCOMB
        3.1  	[12]	Build.VERSION_CODES.HONEYCOMB_MR1
        3.2  	[13]	Build.VERSION_CODES.HONEYCOMB_MR2
        4.0  	[14]	Build.VERSION_CODES.ICE_CREAM_SANDWICH
        4.0.3  	[15]	Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
        4.1  	[16]	Build.VERSION_CODES.JELLY_BEAN
        4.2  	[17]	Build.VERSION_CODES.JELLY_BEAN_MR1
        4.3  	[18]	Build.VERSION_CODES.JELLY_BEAN_MR2

*/
/**
 * Class containing some static utility methods.
 */
public class Utils {
    private Utils() {};

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    @TargetApi(VERSION_CODES.HONEYCOMB)
    public static void enableStrictMode() {
        if (Utils.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            if (Utils.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
                vmPolicyBuilder
                        .setClassInstanceLimit(ImageGridActivity.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    public static boolean hasCUPCAKE(){ return Build.VERSION.SDK_INT >= VERSION_CODES.CUPCAKE;}

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
    }
}
