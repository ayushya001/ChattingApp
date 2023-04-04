package com.example.gapp_sapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gapp_sapp.R;
import com.google.firebase.auth.FirebaseAuth;


public class OptionActivity extends AppCompatActivity {
    TextView logout,group;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        back = findViewById(R.id.back);
        logout = findViewById(R.id.logout);
        group = findViewById(R.id.group);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateGroup.class));
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getApplicationContext().getColor(R.color.green));
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("saved",MODE_PRIVATE);
               sp.edit().clear().commit();
//                SharedPreferences settings = context.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
//                settings.edit().clear().commit();
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(getApplicationContext() , SigninActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
    }
}