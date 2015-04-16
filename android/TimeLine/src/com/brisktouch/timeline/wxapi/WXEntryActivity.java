package com.brisktouch.timeline.wxapi;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.util.Tool;
import com.tencent.mm.sdk.openapi.*;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	private String TAG = "WXEntryActivity";

	private static final int THUMB_SIZE = 150;

	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

	private IWXAPI api;
	boolean isSendFriends = false;
	String text = "这段文字发送自微信SDK示例程序";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		api = WXAPIFactory.createWXAPI(WXEntryActivity.this, Constants.APP_ID, false);
		if(!api.isWXAppInstalled()){
			Toast.makeText(this, "没安装微信", Toast.LENGTH_LONG).show();
			finish();
		}
		api.registerApp(Constants.APP_ID);
		api.handleIntent(getIntent(), this);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setBackgroundResource(R.drawable.share_background);
		setContentView(linearLayout);
		String _message = getIntent().getStringExtra("TYPE");
		if(_message!=null && _message.equals("WEIXIN")){
			String isFriends = getIntent().getStringExtra("FRIENDS");
			if(isFriends!=null && "YES".equals(isFriends)){
				isSendFriends = true;
				int wxSdkVersion = api.getWXAppSupportAPI();
				if (!(wxSdkVersion >= TIMELINE_SUPPORTED_VERSION)) {
					Toast.makeText(this, "不支持分享到朋友圈", Toast.LENGTH_LONG).show();
					finish();
				}
			}
			sendImage(getIntent().getStringExtra("IMAGE_PATH"));
			//sendMessage();
		}
	}

	public void sendImage(String path){
		Bitmap bmp = BitmapFactory.decodeFile(path);
		WXImageObject imgObj = new WXImageObject(bmp);

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;

		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = Tool.customBmpToByteArray(thumbBmp, true);  // 设置缩略图

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = isSendFriends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);

		finish();
	}

	public void sendMessage(){
		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = text;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		req.message = msg;

		req.scene = isSendFriends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

		// 调用api接口发送数据到微信
		api.sendReq(req);
		finish();
	}


	@Override
	public void onReq(BaseReq req){
		switch (req.getType()) {
			case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
				Log.d(TAG, "onReq : COMMAND_GETMESSAGE_FROM_WX");
				//goToGetMsg();
				break;
			case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
				//goToShowMsg((ShowMessageFromWX.Req) req);
				Log.d(TAG, "onReq : COMMAND_SHOWMESSAGE_FROM_WX");
				break;
			default:
				break;
		}
		Log.d(TAG, "onReq : onReq");
	}

	@Override
	public void onResp(BaseResp resp){
		String result = "0";

		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = getResources().getString(R.string.errcode_success);
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = getResources().getString(R.string.errcode_cancel);
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = getResources().getString(R.string.errcode_deny);
				break;
			default:
				result = getResources().getString(R.string.errcode_unknown);
				break;
		}
		Log.d(TAG, "onResp : " + result);
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
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

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			finish();
		}
	};
}