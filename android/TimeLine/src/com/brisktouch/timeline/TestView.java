package com.brisktouch.timeline;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
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
import java.util.Calendar;
import java.util.Date;

import java.util.HashMap;


class TestView extends TextView {
	public static String TAG = "TestView";
	public static final int DATELINE_LENGTH = 70;
	public static final int DATELINE_RADIUS = 30;
	public static final int TIMELINE_RADIUS = 10;
	public static final int MARGIN_RIGHT = 100;
	public static final int DISPLAY_DATE_STRING_X = 150;
	public static final int THING_X = 240;
	public static final int THING_Y = 70;
	public static final int TIME_THING_INTERVAL = 30;
	public static final String YEAR = "年";
	public static final String MONTH = "月";
	
	private Paint p;
	private JSONObject json;
	private int currentLength = 0;
	private int height = 0;

	private HashMap<String, RectF> rectMap;

	private int topHeight;

	public TestView(Context context, JSONObject json ) {
		super(context);
		p = new Paint();
		this.json = json;
		height += 20;
		height += DATELINE_RADIUS*2;
		JSONArray array = json.optJSONArray("things");
		for(int i=0; i < array.length(); i++){
			height += DATELINE_LENGTH;
			height += TIMELINE_RADIUS*2;
		}
		height += DATELINE_LENGTH;
		rectMap = new HashMap<String, RectF>();

		int statusHeight = getStatusBarHeight();
		int titleHeight = getTitleBarHeight(statusHeight);
		topHeight = statusHeight + titleHeight;
		Log.i(TAG, "topHeight :" + topHeight);
	}
	
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		currentLength = 0;
		String date = json.optString("date");
		JSONArray array = json.optJSONArray("things");
		drawLine(MARGIN_RIGHT, currentLength, 20, canvas);
		drawDate(MARGIN_RIGHT, currentLength, date, canvas);
		for(int i=0; i < array.length(); i++){
			JSONObject timeThing = array.optJSONObject(i);
			String time = timeThing.optString("time");
			String context = timeThing.optString("context");
			//Log.i(TAG, "time is " + time);
			currentLength += 2;
			drawLine(MARGIN_RIGHT, currentLength, DATELINE_LENGTH -4, canvas);
			currentLength += 2;
			drawTime(MARGIN_RIGHT, currentLength, canvas);
			drawThing(MARGIN_RIGHT, currentLength - TIMELINE_RADIUS, context, time, canvas);
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
				week = Tool.getWeek(d1);
			}catch (Exception e){}
			p.setColor(Color.BLACK);
			p.setTextSize(24);
			FontMetricsInt fontMetrics = p.getFontMetricsInt();
			int baseline = y + (y+DATELINE_RADIUS*2 - y - fontMetrics.bottom + fontMetrics.top)/2 - fontMetrics.top;
			p.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(day , x, baseline , p); 
			p.setColor(Color.GRAY);
			String displayDate = year + YEAR + month + MONTH + " " + week;
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
	
	private void drawThing(int x, int y, String context, String time, Canvas canvas){
		p.setAntiAlias(true);
		p.setColor(Color.parseColor("#F08080"));
		p.setStrokeWidth(2);
		p.setStyle(Style.STROKE);
		RectF r1 = new RectF();
		r1.left = x + TIME_THING_INTERVAL;
		r1.top = y - THING_Y/2;
		r1.right = x + TIME_THING_INTERVAL + THING_X;
		r1.bottom = y + THING_Y/2;

		if(!rectMap.containsKey(time)){
			rectMap.put(time, r1);
		}
		/*else{
			rectMap.remove(time);
			rectMap.put(time, r1);
		}
		*/
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
		p.setColor(Color.parseColor("#FFE4E1"));
		canvas.drawRoundRect(r1, 10, 10, p);
		canvas.drawPath(path, p);

		p.reset();
		FontMetricsInt fontMetrics = p.getFontMetricsInt();
		float baseline = r1.top + (r1.bottom - r1.top - fontMetrics.bottom + fontMetrics.top)/2 - fontMetrics.top;
		p.setTextSize(24);
		p.setAntiAlias(true);
		p.setColor(Color.BLACK);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		p.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(context + System.currentTimeMillis(), r1.centerX(), baseline , p); 
		p.setColor(Color.GRAY);
		p.setTextSize(18);
		p.setTypeface(Typeface.DEFAULT);
		fontMetrics = p.getFontMetricsInt();
		baseline = r1.top + (r1.bottom - r1.top - fontMetrics.bottom + fontMetrics.top)/2 - fontMetrics.top;
		canvas.drawText(time , x - 50, baseline, p); 
	}

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
		return y;
	}

	public int getTitleBarHeight(int statusBarHeight){
		Activity parent = (Activity) getContext();
		int contentTop = parent.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		int titleBarHeight = contentTop - statusBarHeight;
		return titleBarHeight;
	}

}


