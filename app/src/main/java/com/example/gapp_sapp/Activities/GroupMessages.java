package com.example.gapp_sapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gapp_sapp.Adapter.MessegeAdapter;
import com.example.gapp_sapp.Fragment.GroupChatFragment;
import com.example.gapp_sapp.Models.MessegeModel;
import com.example.gapp_sapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GroupMessages extends AppCompatActivity {
    TextView Name;
    ImageView profilee, back, send;
    RecyclerView messagerv;
    EditText message;
    RecyclerView MessageRv;

    MessegeAdapter adapter;


    ArrayList<MessegeModel> Messagelist;
    String grpid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messages);
        grpid = getIntent().getStringExtra("grpidd");
        Messagelist = new ArrayList<>();
        Name = findViewById(R.id.senderName);
        profilee = findViewById(R.id.senderImage);
        messagerv = findViewById(R.id.messageRv);
        message = findViewById(R.id.messageText);
        back = findViewById(R.id.back);
        send = findViewById(R.id.send);
        MessageRv = findViewById(R.id.messageRv);
        GroupsetName();
        sendMessage();
        SetMessages();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getApplicationContext().getColor(R.color.green));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                startActivity(new Intent(this, GroupChatFragment.class));
            }
        });

    }

    private void SetMessages() {
        adapter = new MessegeAdapter(Messagelist, getApplicationContext());
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
//        layoutManager2.setStackFromEnd(true);
        messagerv.setLayoutManager(layoutManager2);
        messagerv.setNestedScrollingEnabled(false);
        messagerv.setHasFixedSize(false);
        messagerv.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("GroupMessages").document(grpid).collection("Messages").orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            return;
                        }
                        if (value !=null){
                            int count = Messagelist.size();
//                            MessageRv.smoothScrollToPosition(Messagelist.size()-1);
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    Messagelist.add(dc.getDocument().toObject(MessegeModel.class));
                                }
                            }
                            if (count == 0){
                                if (!value.isEmpty()){
                                    adapter.notifyDataSetChanged();
                                    MessageRv.smoothScrollToPosition(adapter.getItemCount()-1);
                                }

                                Log.d("count", "onEvent: count is zero");
                            }else{
                                adapter.notifyDataSetChanged();
                                MessageRv.smoothScrollToPosition(adapter.getItemCount()-1);
//                                MessageRv.smoothScrollToPosition(Messagelist.size()-1);
                            }
                        }

                    }
                });






    }

    private void sendMessage() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (message.getText().toString().isEmpty()) {
                    Toast.makeText(GroupMessages.this, "Message is Empty!", Toast.LENGTH_SHORT).show();
                } else {
                    String messagetext = message.getText().toString();
                    long time = new Date().getTime();
                    MessegeModel messegeModel = new MessegeModel(FirebaseAuth.getInstance().getUid(), messagetext);
                    messegeModel.setTimestamp(new Date().getTime());
                    FirebaseFirestore.getInstance().collection("GroupMessages").document(grpid).collection("Messages").add(messegeModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Map<String, Object> updateMap = new HashMap();
                                    updateMap.put("lastmessage",new Date().getTime());
                                    updateMap.put("lastConversation",messagetext);
                                    FirebaseFirestore.getInstance().collection("Groups").document(grpid)
                                            .update(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                    FirebaseFirestore.getInstance().collection("Groups").document(grpid)
                                            .update("lastmessage",time).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });

//                                    Toast.makeText(GroupMessages.this, "Message sent successfully", Toast.LENGTH_SHORT).show();

                                }
                            });
                    message.setText("");
                }

            }
        });
    }

    private void GroupsetName() {
        String grpname = getIntent().getStringExtra("grpname");
        String grpimage = getIntent().getStringExtra("grpimage");
        Picasso.get().load(grpimage).into(profilee);
        Name.setText(grpname);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}