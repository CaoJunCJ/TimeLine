package com.brisktouch.timeline.wxapi;


import android.graphics.Color;
import android.util.Log;
import android.widget.LinearLayout;
import com.brisktouch.timeline.R;
import com.tencent.mm.sdk.openapi.*;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	private String TAG = "WXEntryActivity";

	private IWXAPI api;
	boolean isSendFriends = false;
	String text = "这段文字发送自微信SDK示例程序";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(WXEntryActivity.this, Constants.APP_ID, false);
		api.handleIntent(getIntent(), this);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setBackgroundColor(Color.WHITE);
		setContentView(linearLayout);
		String _message = getIntent().getStringExtra("TYPE");
		if(_message!=null && _message.equals("WEIXIN")){
			sendMessage();
		}
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
		finish();
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
}