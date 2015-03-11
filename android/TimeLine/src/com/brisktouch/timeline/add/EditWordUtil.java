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
import com.brisktouch.timeline.custom.CircleView;

import java.util.Objects;

/**
 * Created by caojim on 15/3/7.
 */
public class EditWordUtil {

    public static View getEditWord(Context context,final TextView textView){
        final AssetManager mgr = context.getAssets();
        //boolean isChina = context.getResources().getConfiguration().locale.getCountry().equals("CN");
        final View view = LayoutInflater.from(context).inflate(R.layout.edit_word, null);

        //font style ListView
        final ListView fontListView = (ListView)view.findViewById(R.id.listView);
        final ListView sizeListView = (ListView)view.findViewById(R.id.listView3);

        TableRow colorTables[] = {
                (TableRow)view.findViewById(R.id.tableRow),
                (TableRow)view.findViewById(R.id.tableRow1),
                (TableRow)view.findViewById(R.id.tableRow2),
                (TableRow)view.findViewById(R.id.tableRow3)};

        setColorTablesListener(colorTables, textView);



        fontListView.setDivider(null);
        sizeListView.setDivider(null);

        Typeface[] data = {
                Typeface.DEFAULT,
                Typeface.createFromAsset(mgr, "Fonts/zh_cn/MFTheGoldenEra_Noncommercial-Light.otf"),
                Typeface.createFromAsset(mgr, "Fonts/zh_cn/MFPinSong_Noncommercial-Regular.otf"),
                Typeface.createFromAsset(mgr, "Fonts/zh_cn/MFQingShu_Noncommercial-Regular.otf")};

        Float[] floats = {
                18.0f,
                20.0f,
                22.0f,
                24.0f
        };

        fontListView.setAdapter(new SimpleAdapter(WordStyle.fontStyle, context, data, textView));
        sizeListView.setAdapter(new SimpleAdapter(WordStyle.sizeStyle, context, floats, textView));

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

        view.findViewById(R.id.fontLinearLayout).setOnClickListener(new StyleLinearLayout(WordStyle.fontStyle, view));
        view.findViewById(R.id.colorLinearLayout).setOnClickListener(new StyleLinearLayout(WordStyle.colorStyle, view));
        view.findViewById(R.id.sizeLinearLayout).setOnClickListener(new StyleLinearLayout(WordStyle.sizeStyle, view));

        return view;
    }

    public static void setColorTablesListener(TableRow[] colorTables, final TextView tv){
        for(TableRow tableRow : colorTables){
            for(int i=0; i< tableRow.getChildCount(); i++){
                final CircleView circleView = (CircleView)tableRow.getChildAt(i);
                circleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv.setTextColor(circleView.getColor());
                    }
                });

            }
        }
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
                tv.setTextSize(20);
                tv.setTypeface(tf);

            }else if(wordStyle == WordStyle.sizeStyle){
                if(linearLayout == null){
                    linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.edit_word_size_list_item, null);
                }
                Float old = textView.getTextSize();

                Float ft = (Float)data[position];

                if(ft.equals(old)){
                    linearLayout.findViewById(R.id.imageView7).setVisibility(View.VISIBLE);
                }

                TextView tv = (TextView) linearLayout.findViewById(R.id.textVitem);
                tv.setText(R.string.word_size);
                tv.setTextSize(ft);
            }
            return linearLayout;
        }
    }

    public static class StyleLinearLayout implements View.OnClickListener {
        WordStyle wordStyle;
        LinearLayout fontLinearLayout;
        LinearLayout colorLinearLayout;
        LinearLayout sizeLinearLayout;

        View fontListView;
        View colorView;
        View sizeListView;
        public StyleLinearLayout(WordStyle ws, View main){
            wordStyle = ws;
            fontLinearLayout = (LinearLayout)main.findViewById(R.id.fontLinearLayout);
            colorLinearLayout = (LinearLayout)main.findViewById(R.id.colorLinearLayout);
            sizeLinearLayout = (LinearLayout)main.findViewById(R.id.sizeLinearLayout);

            fontListView = main.findViewById(R.id.listView);
            colorView = main.findViewById(R.id.colorTable);
            sizeListView = main.findViewById(R.id.listView3);
        }

        @Override
        public void onClick(View view) {
            switch (wordStyle) {
                case fontStyle:
                    //((LinearLayout) view).getChildAt(0).setBackgroundResource(R.drawable.icon_ziti_white);
                    //((TextView) ((LinearLayout) view).getChildAt(1)).setTextColor(Color.WHITE);
                    fontLinearLayout.getChildAt(0).setBackgroundResource(R.drawable.icon_ziti_white);
                    ((TextView)fontLinearLayout.getChildAt(1)).setTextColor(Color.WHITE);
                    colorLinearLayout.getChildAt(0).setBackgroundResource(R.drawable.icon_yanse_gray);
                    ((TextView)colorLinearLayout.getChildAt(1)).setTextColor(Color.GRAY);
                    sizeLinearLayout.getChildAt(0).setBackgroundResource(R.drawable.icon_zihao_gray);
                    ((TextView)sizeLinearLayout.getChildAt(1)).setTextColor(Color.GRAY);
                    fontListView.setVisibility(View.VISIBLE);
                    colorView.setVisibility(View.GONE);
                    sizeListView.setVisibility(View.GONE);
                    break;
                case colorStyle:
                    fontLinearLayout.getChildAt(0).setBackgroundResource(R.drawable.icon_ziti_gray);
                    ((TextView)fontLinearLayout.getChildAt(1)).setTextColor(Color.GRAY);
                    colorLinearLayout.getChildAt(0).setBackgroundResource(R.drawable.icon_yanse_white);
                    ((TextView)colorLinearLayout.getChildAt(1)).setTextColor(Color.WHITE);
                    sizeLinearLayout.getChildAt(0).setBackgroundResource(R.drawable.icon_zihao_gray);
                    ((TextView)sizeLinearLayout.getChildAt(1)).setTextColor(Color.GRAY);
                    fontListView.setVisibility(View.GONE);
                    colorView.setVisibility(View.VISIBLE);
                    sizeListView.setVisibility(View.GONE);
                    break;
                case sizeStyle:
                    fontLinearLayout.getChildAt(0).setBackgroundResource(R.drawable.icon_ziti_gray);
                    ((TextView)fontLinearLayout.getChildAt(1)).setTextColor(Color.GRAY);
                    colorLinearLayout.getChildAt(0).setBackgroundResource(R.drawable.icon_yanse_gray);
                    ((TextView)colorLinearLayout.getChildAt(1)).setTextColor(Color.GRAY);
                    sizeLinearLayout.getChildAt(0).setBackgroundResource(R.drawable.icon_zihao_white);
                    ((TextView)sizeLinearLayout.getChildAt(1)).setTextColor(Color.WHITE);
                    fontListView.setVisibility(View.GONE);
                    colorView.setVisibility(View.GONE);
                    sizeListView.setVisibility(View.VISIBLE);
                    break;
            }

        }
    }

}
