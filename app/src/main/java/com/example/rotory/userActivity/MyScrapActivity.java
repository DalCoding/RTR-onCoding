package com.example.rotory.userActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;
import com.example.rotory.VO.Person;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

public class MyScrapActivity extends AppCompatActivity {
    private static final String TAG = "MyScrapActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    RecyclerView myScrapRecyclerView;
    String userId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.my_scrap_page);

        FirebaseUser user = mAuth.getCurrentUser();

        myScrapRecyclerView = findViewById(R.id.myScrabRecyclerView);
        myScrapRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (user != null){
            userId  = user.getEmail();
            //loadScrapItem()
        }else {
            Toast.makeText(getApplicationContext(), "사용자 인식 오류", Toast.LENGTH_SHORT);
        }

      db.collection("person")
              .whereEqualTo("userId", userId)
              .get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if (task.isSuccessful()){
                          for (QueryDocumentSnapshot pDocument : task.getResult()) {
                              Query query = db.collection("person")
                                      .document(pDocument.getId()).collection("myScrap")
                                      .orderBy("savedDate", Query.Direction.ASCENDING);

                             // FirestoreRecyclerOptions<Person> options = new FirestoreRecyclerOptions<>()

                          }
                      }

                  }
              });
    }


}
