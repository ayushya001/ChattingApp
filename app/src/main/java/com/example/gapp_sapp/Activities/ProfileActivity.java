package com.example.gapp_sapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.example.gapp_sapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView profile,add;
    TextView skipp;
    EditText bio;
    Button sumbit;
    Uri uri;
    ActivityResultLauncher<String> galleryLauncher;
    FirebaseStorage storage  = FirebaseStorage.getInstance();
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseAuth mauth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ProgressDialog d1 = new ProgressDialog(ProfileActivity.this);
        d1.setTitle("Uploading");
        d1.setCanceledOnTouchOutside(false);


        bio = findViewById(R.id.bio);
        sumbit = findViewById(R.id.button3);
        skipp = findViewById(R.id.Skipp);
        profile = findViewById(R.id.circleImageView);
        add = findViewById(R.id.add_pic);
        skipp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ConversationActivity.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryLauncher.launch("image/*");

            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                profile.setImageURI(result);
                uri =  Uri.parse(result.toString());
            }
        });

        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (uri != null && bio!=null) {

                    try{
                        d1.show();
                    }catch (Exception e){
                        Log.d("Excption", "onClick: "+e.toString());
                    }


                    StorageReference reference = storage.getReference().child("profilePhoto").child(new Date().getTime() + "");
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Map<String, Object> updateMap = new HashMap();
                                    updateMap.put("profilePhoto", uri);

                                    if (d1.isShowing()){
                                        d1.cancel();
                                    }

                                    Toast.makeText(getApplicationContext(), "uploaded successfully", Toast.LENGTH_SHORT).show();
                                    database.collection("Users").document(mauth.getUid()).update(updateMap);



                                    Intent intent = new Intent(ProfileActivity.this, ConversationActivity.class);
                                    startActivity(intent);



                                }
                            });

                        }
                    });

                }
                else{
//                    if (d1.isShowing()){
//                        d1.cancel();
//                    }
////
                    Toast.makeText(ProfileActivity.this, "Set your profile picture and bio", Toast.LENGTH_SHORT).show();
                }


            }
        });




    }
}