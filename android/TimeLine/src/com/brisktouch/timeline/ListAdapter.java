package com.brisktouch.timeline;

import android.widget.*;
import com.brisktouch.timeline.util.Global;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

public class ListAdapter extends BaseAdapter {
	private JSONArray data;
	private Context context;
	private HashMap<Integer, TimeLineDisplayView> map;
	String TAG = "ListAdapter";
	@Override
	public int getCount() {
		return data.length();
	}

	@Override
	public Object getItem(int index) {
		return data.opt(index);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup arg2) {
		TimeLineDisplayView tv;
		JSONObject date = data.optJSONObject(index);
		if(null == convertView){
			tv = new TimeLineDisplayView(context, date);

		}else{
			tv = (TimeLineDisplayView)convertView;
		}
		tv.setData(date);
		//tv.setBackgroundColor(Color.WHITE);
		tv.setBackgroundColor(Color.parseColor("#F9F9F9"));
		tv.setHeight(tv.getCurrentLength());
		return tv;
	}
	
	public ListAdapter(JSONObject json,Context c){
		data = json.optJSONArray(Global.JSON_KEY_DATA);
		this.context = c;
	}
}
