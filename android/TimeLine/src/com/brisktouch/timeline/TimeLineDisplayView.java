package com.brisktouch.timeline;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import com.brisktouch.timeline.style.*;
import com.brisktouch.timeline.util.CustomJSONArray;
import com.brisktouch.timeline.util.FileUtil;
import com.brisktouch.timeline.util.Global;
import com.brisktouch.timeline.util.Tool;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.widget.TextView;
import android.view.Window;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.HashMap;

class TimeLineDisplayView extends TextView {
	public static String TAG = "TestView";
	public static int DATELINE_LENGTH = 70;
	public static int DATELINE_RADIUS = 30;
	public static int TIMELINE_RADIUS = 10;
	public static int MARGIN_RIGHT = 100;
	public static int DISPLAY_DATE_STRING_X = 150;
	public static int THING_X = 240;
	public static int THING_Y = 70;
	public static int TIME_THING_INTERVAL = 30;

	public static int SMALL_WORD_SIZE = 18;
	public static int BIG_WORD_SIZE = 24;

	public static boolean isInitData = false;

	int screenWidth = 0;
	int screenHeight = 0;

	private Paint p;
	private JSONObject json;
	private int currentLength = 0;
	private int height = 0;

	private HashMap<String, RectF> rectMap;

	private int topHeight;

	private String needReDrawByTime = null;
	private int DeleteAnimationTIME_THING_INTERVAL = TIME_THING_INTERVAL;

	public TimeLineDisplayView(Context context, JSONObject json) {
		super(context);
		initData();
		p = new Paint();
		this.json = json;
		height += 20;
		height += DATELINE_RADIUS*2;
		JSONArray array = json.optJSONArray(Global.JSON_KEY_THINGS);
		for(int i=0; i < array.length(); i++){
			height += DATELINE_LENGTH;
			height += TIMELINE_RADIUS*2;
		}
		height += DATELINE_LENGTH;
		rectMap = new HashMap<String, RectF>();

		//hide status bar , so statusHeight = 0;
		//int statusHeight = getStatusBarHeight();
		int statusHeight = 0;
		int titleHeight = getTitleBarHeight(statusHeight);
		topHeight = statusHeight + titleHeight;
		Log.i(TAG, "topHeight :" + topHeight);

	}

	public void setData(JSONObject json){
		this.json = json;
	}

	public void initData(){
		if(!isInitData) {
			Activity parent = (Activity) getContext();
			DisplayMetrics dms = parent.getResources().getDisplayMetrics();
			screenWidth = dms.widthPixels;
			screenHeight = dms.heightPixels;
			DATELINE_LENGTH = screenHeight / 11;
			DATELINE_RADIUS = screenWidth / 16;
			TIMELINE_RADIUS = screenWidth / 48;
			MARGIN_RIGHT = (int) (screenWidth / 4.8);
			DISPLAY_DATE_STRING_X = screenWidth / 3;

			THING_X = screenWidth / 2;
			THING_X = screenWidth * 6 / 10;

			THING_Y = screenHeight / 11;

			//for debug
			//THING_Y = 100;
			//DATELINE_LENGTH = 100;

			TIME_THING_INTERVAL = screenHeight / 26;

			DeleteAnimationTIME_THING_INTERVAL = TIME_THING_INTERVAL;

			BIG_WORD_SIZE = screenWidth/20;
			SMALL_WORD_SIZE = screenWidth/26;
			Log.i(TAG, "screenWidth :" + screenWidth);
			Log.i(TAG, "screenHeight :" + screenHeight);
			Log.i(TAG, "DATELINE_LENGTH :" + DATELINE_LENGTH);
			Log.i(TAG, "DATELINE_RADIUS :" + DATELINE_RADIUS);
			Log.i(TAG, "TIMELINE_RADIUS :" + TIMELINE_RADIUS);
			Log.i(TAG, "MARGIN_RIGHT :" + MARGIN_RIGHT);
			Log.i(TAG, "DISPLAY_DATE_STRING_X :" + DISPLAY_DATE_STRING_X);
			Log.i(TAG, "THING_X :" + THING_X);
			Log.i(TAG, "THING_Y :" + THING_Y);
			Log.i(TAG, "TIME_THING_INTERVAL :" + TIME_THING_INTERVAL);
			Log.i(TAG, "BIG_WORD_SIZE :" + BIG_WORD_SIZE);
			Log.i(TAG, "SMALL_WORD_SIZE :" + SMALL_WORD_SIZE);
			isInitData = true;
		}

	}
	
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		currentLength = 0;
		String date = json.optString(Global.JSON_KEY_DATE);
		JSONArray array = json.optJSONArray(Global.JSON_KEY_THINGS);
		drawLine(MARGIN_RIGHT, currentLength, 20, canvas);
		drawDate(MARGIN_RIGHT, currentLength, date, canvas);
		for(int i=0; i < array.length(); i++){
			JSONObject timeThing = array.optJSONObject(i);
			String time = timeThing.optString(Global.JSON_KEY_TIME);
			String title = timeThing.optString(Global.JSON_KEY_TITLE);
			//Log.i(TAG, "time is " + time);
			currentLength += 2;
			drawLine(MARGIN_RIGHT, currentLength, DATELINE_LENGTH -4, canvas);
			currentLength += 2;
			drawTime(MARGIN_RIGHT, currentLength, canvas);

			if(needReDrawByTime!=null && needReDrawByTime.equals(time)){
				drawThingByDeleteAnimation(MARGIN_RIGHT, currentLength - TIMELINE_RADIUS, title, time, canvas);
			}else{
				drawThing(MARGIN_RIGHT, currentLength - TIMELINE_RADIUS, title, time, canvas);
			}

		}
		currentLength += 2;
		drawLine(MARGIN_RIGHT, currentLength, DATELINE_LENGTH -2, canvas);

		Log.i(TAG, "onDraw call");

		//this.setBackgroundColor(Color.WHITE);
		//this.setHeight(currentLength);
	}

	public int getCurrentLength(){

		return height;
	}
	
	
	private void drawDate(int x, int y ,String date, Canvas canvas){
		p.setStyle(Style.STROKE);
		p.setAntiAlias(true);
		p.setColor(Color.parseColor("#D3D3D3"));
		p.setStrokeWidth(3);
		canvas.drawCircle(x, y + DATELINE_RADIUS, DATELINE_RADIUS, p);
		p.reset();
		String[] dates = date.split("\\.");
		if(dates.length >= 3){
			String year = dates[0];
			String month = dates[1];
			String day = dates[2];
			DateFormat datef = new SimpleDateFormat("yyyy.MM.dd");
			String week = null;
			try{
				Date d1 = datef.parse(date);
				week = Tool.getWeek(this.getContext(), d1);
			}catch (Exception e){}
			p.setAntiAlias(true);
			p.setColor(Color.BLACK);
			p.setTextSize(BIG_WORD_SIZE);
			FontMetricsInt fontMetrics = p.getFontMetricsInt();
			int baseline = y + (y+DATELINE_RADIUS*2 - y - fontMetrics.bottom + fontMetrics.top)/2 - fontMetrics.top;
			p.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(day , x, baseline , p); 
			p.setColor(Color.GRAY);

			String displayDate = year + getResources().getString(R.string.YEAR) + month + getResources().getString(R.string.MONTH) + " " + week;
			canvas.drawText(displayDate , x + DISPLAY_DATE_STRING_X, baseline , p);
		}
		currentLength += DATELINE_RADIUS*2;
	}
	
	private void drawTime(int x, int y ,Canvas canvas){
		p.setColor(Color.parseColor("#ADD8E6"));
		p.setAntiAlias(true);
		canvas.drawCircle(x, y + TIMELINE_RADIUS, TIMELINE_RADIUS, p);
		p.reset();
		currentLength += TIMELINE_RADIUS*2;
		
	}
	
	private void drawLine(int x, int y, int length ,Canvas canvas){
		p.setColor(Color.parseColor("#D3D3D3"));
		p.setStrokeWidth(4);
		p.setAntiAlias(true);
		canvas.drawLine(x, y, x , y + length, p);
		p.reset();
		currentLength += length;
	}
	
	private void drawThing(int x, int y, String title, String time, Canvas canvas){
		p.setAntiAlias(true);
		//p.setColor(Color.parseColor("#F08080"));
		p.setColor(Color.parseColor("#0099CC"));
		p.setStrokeWidth(2);
		p.setStyle(Style.STROKE);
		RectF r1 = new RectF();
		r1.left = x + TIME_THING_INTERVAL;
		r1.top = y - THING_Y/2;
		r1.right = x + TIME_THING_INTERVAL + THING_X;
		r1.bottom = y + THING_Y/2;

		if(!rectMap.containsKey(time)){
			rectMap.put(time, r1);
		}else{
			rectMap.remove(time);
			rectMap.put(time, r1);
		}

		canvas.drawRoundRect(r1, 10, 10, p);

		Path path = new Path();
		path.moveTo(x + TIME_THING_INTERVAL, y - 6);
		path.lineTo(x + TIME_THING_INTERVAL - 10, y);
		path.lineTo(x + TIME_THING_INTERVAL, y + 6);
		canvas.drawPath(path, p);

		p.setColor(Color.WHITE);
		canvas.drawLine(x + TIME_THING_INTERVAL, y - 5, x + TIME_THING_INTERVAL, y + 5, p);

		p.reset();
		p.setAntiAlias(true);
		//p.setColor(Color.parseColor("#FFE4E1"));
		p.setColor(Color.parseColor("#FFFFFF"));
		canvas.drawRoundRect(r1, 10, 10, p);
		canvas.drawPath(path, p);

		p.reset();
		p.setTextSize(BIG_WORD_SIZE);
		FontMetricsInt fontMetrics = p.getFontMetricsInt();
		float baseline = r1.top + (r1.bottom - r1.top - fontMetrics.bottom + fontMetrics.top)/2 - fontMetrics.top;
		p.setAntiAlias(true);
		//p.setColor(Color.BLACK);
		p.setColor(Color.parseColor("#33B5E5"));
		p.setTypeface(Typeface.DEFAULT_BOLD);
		p.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(title, r1.centerX(), baseline , p);
		p.setColor(Color.GRAY);
		p.setTextSize(SMALL_WORD_SIZE);
		p.setTypeface(Typeface.DEFAULT);
		fontMetrics = p.getFontMetricsInt();
		baseline = r1.top + (r1.bottom - r1.top - fontMetrics.bottom + fontMetrics.top)/2 - fontMetrics.top;
		String[] timeSpit = time.split(":");
		String hourAndMinute = timeSpit[0] + ":" + timeSpit[1];
		canvas.drawText(hourAndMinute , x - 35, baseline, p);
	}

	private void drawThingByDeleteAnimation(int x, int y, String title, String time, Canvas canvas){
		if(screenWidth == 0){
			Activity parent = (Activity) getContext();
			screenWidth = parent.getResources().getDisplayMetrics().widthPixels;
		}

		float f = 0.9f -(((float)DeleteAnimationTIME_THING_INTERVAL/screenWidth)*1.6f);
		//Log.d(TAG, "float f :" + f);
		p.setAntiAlias(true);
		//p.setColor(Color.parseColor("#F08080"));
		p.setColor(Color.parseColor("#0099CC"));
		p.setAlpha((int)(f*255));
		p.setStrokeWidth(2);
		p.setStyle(Style.STROKE);
		RectF r1 = new RectF();
		r1.left = x + DeleteAnimationTIME_THING_INTERVAL;
		r1.top = y - THING_Y/2;
		r1.right = x + DeleteAnimationTIME_THING_INTERVAL + THING_X;
		r1.bottom = y + THING_Y/2;

		if(!rectMap.containsKey(time)){
			rectMap.put(time, r1);
		}else{
			rectMap.remove(time);
			rectMap.put(time, r1);
		}

		canvas.drawRoundRect(r1, 10, 10, p);

		Path path = new Path();
		path.moveTo(x + DeleteAnimationTIME_THING_INTERVAL, y - 6);
		path.lineTo(x + DeleteAnimationTIME_THING_INTERVAL - 10, y);
		path.lineTo(x + DeleteAnimationTIME_THING_INTERVAL, y + 6);
		canvas.drawPath(path, p);

		p.setColor(Color.WHITE);
		//p.setAlpha((int)(f*255));
		canvas.drawLine(x + DeleteAnimationTIME_THING_INTERVAL, y - 5, x + DeleteAnimationTIME_THING_INTERVAL, y + 5, p);

		p.reset();
		p.setAntiAlias(true);
		//p.setColor(Color.parseColor("#FFE4E1"));
		p.setColor(Color.parseColor("#FFFFFF"));
		p.setAlpha((int)(f*255));
		canvas.drawRoundRect(r1, 10, 10, p);
		canvas.drawPath(path, p);

		p.reset();

		p.setTextSize(BIG_WORD_SIZE);
		FontMetricsInt fontMetrics = p.getFontMetricsInt();
		float baseline = r1.top + (r1.bottom - r1.top - fontMetrics.bottom + fontMetrics.top)/2 - fontMetrics.top;
		p.setAntiAlias(true);
		//p.setColor(Color.BLACK);
		p.setColor(Color.parseColor("#33B5E5"));
		p.setAlpha((int)(f*255));
		p.setTypeface(Typeface.DEFAULT_BOLD);
		p.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(title, r1.centerX(), baseline , p);
		p.setColor(Color.GRAY);
		p.setTextSize(SMALL_WORD_SIZE);
		p.setTypeface(Typeface.DEFAULT);
		fontMetrics = p.getFontMetricsInt();
		baseline = r1.top + (r1.bottom - r1.top - fontMetrics.bottom + fontMetrics.top)/2 - fontMetrics.top;
		String[] timeSpit = time.split(":");
		String hourAndMinute = timeSpit[0] + ":" + timeSpit[1];
		canvas.drawText(hourAndMinute , x - 35, baseline, p);
	}

	/*
	public boolean onTouchEvent(MotionEvent event) {
		//firstVisiblePosition is current screen the first item.
		//int firstVisiblePosition = listview.getFirstVisiblePosition();
		int top = this.getTop();

		float x = event.getRawX();
		float y = event.getRawY();
		//topHeight is statusBarHeight + titleBarHeight, because the y value include statusBarHeight and titleBarHeight.
		y -= topHeight;
		//top is current item out of current screen value. because top is negative number, so less it.
		y -= top;
		for (String key : rectMap.keySet()){
			RectF r = rectMap.get(key);
			if(r.contains(x, y)){
				Log.i(TAG, "time :" + key);
			}
		}
		return super.onTouchEvent(event);
	}
	*/

	/*
	long downTimeMillis;

	public boolean dispatchTouchEvent(MotionEvent ev) {
		//Log.i(TAG, "dispatchTouchEvent");
		switch(ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//Log.i(TAG, "ACTION_DOWN");
				downTimeMillis = System.currentTimeMillis();
				break;
			case MotionEvent.ACTION_UP:
				//Log.i(TAG, "ACTION_UP");
				if(((System.currentTimeMillis()-downTimeMillis)>1500)){
					int top = this.getTop();

					float x = ev.getRawX();
					float y = ev.getRawY();
					y -= topHeight;
					y -= top;
					for (String key : rectMap.keySet()){
						RectF r = rectMap.get(key);
						if(r.contains(x, y)){
							Log.i(TAG, "long touch, and time :" + key);
						}
					}
				}else{
					int top = this.getTop();

					float x = ev.getRawX();
					float y = ev.getRawY();
					y -= topHeight;
					y -= top;
					for (String key : rectMap.keySet()){
						RectF r = rectMap.get(key);
						if(r.contains(x, y)){
							Log.i(TAG, "short touch, and time :" + key);
						}
					}
				}
				break;
		}
		return true;
	}
	*/

	float startX;
	float startY;
	float movingX;
	float movingY;
	float endX;
	float endY;

	boolean clickAtThing = false;
	boolean isMove = false;
	long startTimeMillis;

	String useTimeDate = "";
	protected void resetTouchEvenData(){
		startX = 0f;
		startY = 0f;
		movingX = 0f;
		movingY = 0f;
		endX = 0f;
		endY = 0f;
		clickAtThing = false;
		isMove = false;
		startTimeMillis = 0L;
		useTimeDate = "";
		DeleteAnimationTIME_THING_INTERVAL = TIME_THING_INTERVAL;
	}
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//Log.i(TAG, "dispatchTouchEvent");
		switch(ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				resetTouchEvenData();
				//Log.i(TAG, "ACTION_DOWN");
				startTimeMillis = System.currentTimeMillis();
				int top = this.getTop();

				float x = ev.getRawX();
				float y = ev.getRawY();
				y -= topHeight;
				y -= top;
				startX = x;
				startY = y;
				movingX = startX;
				movingY = startY;
				for (String key : rectMap.keySet()){
					RectF r = rectMap.get(key);
					if(r.contains(x, y)){
						//Log.i(TAG, "long touch, and time :" + key);
						clickAtThing = true;
						useTimeDate = key;
						needReDrawByTime = useTimeDate;
						Log.d(TAG, "r top:" + r.top);
						Log.d(TAG, "r bottom:" + r.bottom);
						Log.d(TAG, "currentMoveY:"+startY);
						break;
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				isMove = true;
				int currentMoveTop = this.getTop();

				float currentMoveX = ev.getRawX();
				float currentMoveY = ev.getRawY();
				currentMoveY -= topHeight;
				currentMoveY -= currentMoveTop;

				if(clickAtThing){
					////boolean outRect = true;
					for (String key : rectMap.keySet()){
						RectF r = rectMap.get(key);
						//why do this, because the even was intercept by ViewGroup .wait, maybe use MotionEvent.ACTION_CANCEL.
						//RectF r1 = new RectF(r.left+15, r.top +15, r.right -15, r.bottom -15 );
						//Log.d(TAG, "r top:" + r1.top);
						//Log.d(TAG, "r bottom:" + r1.bottom);
						//Log.d(TAG, "currentMoveY:"+currentMoveY);
						if(r.contains(currentMoveX, currentMoveY)&& key.equals(useTimeDate)){
							//outRect = false;
							if(currentMoveX>=movingX){
								//call onDraw
								float offset = currentMoveX - movingX;
								DeleteAnimationTIME_THING_INTERVAL += offset;

								postInvalidate();

								movingX = currentMoveX;
								movingY = currentMoveY;
								break;
							}
						}
					}
					//Log.i(TAG, "outRect:"+outRect);
					//touch from up to down , was intercept by ViewGroup so ...
					//if(outRect){
					//	TIME_THING_INTERVAL = oldTIME_THING_INTERVAL;
					//	postInvalidate();
					//	clickAtThing = false;
					//}
				}
				//Log.i(TAG, "ACTION_MOVE");
				break;
			case MotionEvent.ACTION_UP:
				//Log.i(TAG, "ACTION_UP");


				float currentUpX = ev.getRawX();

				needReDrawByTime = null;
				if(clickAtThing){
					if(screenWidth == 0){
						Activity parent = (Activity) getContext();
						screenWidth = parent.getResources().getDisplayMetrics().widthPixels;
					}
					if(currentUpX - startX > screenWidth/2){
						//Log.d(TAG, "currentUpX:"+currentUpX);
						//Log.d(TAG, "startX:"+startX);
						//Log.d(TAG, "screenWidth/2:"+screenWidth/2);
						//Log.d(TAG, "remove this thing");
						//modify json data.(remove this jsonobject by time)

						JSONArray array = json.optJSONArray(Global.JSON_KEY_THINGS);
						ArrayList<JSONObject> list = new ArrayList<JSONObject>();
						for(int i=0; i < array.length(); i++){
							JSONObject timeThing = array.optJSONObject(i);
							String time = timeThing.optString(Global.JSON_KEY_TIME);
							if(!time.equals(useTimeDate))
								list.add(timeThing);
						}
						JSONArray newArray = new JSONArray(list);
						json.remove(Global.JSON_KEY_THINGS);
						try {
							json.put(Global.JSON_KEY_THINGS, newArray);
							//save to file.
							//FileUtil.getInstance().saveJsonToFile();
						}catch (Exception e){e.printStackTrace();}

					}
					DeleteAnimationTIME_THING_INTERVAL = TIME_THING_INTERVAL;
					invalidate();
					if(!isMove || (System.currentTimeMillis() - startTimeMillis)<300){
						//jump to style activity
						jumpToStyleActivity(useTimeDate);
					}
				}
				break;

			case MotionEvent.ACTION_CANCEL:
				needReDrawByTime = null;
				if(clickAtThing){

					DeleteAnimationTIME_THING_INTERVAL = TIME_THING_INTERVAL;
					invalidate();
				}
				//Log.i(TAG, "ACTION_CANCEL");
				break;
		}
		return true;
	}

	enum StyleActivityEnum {
		DrinkStyleActivity,
		FoodStyleActivity,
		HumanStyleActivity,
		SceneryStyleActivity,
		StoryStyleActivity,
		TravelStyleActivity
	}

	public void jumpToStyleActivity(String useTimeDate){
		JSONArray array = json.optJSONArray(Global.JSON_KEY_THINGS);
		for(int i=0; i < array.length(); i++){
			JSONObject timeThing = array.optJSONObject(i);
			String time = timeThing.optString(Global.JSON_KEY_TIME);
			if(time.equals(useTimeDate)){
				String style = timeThing.optString(Global.JSON_KEY_STYLE);
				StyleActivityEnum enumval = StyleActivityEnum.valueOf(style);
				Intent intent = new Intent();
				switch (enumval){
					case DrinkStyleActivity:
						intent.setClass(getContext(), DrinkStyleActivity.class);
						break;
					case FoodStyleActivity:
						intent.setClass(getContext(), FoodStyleActivity.class);
						break;
					case HumanStyleActivity:
						intent.setClass(getContext(), HumanStyleActivity.class);
						break;
					case SceneryStyleActivity:
						intent.setClass(getContext(), SceneryStyleActivity.class);
						break;
					case StoryStyleActivity:
						intent.setClass(getContext(), StoryStyleActivity.class);
						break;
					case TravelStyleActivity:
						intent.setClass(getContext(), TravelStyleActivity.class);
						break;

				}
				getContext().startActivity(intent);
				//((Activity)getContext()).finish();
				break;
			}
		}
	}

	public int getStatusBarHeight(){
		int y = 0;
		try {
			Class c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			y = getResources().getDimensionPixelSize(x);
		}catch (Exception e){
			e.printStackTrace();
		}
		Log.i(TAG, "StatusBarHeight:"+y);
		return y;
	}

	public int getTitleBarHeight(int statusBarHeight){
		Activity parent = (Activity) getContext();
		int contentTop = parent.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		int titleBarHeight = contentTop - statusBarHeight;
		Log.i(TAG, "titleBarHeight:"+titleBarHeight);
		return titleBarHeight;
	}

}


