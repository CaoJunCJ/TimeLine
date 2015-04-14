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
	Button sendMessage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(WXEntryActivity.this, Constants.APP_ID, false);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setBackgroundColor(Color.WHITE);
		sendMessage = new Button(this);
		sendMessage.setText("send message");
		linearLayout.addView(sendMessage);
		setContentView(linearLayout);

		sendMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

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

				req.scene = false ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

				// 调用api接口发送数据到微信
				api.sendReq(req);
				finish();
			}
		});

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
	}

	@Override
	public void onResp(BaseResp resp){
		int result = 0;

		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = R.string.errcode_success;
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = R.string.errcode_cancel;
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = R.string.errcode_deny;
				break;
			default:
				result = R.string.errcode_unknown;
				break;
		}

		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
}