package com.example.gapp_sapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.gapp_sapp.Adapter.listuserAdapter;
import com.example.gapp_sapp.Models.UserModel;
import com.example.gapp_sapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserlistActivity extends AppCompatActivity {

    RecyclerView listrv;

    listuserAdapter adapter;
    ArrayList<UserModel> Userlist;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        listrv = findViewById(R.id.listrv);

        Userlist = new ArrayList<>();
        adapter= new listuserAdapter(this,Userlist);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        listrv.setLayoutManager(layoutManager2);
        listrv.setNestedScrollingEnabled(false);
        listrv.setHasFixedSize(true);
        db.collection("Users").whereNotEqualTo("id", FirebaseAuth.getInstance().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    return;
                }

                for (DocumentChange dc: value.getDocumentChanges()){

                    if (dc.getType()==DocumentChange.Type.ADDED){
                        Userlist.add(dc.getDocument().toObject(UserModel.class));
                    }
                    listrv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }
}