package com.brisktouch.timeline.add;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.brisktouch.timeline.R;

import java.util.Objects;

/**
 * Created by caojim on 15/3/7.
 */
public class EditWordUtil {

    public static View getEditWord(Context context,final TextView textView){
        final AssetManager mgr = context.getAssets();
        //boolean isChina = context.getResources().getConfiguration().locale.getCountry().equals("CN");
        final View view = LayoutInflater.from(context).inflate(R.layout.edit_word, null);

        final LinearLayout fontLinearLayout = (LinearLayout)view.findViewById(R.id.fontLinearLayout);
        final LinearLayout colorLinearLayout = (LinearLayout)view.findViewById(R.id.colorLinearLayout);
        final LinearLayout sizeLinearLayout = (LinearLayout)view.findViewById(R.id.sizeLinearLayout);

        //font style ListView
        final ListView fontListView = (ListView)view.findViewById(R.id.listView);
        fontListView.setDivider(null);

        Typeface[] data = {
                Typeface.DEFAULT,
                Typeface.createFromAsset(mgr, "Fonts/zh_cn/MFTheGoldenEra_Noncommercial-Light.otf"),
                Typeface.createFromAsset(mgr, "Fonts/zh_cn/MFQingShu_Noncommercial-Regular.otf"),
                Typeface.createFromAsset(mgr, "Fonts/zh_cn/MFPinSong_Noncommercial-Regular.otf")};

        fontListView.setAdapter(new SimpleAdapter(WordStyle.fontStyle, context, data, textView));

        fontListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO bug, need modify it.
                for(int index=0;index<=i;index++){
                    LinearLayout lt = (LinearLayout)fontListView.getChildAt(index);
                    if(index!=i){
                        ((LinearLayout)lt.getChildAt(0)).getChildAt(0).setVisibility(View.INVISIBLE);
                    }else{
                        ((LinearLayout)lt.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
                    }

                }
                //next code: listView -> itemView(edit_word_font_list_item.xml) ->  LinearLayout -> LinearLayout -> TextView
                textView.setTypeface(((TextView)((LinearLayout)((LinearLayout)fontListView.getChildAt(i)).getChildAt(0)).getChildAt(1)).getTypeface());
            }
        });
        return view;
    }



    enum WordStyle{
        fontStyle,
        colorStyle,
        sizeStyle
    }

    public static class SimpleAdapter extends BaseAdapter{
        WordStyle wordStyle;
        Context context;
        Object[] data;
        TextView textView;
        public SimpleAdapter(WordStyle ws, Context context, Object[] data, TextView textView){
            wordStyle = ws;
            this.context = context;
            this.data = data;
            this.textView = textView;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout linearLayout = null;
            if (convertView != null) {
                linearLayout = (LinearLayout) convertView;
            }

            if(wordStyle == WordStyle.fontStyle){
                if(linearLayout == null){
                    linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.edit_word_font_list_item, null);
                }
                Typeface old = textView.getTypeface();
                if(old == null){
                    old = Typeface.DEFAULT;
                }

                Typeface tf = (Typeface)data[position];

                if(tf.equals(old)){
                    linearLayout.findViewById(R.id.imageView7).setVisibility(View.VISIBLE);
                }

                TextView tv = (TextView) linearLayout.findViewById(R.id.textVitem);
                tv.setTypeface(tf);

            }else if(wordStyle == WordStyle.sizeStyle){

            }
            return linearLayout;
        }
    }

    public static class StyleLinearLayout implements View.OnClickListener {

        View[] v;
        WordStyle wordStyle;
        View[] listViews;

        public StyleLinearLayout(View[] v, WordStyle ws){
            this.v = v;
            wordStyle = ws;
        }

        @Override
        public void onClick(View view){
            for(View v : this.v){
                if(v == view){
                    switch (wordStyle){
                        case fontStyle:
                            ((LinearLayout)v).getChildAt(0).setBackgroundResource(R.drawable.icon_ziti_white);
                            break;
                        case colorStyle:
                            ((LinearLayout)v).getChildAt(0).setBackgroundResource(R.drawable.icon_yanse_white);
                            break;
                        case sizeStyle:
                            ((LinearLayout)v).getChildAt(0).setBackgroundResource(R.drawable.icon_zihao_white);
                            break;
                    }
                    ((TextView)((LinearLayout)v).getChildAt(1)).setTextColor(Color.WHITE);
                }else{
                    switch (wordStyle){
                        case fontStyle:
                            ((LinearLayout)v).getChildAt(0).setBackgroundResource(R.drawable.icon_ziti_gray);
                            break;
                        case colorStyle:
                            ((LinearLayout)v).getChildAt(0).setBackgroundResource(R.drawable.icon_yanse_gray);
                            break;
                        case sizeStyle:
                            ((LinearLayout)v).getChildAt(0).setBackgroundResource(R.drawable.icon_zihao_gray);
                            break;
                    }
                    ((TextView)((LinearLayout)v).getChildAt(1)).setTextColor(Color.GRAY);
                }
            }
        }

        public void displaySelectedListView(){
            for(View v : listViews){

            }
        }

    }

}
