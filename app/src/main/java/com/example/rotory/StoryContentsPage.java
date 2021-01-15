package com.example.rotory;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnUserActItemClickListener;
import com.example.rotory.VO.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoryContentsPage extends Fragment {
    final static String TAG="StoryContentsPage";

    TextView scontentsGroupText;
    ImageView scontentsLinkImg;
    ImageView scontentsScrapImg;
    ImageView scontentsHeartImg;
    ImageView scontentsStarImg;
    TextView scontentsLocText;
    TextView scontentsTitleText;
    ImageView scontentsLevelImg;
    TextView scontentsUsernameText;
    ImageView scontentsBigImg;
    TextView scontentsMentText;
    RecyclerView scontentsThumbnailRView;
    TextView scontentsTextText;
    ImageView scontentsNextImg;
    EditText scontentsCommEdit;
    Button scontentsCommBtn;
    RecyclerView sCommRView;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); //db 선언

    ArrayList<Comment> commentArrayList;

    Context context;
    OnUserActItemClickListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (context != null) {
            context = null;
            listener = null;
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.story_contents_page, container, false);

        initUI(rootView);
        return rootView;
    }

    private void initUI(ViewGroup rootView) {
        scontentsCommBtn = rootView.findViewById(R.id.scontentsCommBtn);
        scontentsLinkImg = rootView.findViewById(R.id.scontentsLinkImg);
        scontentsScrapImg = rootView.findViewById(R.id.scontentsScrapImg);
        scontentsHeartImg = rootView.findViewById(R.id.scontentsHeartImg);
        scontentsTitleText = rootView.findViewById(R.id.scontentsTitleText);
        scontentsLevelImg = rootView.findViewById(R.id.scontentsLevelImg);
        scontentsUsernameText = rootView.findViewById(R.id.scontentsUsernameText);
        scontentsStarImg = rootView.findViewById(R.id.scontentsStarImg);
        scontentsCommEdit = rootView.findViewById(R.id.rcontentsCommEdit);
        sCommRView = rootView.findViewById(R.id.sCommRView);
        scontentsBigImg = rootView.findViewById(R.id.scontentsBigImg);
        scontentsMentText = rootView.findViewById(R.id.scontentsMentText);
        scontentsThumbnailRView = rootView.findViewById(R.id.scontentsThumbnailRView);
        scontentsTextText = rootView.findViewById(R.id.scontentsTextText);
        scontentsLocText = rootView.findViewById(R.id.scontentsLocText);

/*
        scontentsCommBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    saveComment();
                } else if() {
                    modifyComment();
                }
            }
        });
        scontentsScrapImg.setOnUserActItemClickListener(new View.OnUserActItemClickListener(){
            @Override
            public void onFlagClicked(View v) {
                if (listener != null) {
                    saveScrap();
                } else if() {
                    modifyScrap();
                }
            }

            public void onLikeClicked(View v) {
                if (listener != null) {
                    saveLike();
                } else if() {
                    modifyLike();
                }
            }

            public void onStarClicked(View v) {
                if (listener != null) {
                    saveStar();
            } else if() {
                    modifyStar();
                }
        })
*/

        db.collection("contents")
                .whereEqualTo("contentsType", 1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String contentsID = document.getId();
                                Log.d(TAG, document.getId() + " => " + contentsID);

                                loadContents(contentsID);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents : " , task.getException());
                        }
                    }
                });

    }

    private void loadContents(String contentsID) {

        DocumentReference docRef = db.collection("contents").document(contentsID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "자료 받아오기 성공 " );

                        Map<String, Object> ContentsList = new HashMap<>();
                        ContentsList = document.getData();
                        Log.d(TAG, "title확인" + ContentsList.get("title"));
                        setContents(ContentsList);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    private void setContents(Map<String, Object> contentsList) {
        Log.d(TAG, "title확인" + contentsList.get("title"));

     scontentsTitleText.setText(contentsList.get("title").toString());

     //scontentsBigImg.setImage(contentsList.get("titleImage").toString());
    // scontentsMentText.setText(contentsList.get("storyMent").toString());
     scontentsTextText.setText(contentsList.get("storyText").toString());
     scontentsLocText.setText(contentsList.get("storyAddress").toString());

    }
}
