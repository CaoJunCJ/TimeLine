package com.brisktouch.timeline;

import android.widget.*;
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
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		Log.i(TAG, "getView");
		JSONObject date = data.optJSONObject(arg0);
		TimeLineDisplayView tv = new TimeLineDisplayView(context, date);
		tv.setBackgroundColor(Color.WHITE);
		tv.setHeight(tv.getCurrentLength());
		return tv;
	}
	
	public ListAdapter(JSONObject json,Context c){
		data = json.optJSONArray("data");
		System.out.println(data.length());
		this.context = c;
	}
}
