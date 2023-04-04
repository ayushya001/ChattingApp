package com.example.gapp_sapp.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gapp_sapp.Models.GroupModel;
import com.example.gapp_sapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupNamingActivity extends AppCompatActivity {
    CircleImageView grpphoto,add;
    EditText grpname;

    ActivityResultLauncher<String> galleryLauncher;

    FirebaseStorage storage= FirebaseStorage.getInstance();

    Uri uri;
    Button sbmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_naming);
        grpphoto = findViewById(R.id.circleImageView);
        add = findViewById(R.id.add_pic);
        grpname = findViewById(R.id.grpname);
        sbmt = findViewById(R.id.sbmt);
        SharedPreferences sp = getSharedPreferences("saved", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sp.edit();
        String grpid= sp.getString("grpid","");
        ProgressDialog d = new ProgressDialog(this);
        d.setTitle("Uploading");
        d.setCanceledOnTouchOutside(false);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryLauncher.launch("image/*");

            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                grpphoto.setImageURI(result);
                uri =  Uri.parse(result.toString());
            }
        });

        sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri!=null && grpname!=null){
                    d.show();

                    StorageReference reference = storage.getReference().child("GroupPhoto").child(new Date().getTime() + "");
                    reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
//                                    GroupModel model = new GroupModel();
//                                    model.setGroupid(grpid);
//                                    model.setGroupName(grpname.getText().toString());
//                                    model.setGroupImage(String.valueOf(uri));
                                    Map<String, Object> updateMap = new HashMap();
                                    updateMap.put("groupImage", uri);
                                    updateMap.put("groupid", grpid);
                                    updateMap.put("groupid", grpid);
                                    updateMap.put("lastmessage", new Date().getTime());
                                    updateMap.put("groupName",grpname.getText().toString().trim());
                                    FirebaseFirestore.getInstance().collection("Groups")
                                            .document(grpid).update(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(GroupNamingActivity.this, "success", Toast.LENGTH_SHORT).show();
                                                    Intent intent  = new Intent(GroupNamingActivity.this,GroupMessages.class);
                                                    intent.putExtra("grpidd",grpid);
                                                    intent.putExtra("grpname",grpname.getText().toString().trim());
                                                    intent.putExtra("grpimage",uri);
                                                    startActivity(intent);
                                                    finish();
                                                    if (d.isShowing()){
                                                        d.cancel();
                                                    }

                                                }
                                            });
                                }
                            });
                        }
                    });




                }
                else{
                    Toast.makeText(GroupNamingActivity.this, "Please set Group Name and Photo", Toast.LENGTH_SHORT).show();
                }

            }
        });


//        FirebaseFirestore.getInstance().collection("Groups").whereArrayContains("userIds", FirebaseAuth.getInstance().getUid())
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.w("grp", "Listen failed.", error);
//                            return;
//                        }
//
//                        for (QueryDocumentSnapshot document : value) {
//                            Toast.makeText(GroupNamingActivity.this, "yes", Toast.LENGTH_SHORT).show();
//                            Log.d("grp", document.getId() + " => " + document.getData());
//                        }
//                    }
//                });







    }
}