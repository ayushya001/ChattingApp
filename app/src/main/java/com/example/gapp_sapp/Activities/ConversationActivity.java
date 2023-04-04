package com.example.gapp_sapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import com.example.gapp_sapp.Adapter.FragmentAdapter;
import com.example.gapp_sapp.Adapter.listuserAdapter;
import com.example.gapp_sapp.Models.ConverstionModel;
import com.example.gapp_sapp.Models.UserModel;
import com.example.gapp_sapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationActivity extends AppCompatActivity {
    private static  final int TIME_INTERVAL = 2000;
    private long backPressed;
    FrameLayout list;
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    ImageView options;




    

    FragmentAdapter fragmentAdapter;
    private  String[] tabTitles = new String[]{"CHATS","GROUP CHATS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);

        viewPager2 = findViewById(R.id.View_pager);
        tabLayout =findViewById(R.id.tab_Layout);
        options = findViewById(R.id.settings);
        list = findViewById(R.id.listframe);
        CircleImageView userpic =findViewById(R.id.userProfile);
        TextView userName =findViewById(R.id.username);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UserlistActivity.class));
            }
        });
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),OptionActivity.class));
            }
        });
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                UserModel users = document.toObject(UserModel.class);
                                userName.setText(users.getFullname());
                                Picasso.get().load(users.getProfilePhoto()).placeholder(R.drawable.user_icon2).into(userpic);

                                SharedPreferences sp = getSharedPreferences("saved",MODE_PRIVATE);

                                SharedPreferences.Editor myEdit = sp.edit();
                                myEdit.putString("authName", users.getFullname());
                                myEdit.putString("authPic", users.getProfilePhoto());


                                myEdit.commit();
                            }
                        } else {
                            Log.d("failure", "get failed with ", task.getException());
                        }

                    }
                });




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getApplicationContext().getColor(R.color.green));
        }

        fragmentAdapter = new FragmentAdapter(this);
        viewPager2.setAdapter(fragmentAdapter);

        new TabLayoutMediator(tabLayout,viewPager2,((tab, position) -> tab.setText(tabTitles[position]))).attach();

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),OptionActivity.class));

            }
        });


    }
    @Override
    public void onBackPressed() {
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            finishAffinity();
        }
        else {
            Toast.makeText(this, "Press again to exit the app", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();

    }
}