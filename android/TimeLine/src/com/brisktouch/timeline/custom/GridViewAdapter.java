package com.brisktouch.timeline.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.custom.BrowseNativeImageUtil;

/**
 * Created by jim on 4/8/2015.
 */
public class GridViewAdapter extends BaseAdapter{
    private Context mContext;
    private List<String> mList;
    private BrowseNativeImageUtil browseView;

    public GridViewAdapter(Context mContext,List<String> mList, BrowseNativeImageUtil browseView) {
        super();
        this.mContext = mContext;
        this.mList = mList;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from
                    (this.mContext).inflate(R.layout.test_gridview_item, null, false);
            holder.imageView = (ImageView)convertView.findViewById(R.id.gridview_item_image);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (this.mList != null) {
            String imagePath = this.mList.get(position);
            if (holder.imageView != null) {
                browseView.mImageFetcher.loadImage(imagePath, holder.imageView);
                holder.imageView.setTag(imagePath);
                holder.imageView.setOnClickListener(browseView);
                //holder.imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            }
        }

        return convertView;

    }


    private class ViewHolder {
        ImageView imageView;
    }

}