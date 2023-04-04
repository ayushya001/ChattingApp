package com.example.gapp_sapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.gapp_sapp.Adapter.GroupParticipateAdapter;
import com.example.gapp_sapp.Adapter.listuserAdapter;
import com.example.gapp_sapp.Models.UserModel;
import com.example.gapp_sapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateGroup extends AppCompatActivity implements GroupParticipateAdapter.OnSelectedUserIdsListener {

    RecyclerView listrv;
    Button btn;
    ArrayList<UserModel> Userlist;

    List<String> selectedUserId = new ArrayList<>();
    GroupParticipateAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        SharedPreferences sp = getSharedPreferences("saved", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sp.edit();


        listrv = findViewById(R.id.listrv);
        btn = findViewById(R.id.button);
        ProgressDialog d = new ProgressDialog(this);
        d.setCanceledOnTouchOutside(false);
        d.setTitle("Creating");

        Userlist = new ArrayList<>();
        adapter = new GroupParticipateAdapter(this, Userlist,btn);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        listrv.setLayoutManager(layoutManager2);
        listrv.setNestedScrollingEnabled(false);
        listrv.setHasFixedSize(true);
        db.collection("Users").whereNotEqualTo("id", FirebaseAuth.getInstance().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        Userlist.add(dc.getDocument().toObject(UserModel.class));
                    }
                    listrv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }
        });

        adapter.setOnSelectedUserIdsListener(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUserId.isEmpty()){
                    Toast.makeText(CreateGroup.this, "Please select any one user", Toast.LENGTH_SHORT).show();

                }else{
                    selectedUserId.add(FirebaseAuth.getInstance().getUid());
                    d.show();
                    Map<String, Object> group = new HashMap<>();
                    group.put("userIds", selectedUserId);
//                    Toast.makeText(this, "s  "+selectedUserIds, Toast.LENGTH_SHORT).show();

                    CollectionReference groupsRef = db.collection("Groups");

                    groupsRef.add(group)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(CreateGroup.this, "Successfully created", Toast.LENGTH_SHORT).show();
                                    // Document added successfully
                                    String grpid = documentReference.getId();
                                    myEdit.putString("grpid", grpid);
                                    myEdit.commit();
                                    Log.d("groupid", "onSuccess: "+grpid);
                                    startActivity(new Intent(getApplicationContext(),GroupNamingActivity.class));
                                    finish();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (d.isShowing()){
                                        d.cancel();
                                    }
                                    Toast.makeText(CreateGroup.this, "Failed", Toast.LENGTH_SHORT).show();
                                    // Error adding document
                                }
                            });
                }
            }
        });
    }



    @Override
    public void onSelectedUserIds(ArrayList<String> selectedUserIds) {

        selectedUserId = selectedUserIds;
    }

}