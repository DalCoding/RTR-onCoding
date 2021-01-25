package com.example.rotory.WriteContents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.R;


import java.util.ArrayList;

public class TagSelectDialog extends Activity {
    private static final String TAG = "tagSelectDialog";
    GridView gridView;
    Button plusBtn;
    TagDataAdapter adapter;


    ArrayList<String> selectedTag = new ArrayList<>();

    public static OnTagItemClickListener listener;

    public TagSelectDialog() {
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.write_road_tag);

        Intent intent = getIntent();
        selectedTag = intent.getStringArrayListExtra("selectedTag");
        Log.d(TAG,"넘겨온 태그리스트 확인" + selectedTag);

        gridView = findViewById(R.id.tagsList);
        gridView.setColumnWidth(30);
        gridView.setVerticalSpacing(4);
        gridView.setHorizontalSpacing(5);


        adapter = new TagDataAdapter(this, selectedTag);


        gridView.setAdapter(adapter);
        gridView.setNumColumns(adapter.getNumColumns());
        plusBtn = findViewById(R.id.tagPlusBtn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    static class TagDataAdapter extends BaseAdapter{
        Context mContext;

        ArrayList<String> selectedTag = new ArrayList<>();

        public static final String [] tags = new String[]{
                "#구경", "#데이트", "#장보기", "#사진찍기", "#쇼핑", "#산책", "#운동",
                "#신남", "#우울", "#핫플", "#한적한", "#비", "#눈", "#햇살",
                "#술", "#커피", "#디저트", "#맛집", "#공원","#낙엽", "#가을", "#꽃",
                "#봄","#여름", "#겨울", "#새벽","#저녁","#아침","#밤","#점심","#주말",
                ""
        };
        int rowCount;
        int columnCount;


        public TagDataAdapter(Context mContext, ArrayList<String> selectedTag) {
                super();
                this.mContext = mContext;
                columnCount = 4;
                rowCount = 8;
                this.selectedTag = selectedTag;
            }

         public TagDataAdapter(Context mContext) {

        }


            public ArrayList<String> getSelectedTag(){
                return selectedTag;
            }

            public int getNumColumns() {
                return columnCount;
            }
            @Override
            public int getCount() {
                return columnCount*rowCount;
            }
            @Override
            public Object getItem(int position) {
                return tags[position];
            }
            @Override
            public long getItemId(int position) {
                return 0;
            }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "getView(" + position + ") called.");
            int rowIndex = position/ rowCount;
            int columnIndex = position % rowCount;

            Log.d(TAG, "Index : " + rowIndex + ", " + columnIndex);

            GridView.LayoutParams params = new GridView.LayoutParams(
                    GridView.LayoutParams.MATCH_PARENT,
                    GridView.LayoutParams.MATCH_PARENT
            );

            Button tagItem = new Button(mContext);
            tagItem.setText(tags[position]);
            tagItem.setLayoutParams(params);
            tagItem.setPadding(2,2,2,2);
            tagItem.setBackgroundColor(Color.argb(100,239,235, 218));
            tagItem.setHeight(parent.getHeight()/8);
            tagItem.setTag(tags[position]);
            selectedTag = getSelectedTag();
            Log.d(TAG,"getView에서 읽어내는지 확인" + selectedTag);
            if (selectedTag.contains(tagItem.getText().toString())){
                tagItem.setTextColor(Color.BLACK);

            }else{
                tagItem.setTextColor(Color.LTGRAY);
            }


            tagItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TagSelectDialog.listener != null) {
                        TagSelectDialog.listener.onItemSelected(v.getTag().toString());
                        if (tagItem.getCurrentTextColor() == Color.BLACK){
                            tagItem.setTextColor(Color.LTGRAY);
                        }else{
                            tagItem.setTextColor(Color.BLACK);
                        }
                    }
                }
            });
            return tagItem;
        }
    }
}