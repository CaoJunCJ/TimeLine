/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
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

package com.brisktouch.timeline.wbapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.brisktouch.timeline.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.*;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.LogUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 该类是分享功能的入口。
 * 
 * @author SINA
 * @since 2013-09-29
 */
public class WBShareMainActivity extends Activity implements IWeiboHandler.Response{
    String TAG = "WBShareMainActivity";

    /** 微博分享的接口实例 */
    private IWeiboShareAPI mWeiboShareAPI;
    
    String text = "这段文字发送自新浪微博SDK示例程序";

    /**
     * @see {@link android.app.Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Log.d(TAG, "WBShareMainActivity call");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundResource(R.drawable.share_background);
        setContentView(linearLayout);

        // 创建微博 SDK 接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);
        mWeiboShareAPI.registerApp();

        LogUtil.enableLog();
        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        //if (savedInstanceState != null) {

        //}
        mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        String _message = getIntent().getStringExtra("TYPE");
        if(_message!=null && _message.equals("WEIBO")){
            sendImage(getIntent().getStringExtra("IMAGE_PATH"));
            //sendMessage();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
    }

    /**
     * 初始化 UI 和微博接口实例 。
     */
    private void sendMessage() {

        // 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
        final boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
        final int supportApiLevel = mWeiboShareAPI.getWeiboAppSupportAPI();
        if(isInstalledWeibo){
            // 2. 初始化从第三方到微博的消息请求
            if (supportApiLevel >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/){
                WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                weiboMessage.textObject = getTextObj();
                SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
                request.transaction = String.valueOf(System.currentTimeMillis());
                request.multiMessage = weiboMessage;

                mWeiboShareAPI.sendRequest(WBShareMainActivity.this, request);
            }else{
                WeiboMessage weiboMessage = new WeiboMessage();
                weiboMessage.mediaObject = getTextObj();
                SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
                // 用transaction唯一标识一个请求
                request.transaction = String.valueOf(System.currentTimeMillis());
                request.message = weiboMessage;

                mWeiboShareAPI.sendRequest(WBShareMainActivity.this, request);
            }
        }else{
            Toast.makeText(WBShareMainActivity.this, "没安装新浪微博", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void sendImage(String bitmapPath){
        // 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
        final boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
        final int supportApiLevel = mWeiboShareAPI.getWeiboAppSupportAPI();
        if(isInstalledWeibo){
            // 2. 初始化从第三方到微博的消息请求
            if (supportApiLevel >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/){
                WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                //weiboMessage.textObject = getTextObj();
                weiboMessage.imageObject = getImageObj(bitmapPath);
                SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
                request.transaction = String.valueOf(System.currentTimeMillis());
                request.multiMessage = weiboMessage;

                mWeiboShareAPI.sendRequest(WBShareMainActivity.this, request);
            }else{
                WeiboMessage weiboMessage = new WeiboMessage();
                weiboMessage.mediaObject = getImageObj(bitmapPath);
                SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
                // 用transaction唯一标识一个请求
                request.transaction = String.valueOf(System.currentTimeMillis());
                request.message = weiboMessage;

                mWeiboShareAPI.sendRequest(WBShareMainActivity.this, request);
            }
        }else{
            Toast.makeText(WBShareMainActivity.this, "没安装新浪微博", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    public void onResponse(BaseResponse baseResp){
        String result = "0";
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                result = "ERR_OK";
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                result = "ERR_CANCEL";
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                result = "ERR_FAIL Error Message:" + baseResp.errMsg;
                break;
        }
        Log.d(TAG, result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2500);
        //finish();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finish();
        }
    };
}
