package com.example.rotory;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.VO.Contents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import javax.annotation.Nonnull;

public class MainPage extends Fragment {
    Button mainFloatingBtn;
    Button mainSearchBtn;
    EditText mainSearchEdit;

    RecyclerView mainRoadList;
    Button mainStoryNextBtn;
    RecyclerView mainStoryList;
    Button mainRoadNextBtn;

    FrameLayout mainMapLayout;
    Button mainMapExtendBtn;

    private FirestoreRecyclerAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static MainPage newInstance() {
        return new MainPage();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_page, container, false);
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {

        }
        if (user != null) {

            initUI(rootView);
        }
        return rootView;

    }


    // public void showWrite(){}

   /* public void onContentsListener (contentsAdapter.ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onContentsListener(holder, view, position);
        }
    }*/


    private void initUI(ViewGroup rootView) {
      /*  FirebaseUser user = mAuth.getCurrentUser();
=======
    private void initUI(ViewGroup rootView, FirebaseUser user) {

>>>>>>> 64f7814fea943e313351cd2769fbeca6e0da1a46
        db.collection("contents")
                .whereEqualTo("uid", user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String contentsId = document.getId();

                        Query query = db.collection("contents")
                                .document(contentsId).collection("title")
                                .orderBy("");

                        FirestoreRecyclerOptions<Contents> options = new FirestoreRecyclerOptions.Builder<Contents>()
                                .setQuery(query, Contents.class)
                                .build();
                        makeAdapter(options);
                    }
                }
            }
        });*/

        //mainRoadList.setAdapter(adapter);
    }

    private void makeAdapter(FirestoreRecyclerOptions<Contents> options) {
      /*  adapter = new FirestoreRecyclerOptions<SearchContents>(options) {

               @Override
                public void onDataChanged() {
                    super.onDataChanged();
                    Log.d(TAG, "어댑터 작동");
                }

                @Override
                protected void onBindViewHolder(@NonNull contentsViewHolder holder, int position,
                                                    @NonNull SearchContents model) {
                        holder.setUserItems(model);
                    }
                }
        }*/
    }

        public class contentsViewHolder extends RecyclerView.ViewHolder {
            private View view;

            public contentsViewHolder(@NonNull View itemView) {
                super(itemView);
            }

          /*  public contentsViewHolder(NonNull View itemView) {
                super(itemView);
                view = itemView;
            }*/
        }


      /*  MapView mapView = new MapView(getContext());
        ViewGroup mapViewContainer = (ViewGroup) rootView.findViewById(R.id.mainMapLayout);
        mapViewContainer.addView(mapView);

       */
        /*MapView mapView = new MapView(getContext());
        ViewGroup mapViewContainer = (ViewGroup) rootView.findViewById(R.id.mainMapLayout);
        mapViewContainer.addView(mapView);

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.541258, 126.838193), 2, true);*/
/*
  Button button = rootView.findViewById(R.id.mainFloatingBtn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { showWrite(); }
        });
        ImageButton mainMapExtendBtn = rootView.findViewById(R.id.mainMapExtendBtn);
        mainMapExtendBtn.bringToFront();
        mainMapExtendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), BigMapPage.class);
                startActivity(intent);
            }
        });

        ImageButton mainMapExtendBtn = rootView.findViewById(R.id.mainMapExtendBtn);
        mainMapExtendBtn.bringToFront();
        mainMapExtendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(BigMapPage.newInstance());
            }
        });
*/

        // 디버그 키 해시 구하기 (카카오 맵 API 연동 시 필요), 맵 코드 주석 처리 후 실행! (애뮬에서 돌리면 실행 오류 날 수 있음)
        /*try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo("com.example.rotory", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String str = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("Key Hash : ", str);
                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/

}






