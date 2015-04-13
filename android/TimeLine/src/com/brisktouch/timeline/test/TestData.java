package com.brisktouch.timeline.test;

import java.util.ArrayList;
import java.util.Random;

import org.cjson.JSONArray;
import org.cjson.JSONException;
import org.cjson.JSONObject;

public class TestData {
	private JSONObject json;
	private JSONArray data;
	
	public TestData(){
		json = new JSONObject();
		data = new JSONArray();
	}
	
	public JSONObject getJson(){
		try {
			json.put("data", data);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	public JSONArray addThings(ArrayList<String[]> list){
		JSONArray things = new JSONArray();
		
		Random rd = new Random();
		int max = rd.nextInt(list.size()-1);
		max += 1;
		max = list.size();
		try {
			for(int i=0; i < max; i++){
				JSONObject j = new JSONObject();
				String s[] = list.get(i);
				j.put("time", s[0]);
				j.put("context", s[1]);
				things.put(j);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return things;
	}
	
	public void addData(String date, JSONArray thing){
		JSONObject j = new JSONObject();
		try {
			j.put("date", date);
			j.put("things", thing);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.put(j);
	}
	
	public static JSONObject getRandomData(){
		TestData test = new TestData();
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(new String[]{"12:11","test1"});
		list.add(new String[]{"12:12","test2"});
		list.add(new String[]{"12:13","test3"});
		list.add(new String[]{"12:14","test4"});
		list.add(new String[]{"12:15","test5"});
		list.add(new String[]{"12:16","test6"});
		list.add(new String[]{"12:17","test7"});
		list.add(new String[]{"12:18","test8"});
		list.add(new String[]{"12:19","test9"});
		list.add(new String[]{"12:20","test10"});
		list.add(new String[]{"12:25","test11"});
		//test.addData("1991.8.4", test.addThings(list));
		//test.addData("1992.8.4", test.addThings(list));
		//test.addData("1993.8.4", test.addThings(list));
		//test.addData("1994.8.4", test.addThings(list));
		//test.addData("1995.8.4", test.addThings(list));
		//test.addData("1996.8.4", test.addThings(list));
		//test.addData("1997.8.4", test.addThings(list));
		//test.addData("1998.8.4", test.addThings(list));
		//test.addData("1999.8.4", test.addThings(list));
		//test.addData("1999.8.4", test.addThings(list));
		//test.addData("1999.8.4", test.addThings(list));
		test.addData("2014.12.5", test.addThings(list));
		test.addData("2014.12.18", test.addThings(list));
		return test.getJson();
	}
}
