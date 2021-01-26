//package com.example.rotory.Contents;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.rotory.Comment;
//import com.example.rotory.R;
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class CommentAdapter extends FirestoreRecyclerAdapter<Comment, StoryContentsPage.CommentViewHolder> {
//    private final String TAG = "CommentAdapter";
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    View view;
//    String contents;
//    /**
//     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
//     * FirestoreRecyclerOptions} for configuration options.
//     *
//     * @param options
//     */
//    public CommentAdapter(@NonNull FirestoreRecyclerOptions options, String contentsId) {
//        super(options);
//        contents = contentsId;
//    }
//
//    @Override
//    public void onDataChanged() {
//        super.onDataChanged();
//        notifyDataSetChanged();
//        Log.d(TAG, " 어댑터 작동");
//    }
//
//
//    @NonNull
//    @Override
//    public StoryContentsPage.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
//        return new StoryContentsPage.CommentViewHolder(view);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull StoryContentsPage.CommentViewHolder holder, int position, @NonNull Comment model) {
//        Log.d(TAG, "onBindViewHolder 작동" + holder.itemView.toString() + ":" + contents);
//       /* db.collection("contents").document(contents).collection("comment")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            int count = 0;
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                holder.setCommentItems(model);
//                                count ++;
//                            }
//                            Log.d(TAG, "데이터 불러오기 고쳐봄  개수 ?" + count);
//                        }
//                    }
//                });*/
//        holder.setCommentItems(model);
//    }
//
//    public View getItemView(){
//        return view;
//    }
//
//
//
//}
//
//
