package com.example.rotory.story;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rotory.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class SearchOnMyRoadFragment extends Fragment {
    private static final String TAG = "SearchOnMyRoadFragment";

    Context context;
    TextView textView;
    RecyclerView recyclerView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter adapter;


    public SearchOnMyRoadFragment() {}

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search_on_my_road, container, false);

        initUI(rootView);

        return rootView;
    }

    private void initUI(ViewGroup rootView) {

        textView = rootView.findViewById(R.id.textView3);
        recyclerView = rootView.findViewById(R.id.writeStoryMyRoadsSearchRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        db.collection("contents")
                .orderBy("writeDate", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult());

                            Query query = db.collection("contents")
                                    .whereEqualTo("contentsType", 0);

                            FirestoreRecyclerOptions<MyRoad> options = new FirestoreRecyclerOptions.Builder<MyRoad>()
                                    .setQuery(query, MyRoad.class)
                                    .build();
                            setAdapter(options);
                            adapter.startListening();
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }

    public void setAdapter(FirestoreRecyclerOptions options) {
        adapter = new FirestoreRecyclerAdapter<MyRoad, MyRoadViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull MyRoadViewHolder holder, int position, @NonNull MyRoad model) {
                holder.setMyRoadItems(model);
            }

            @NonNull
            @Override
            public MyRoadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_myroad_search_item, parent, false);

                return new MyRoadViewHolder(view);
            }
        };
    }

    private class MyRoadViewHolder extends RecyclerView.ViewHolder {
        View view;

        TextView dtrName = itemView.findViewById(R.id.myRoadSearchItemDtrTitle);
        TextView title = itemView.findViewById(R.id.myRoadSearchItemRoadTitle);
        TextView address = itemView.findViewById(R.id.myRoadSearchItemAddress);


        public MyRoadViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), dtrName.getText(), Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(getActivity(), WriteRoadPage.class);
                    intent.putExtra("dtrName", dtrName.getText());
                    startActivityForResult(intent, 0);*/
                }
            });
        }

        public void setMyRoadItems(MyRoad items) {

            dtrName.setText(items.getDtrName());
            title.setText(items.getTitle());
            address.setText(items.getAddress());
        }
    }


    @Override
    public void onStart() {
        Log.d(TAG, "어댑터 작동 시작");
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }
}