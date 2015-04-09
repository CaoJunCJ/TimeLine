package com.brisktouch.timeline.add;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.custom.CircleView;
import com.brisktouch.timeline.util.Utils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by caojim on 15/3/7.
 */
public class EditWordUtil {
    private Context context;

    private View editWord;

    private TextView currentSelectTextView;

    private SimpleAdapter fontListAdapter;
    private SimpleAdapter sizeListAdapter;

    private TextViewAction textAction = TextViewAction.Normal;

    public EditWordUtil(Context context){
        this.context = context;
        init();
    }

    public void setTextAction(TextViewAction textAction){
        this.textAction = textAction;
    }


    public void setCurrentSelectTextView(TextView tv){
        currentSelectTextView = tv;
        flush();
    }

    private void flush(){
        fontListAdapter.notifyDataSetChanged();
        sizeListAdapter.notifyDataSetChanged();
        editWord.findViewById(R.id.fontLinearLayout).performClick();
    }

    public void init(){
            final AssetManager mgr = context.getAssets();
            //boolean isChina = context.getResources().getConfiguration().locale.getCountry().equals("CN");
            editWord = LayoutInflater.from(context).inflate(R.layout.edit_word, null);

            //font style ListView
            final ListView fontListView = (ListView)editWord.findViewById(R.id.listView);
            final ListView sizeListView = (ListView)editWord.findViewById(R.id.listView3);

            TableRow colorTables[] = {
                    (TableRow)editWord.findViewById(R.id.tableRow),
                    (TableRow)editWord.findViewById(R.id.tableRow1),
                    (TableRow)editWord.findViewById(R.id.tableRow2),
                    (TableRow)editWord.findViewById(R.id.tableRow3)};

            setColorTablesListener(colorTables);



            fontListView.setDivider(null);
            sizeListView.setDivider(null);

            Typeface[] typeFaceData = {
                    Typeface.DEFAULT,
                    Typeface.createFromAsset(mgr, "Fonts/zh_cn/MFTheGoldenEra_Noncommercial-Light.otf"),
                    Typeface.createFromAsset(mgr, "Fonts/zh_cn/MFPinSong_Noncommercial-Regular.otf"),
                    Typeface.createFromAsset(mgr, "Fonts/zh_cn/MFQingShu_Noncommercial-Regular.otf")};

            Float[] floats = {
                    13.0f,
                    14.0f,
                    15.0f,
                    16.0f,
                    17.0f,
                    18.0f,
                    20.0f,
                    22.0f,
                    24.0f
            };
            fontListAdapter = new SimpleAdapter(WordStyle.fontStyle, context, typeFaceData);
            sizeListAdapter = new SimpleAdapter(WordStyle.sizeStyle, context, floats);
            fontListView.setAdapter(fontListAdapter);
            sizeListView.setAdapter(sizeListAdapter);
            sizeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //TODO bug, need modify it.  =====>fix
                    /*
                    for(int index = 0; index < sizeListView.getAdapter().getCount() ; index++){
                        LinearLayout lt = (LinearLayout)sizeListView.getChildAt(index);
                        ((LinearLayout)lt.getChildAt(0)).getChildAt(0).setVisibility(View.INVISIBLE);
                        //NULLPOINTEXCEPTION ......
                    }
                    */
                    IterateViewGroup.iterateViewGroup(sizeListView, new IterateViewGroup.IterateViewCallBack() {
                        @Override
                        public void callBack(View v) {
                            v.setVisibility(View.INVISIBLE);
                        }
                    }, IterateViewGroup.ViewClassType.IMAGEVIEW);
                    ((LinearLayout)((LinearLayout)view).getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
                    currentSelectTextView.setTextSize(((TextView) ((LinearLayout) ((LinearLayout) sizeListView.getChildAt(i)).getChildAt(0)).getChildAt(1)).getTextSize());
                }
            });

            fontListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //TODO bug, need modify it.  =======>fix
                    for(int index = 0; index < fontListView.getAdapter().getCount() ; index++){
                        LinearLayout lt = (LinearLayout)fontListView.getChildAt(index);
                        ((LinearLayout)lt.getChildAt(0)).getChildAt(0).setVisibility(View.INVISIBLE);

                    }
                    ((LinearLayout)((LinearLayout)view).getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
                    //next code: listView -> itemView(edit_word_font_list_item.xml) ->  LinearLayout -> LinearLayout -> TextView
                    currentSelectTextView.setTypeface(((TextView)((LinearLayout)((LinearLayout)fontListView.getChildAt(i)).getChildAt(0)).getChildAt(1)).getTypeface());
                    switch (i){
                        case 0:
                            currentSelectTextView.setTag("DEFAULT");
                            break;
                        case 1:
                            currentSelectTextView.setTag("Fonts/zh_cn/MFTheGoldenEra_Noncommercial-Light.otf");
                            break;
                        case 2:
                            currentSelectTextView.setTag("Fonts/zh_cn/MFPinSong_Noncommercial-Regular.otf");
                            break;
                        case 3:
                            currentSelectTextView.setTag("Fonts/zh_cn/MFQingShu_Noncommercial-Regular.otf");
                            break;
                        default:
                            currentSelectTextView.setTag("DEFAULT");
                            break;
                    }
                }
            });

            editWord.findViewById(R.id.fontLinearLayout).setOnClickListener(new StyleLinearLayout(WordStyle.fontStyle, editWord));
            editWord.findViewById(R.id.colorLinearLayout).setOnClickListener(new StyleLinearLayout(WordStyle.colorStyle, editWord));
            editWord.findViewById(R.id.sizeLinearLayout).setOnClickListener(new StyleLinearLayout(WordStyle.sizeStyle, editWord));

            View acceptButton = editWord.findViewById(R.id.imageButton13);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText)editWord.findViewById(R.id.editText);
                    editText.clearFocus();
                    InputMethodManager imm = (InputMethodManager) context
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(Utils.hasCUPCAKE()){
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    }
                    String result;
                    String temp = editText.getText().toString();
                    switch (textAction){
                        case Normal:
                            currentSelectTextView.setText(editText.getText());
                            break;
                        case SplitChar:
                            result = "";
                            char[] chars = temp.toCharArray();
                            for(int i=0;i<chars.length;i++){
                                if(i==chars.length-1){
                                    result = result + chars[i];
                                }else{
                                    result = result + chars[i] + '\n';
                                }
                            }
                            currentSelectTextView.setText(result);
                            break;
                        case SplitWord:
                            result = "";
                            if(isChineseCharacter(temp)){
                                char[] charArray = temp.toCharArray();
                                for(int i=0;i<charArray.length;i++){
                                    if(i==charArray.length-1){
                                        result = result + charArray[i];
                                    }else{
                                        result = result + charArray[i] + '\n';
                                    }
                                }
                            }else{
                                String[] strings = temp.split("\\s+");
                                for(int i=0;i<strings.length;i++){
                                    if(i==strings.length-1){
                                        result = result + strings[i];
                                    }else{
                                        result = result + strings[i] + '\n';
                                    }
                                }
                            }
                            currentSelectTextView.setText(result);
                            break;
                        default:
                            currentSelectTextView.setText(editText.getText());
                            break;
                    }

                    currentSelectTextView.performClick();
                }
            });

        View hideSoftKeyBoard = editWord.findViewById(R.id.imageButton14);
        hideSoftKeyBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText)editWord.findViewById(R.id.editText);
                editText.clearFocus();
                InputMethodManager imm = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if(Utils.hasCUPCAKE()){
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
            }
        });

    }

    public static boolean isChineseCharacter(String str){
        if(str == null)
            return false;
        Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher m = p_str.matcher(str);
        if(m.find()&&m.group(0).equals(str)){
            return true;
        }
        return false;
    }

    public View getEditWordView(){ return editWord;}


    public void setColorTablesListener(TableRow[] colorTables){
        for(TableRow tableRow : colorTables){
            for(int i=0; i< tableRow.getChildCount(); i++){
                final CircleView circleView = (CircleView)tableRow.getChildAt(i);
                circleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentSelectTextView.setTextColor(circleView.getColor());
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

    public enum TextViewAction {
        Normal,
        SplitChar,
        SplitWord
    }

    public class SimpleAdapter extends BaseAdapter{
        WordStyle wordStyle;
        Context context;
        Object[] data;
        public SimpleAdapter(WordStyle ws, Context context, Object[] data){
            wordStyle = ws;
            this.context = context;
            this.data = data;
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
                Typeface old = currentSelectTextView.getTypeface();
                if(old == null){
                    old = Typeface.DEFAULT;
                }

                Typeface tf = (Typeface)data[position];

                if(tf.equals(old)){
                    linearLayout.findViewById(R.id.imageView7).setVisibility(View.VISIBLE);
                }else{
                    linearLayout.findViewById(R.id.imageView7).setVisibility(View.INVISIBLE);
                }

                TextView tv = (TextView) linearLayout.findViewById(R.id.textVitem);
                tv.setTextSize(20);
                tv.setTypeface(tf);

            }else if(wordStyle == WordStyle.sizeStyle){
                if(linearLayout == null){
                    linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.edit_word_size_list_item, null);
                }
                Float old = currentSelectTextView.getTextSize();

                Float ft = (Float)data[position];

                if(ft.equals(old)){
                    linearLayout.findViewById(R.id.imageView7).setVisibility(View.VISIBLE);
                }else{
                    linearLayout.findViewById(R.id.imageView7).setVisibility(View.INVISIBLE);
                }

                TextView tv = (TextView) linearLayout.findViewById(R.id.textVitem);
                tv.setText(R.string.word_size);
                tv.setTextSize(ft);
            }
            return linearLayout;
        }
    }

    public class StyleLinearLayout implements View.OnClickListener {
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
