package com.brisktouch.timeline.custom;
import java.text.SimpleDateFormat;
import java.util.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.brisktouch.timeline.R;


/**
 * Created by jim on 4/8/2015.
 */
public class ListViewAdapter extends BaseAdapter {
    private List<Map.Entry<Long, List<String>>> mList;
    private Context mContext;
    private BrowseNativeImageUtil browseView;

    public ListViewAdapter(List<Map.Entry<Long, List<String>>> mList, Context mContext, BrowseNativeImageUtil browseView) {
        super();
        this.mList = mList;
        this.mContext = mContext;
        this.browseView = browseView;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        } else {
            return this.mList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        } else {
            return this.mList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from
                    (this.mContext).inflate(R.layout.test_listview_item, null, false);
            holder.textView = (TextView) convertView.findViewById(R.id.listview_item_textview);
            holder.gridView = (GridView) convertView.findViewById(R.id.listview_item_gridview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (this.mList != null) {
            if (holder.textView != null) {
                long dateTime = this.mList.get(position).getKey();
                SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
                holder.textView.setText(timeFormat.format(new Date(dateTime)));
            }
            if (holder.gridView != null) {
                List<String> imagePathList = this.mList.get(position).getValue();
                GridViewAdapter gridViewAdapter=new GridViewAdapter(mContext, imagePathList, this.browseView);
                holder.gridView.setAdapter(gridViewAdapter);
            }

        }

        return convertView;

    }


    private class ViewHolder {
        TextView textView;
        GridView gridView;
    }

}
