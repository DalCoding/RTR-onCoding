package com.example.rotory.WriteContents;

import android.app.Activity;
import android.content.Context;
<<<<<<< HEAD
=======
import android.content.Intent;
>>>>>>> origin/master
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

<<<<<<< HEAD
=======
import java.util.ArrayList;

>>>>>>> origin/master
public class TagSelectDialog extends Activity {
    private static final String TAG = "tagSelectDialog";
    GridView gridView;
    Button plusBtn;
    TagDataAdapter adapter;

<<<<<<< HEAD
=======
    ArrayList<String> selectedTag = new ArrayList<>();

>>>>>>> origin/master
    public static OnTagItemClickListener listener;

    public TagSelectDialog() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.write_road_tag);

<<<<<<< HEAD
=======
        Intent intent = getIntent();
        selectedTag = intent.getStringArrayListExtra("selectedTag");
        Log.d(TAG,"넘겨온 태그리스트 확인" + selectedTag);

>>>>>>> origin/master
        gridView = findViewById(R.id.tagsList);
        gridView.setColumnWidth(30);
        gridView.setVerticalSpacing(4);
        gridView.setHorizontalSpacing(5);

<<<<<<< HEAD
        adapter = new TagDataAdapter(this);
=======
        adapter = new TagDataAdapter(this, selectedTag);
>>>>>>> origin/master

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
<<<<<<< HEAD
=======
        ArrayList<String> selectedTag = new ArrayList<>();
>>>>>>> origin/master

        public static final String [] tags = new String[]{
                "#구경", "#데이트", "#장보기", "#사진찍기", "#쇼핑", "#산책", "#운동",
                "#신남", "#우울", "#핫플", "#한적한", "#비", "#눈", "#햇살",
                "#술", "#커피", "#디저트", "#맛집", "#공원","#낙엽", "#가을", "#꽃",
                "#봄","#여름", "#겨울", "#새벽","#저녁","#아침","#밤","#점심","#주말",
                ""

        };
        int rowCount;
        int columnCount;

<<<<<<< HEAD
        public TagDataAdapter(Context mContext) {
=======
        public TagDataAdapter(Context mContext, ArrayList<String> selectedTag) {
>>>>>>> origin/master
            super();
            this.mContext = mContext;
            columnCount = 4;
            rowCount = 8;
<<<<<<< HEAD
=======
           this.selectedTag = selectedTag;
        }

        public ArrayList<String> getSelectedTag(){
            return selectedTag;
>>>>>>> origin/master
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
<<<<<<< HEAD
=======
            selectedTag = getSelectedTag();
            Log.d(TAG,"getView에서 읽어내는지 확인" + selectedTag);
            if (selectedTag.contains(tagItem.getText().toString())){
                tagItem.setTextColor(Color.BLACK);

            }else{
                tagItem.setTextColor(Color.LTGRAY);
            }

>>>>>>> origin/master

            tagItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TagSelectDialog.listener != null) {
                        TagSelectDialog.listener.onItemSelected(v.getTag().toString());
<<<<<<< HEAD
=======
                        if (tagItem.getCurrentTextColor() == Color.BLACK){
                            tagItem.setTextColor(Color.LTGRAY);
                        }else{
                            tagItem.setTextColor(Color.BLACK);
                        }
>>>>>>> origin/master
                    }
                }
            });
            return tagItem;
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/master
