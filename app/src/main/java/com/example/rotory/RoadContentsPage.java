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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.rotory.Adapter.RCommAdapter;
import com.example.rotory.Interface.OnContentsListener;
import com.example.rotory.Interface.OnDeleteListener;
import com.example.rotory.Interface.OnUserActItemClickListener;
import com.example.rotory.Interface.OnUserListener;
import com.example.rotory.VO.Comment;
import com.example.rotory.VO.Contents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RoadContentsPage extends Fragment {
    public static final int loginCode = 3000;
    final static String TAG = "RoadContentsPage";

    Button rcontentsCommBtn;
    ImageView rcontentsLinkImg;
    ImageView rcontentsScrapImg;
    ImageView rcontentsHeartImg;
    TextView rcontentsTitleText;
    ImageView rcontentsLevelImg;
    TextView rcontentsUsernameText;
    ImageView rcontentsStarImg;
    RecyclerView rCommRView;
    EditText rcontentsCommEdit;
    //RCommAdapter adapter;

    TextView rcontentsMapText;
    TextView rcontentsTakeTimeText;
    TextView rcontentsTakewhoText;

    ArrayList<Comment> commentArrayList;

    Context context;
    OnUserActItemClickListener listener;

    AppBarLayout appBarLayout;

    Boolean isSignIn = false;
    private FirestoreRecyclerAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.road_contents_page, container, false);

        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저 네임 : " + checkLogIN);
            isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
        }

    Query query = db.collection("SearchContents").whereEqualTo("contentsType", 1)
            .orderBy("contentsType", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Contents> options = new FirestoreRecyclerOptions.Builder<Contents>()
                .setQuery(query, Contents.class)
                .build();

     /*   adapter = new FirestoreRecyclerAdapter<SearchContents, roadcontentsViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, "어댑터 작동");
            }

            @Override
            protected void onBindViewHolder(@NonNull roadcontentsViewHolder holder, int position, @NonNull SearchContents model) {
                holder.setContentsItems(model);
            }

*//*            @NonNull
            @Override
            public roadcontentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.road_contents_page);
                return ;
            }
         };*//*

        rCommRView.setAdapter(adapter);

        }

        @Override
        protected void onStart(){
            super.onStart();
            adapter.startListening();
        }

        @Override
        protected void onStop() {
            super.onStop();
            if(adapter != null) {
                adapter.stopListening();
            }
        }


        //정보추가recyclerView
        public class roadcontentsViewHolder extends RecyclerView.ViewHolder{
            private View view;

            public roadcontentsViewHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView;
            }

            public void setContentsItems(SearchContents comment) {
                commLevelImg
                commUsernameText
                commConText
                commTimeText
                commReportText


            }
        }
*/
        initUI(rootView);
        return rootView;
    }


    private void initUI(ViewGroup rootView) {
        rcontentsCommBtn = rootView.findViewById(R.id.rcontentsCommBtn);
        rcontentsLinkImg = rootView.findViewById(R.id.rcontentsLinkImg);
        rcontentsScrapImg = rootView.findViewById(R.id.rcontentsScrapImg);
        rcontentsHeartImg = rootView.findViewById(R.id.rcontentsHeartImg);
        rcontentsTitleText = rootView.findViewById(R.id.rcontentsTitleText);
        rcontentsLevelImg = rootView.findViewById(R.id.rcontentsLevelImg);
        rcontentsUsernameText = rootView.findViewById(R.id.rcontentsUsernameText);
        rcontentsStarImg = rootView.findViewById(R.id.rcontentsStarImg);
        rcontentsCommEdit = rootView.findViewById(R.id.rcontentsCommEdit);
        rCommRView = rootView.findViewById(R.id.rCommRView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rCommRView.setLayoutManager(layoutManager);

      /*  adapter = new RCommAdapter();

        rCommRView.setAdapter(adapter);
*/
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getEmail();

        db.collection("contents")
                .whereEqualTo("contentsType", 0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String contentsID = document.getId();
                                Log.d(TAG, document.getId() + " => " + contentsID);

                                loadContents(contentsID);
                             /*   rcontentsHeartImg.setClickable(true);
                                rcontentsHeartImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getContext(), "좋아요 버튼 눌림", Toast.LENGTH_SHORT);
                                        listener.OnLikeClicked(contentsID,contentli userId);
                                    }
                                });*/
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
                            Log.d(TAG, "자료 받아오기 성공");
                            Map<String, Object> ContentsList = new HashMap<>();
                            ContentsList = document.getData();
                            setContents(ContentsList);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }

                private void setContents(Map<String, Object> contentsList) {
                }
            });
        }

        private void setContents(Map<String, Object> contentsList) {
            rcontentsTitleText.setText(contentsList.get("roadTitle").toString());
            rcontentsTakeTimeText.setText(contentsList.get("roadTaketime").toString());
            rcontentsTakewhoText.setText(contentsList.get("roadTakewho").toString());

        }

    }

//https://machine-woong.tistory.com/53 스피너