package com.example.gapp_sapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gapp_sapp.Adapter.MessegeAdapter;
import com.example.gapp_sapp.Models.MessegeModel;
import com.example.gapp_sapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class MessageActivity extends AppCompatActivity {

    TextView Name;
    ImageView profilee, back, send;
    RecyclerView messagerv;
    EditText message;

    ArrayList<MessegeModel> Messagelist;

    RecyclerView MessageRv;
    MessegeAdapter adapter;

    String conversionId = null;

    FirebaseAuth mauth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ConversationActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        String receiverId = getIntent().getStringExtra("idd");
        String name = getIntent().getStringExtra("name");
        String profile = getIntent().getStringExtra("profile");
        Messagelist = new ArrayList<>();
        Name = findViewById(R.id.senderName);
        profilee = findViewById(R.id.senderImage);
        messagerv = findViewById(R.id.messageRv);
        message = findViewById(R.id.messageText);
        back = findViewById(R.id.back);
        send = findViewById(R.id.send);
        MessageRv = findViewById(R.id.messageRv);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ConversationActivity.class));
                finish();
            }
        });
        final String senderId = mauth.getUid();


//        Picasso.get().load(profile).placeholder(R.drawable.user_icon2).into(profilee);
        try{
            if (profile.isEmpty()) {
                profilee.setImageResource(R.drawable.user_icon2);
            }
            else{
                Picasso.get().load(profile).into(profilee);
            }
        }catch (Exception e){
            Log.d("Exception", "onCreate: "+e);
        }


        adapter = new MessegeAdapter(Messagelist, getApplicationContext());
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
//        layoutManager2.setStackFromEnd(true);
        messagerv.setLayoutManager(layoutManager2);
        messagerv.setNestedScrollingEnabled(false);
        messagerv.setHasFixedSize(false);
        messagerv.setAdapter(adapter);

        Name.setText(name);
        String SenderRoom = senderId + receiverId;
        String ReceiverRoom = receiverId + senderId;

        db.collection("Chats").document(SenderRoom).collection("Message").orderBy("timestamp")
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
                                adapter.notifyDataSetChanged();
                                MessageRv.smoothScrollToPosition(adapter.getItemCount()-1);
                                Log.d("count", "onEvent: count is zero");
                            }else{
                                adapter.notifyDataSetChanged();
//                                MessageRv.smoothScrollToPosition(adapter.getItemCount()-1);
                                MessageRv.smoothScrollToPosition(Messagelist.size()-1);
                            }
                        }
                        if (conversionId==null){
                            checkforConversion(receiverId);
                        }
                    }
                });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getApplicationContext().getColor(R.color.green));
        }
        SharedPreferences sp = getSharedPreferences("saved", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sp.edit();
        String Sendername = (sp.getString("authName", ""));
        String Senderpic = (sp.getString("authPic", ""));

        Log.d("saved", "onCreate: " + Sendername);
        Log.d("saved", "onCreate: " + Senderpic);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()) {
                    Toast.makeText(MessageActivity.this, "Message is Empty!", Toast.LENGTH_SHORT).show();
                } else {
                    String messagetext = message.getText().toString();
                    long time= new Date().getTime();
                    MessegeModel messegeModel = new MessegeModel(senderId, messagetext);
                    messegeModel.setTimestamp(new Date().getTime());
                    db.collection("Chats").document(SenderRoom).collection("Message").add(messegeModel).
                            addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    db.collection("Chats").document(ReceiverRoom).collection("Message").add(messegeModel);

                                }
                            });

                    if (conversionId!=null){
                        updateConversion(messagetext,time);
                    }
                    else{
                        HashMap<String,Object> conversion = new HashMap<>();
                        conversion.put("lastMessage",messagetext);
                        conversion.put("timestamp", new Date().getTime());
                        conversion.put("receiverid", receiverId);
                        conversion.put("receiverPic", profile);
                        conversion.put("receiverName", name);
                        conversion.put("senderid", senderId);
                        conversion.put("senderName", Sendername);
                        conversion.put("senderPic", Senderpic);
                        addCoversion(conversion);
                    }
                    message.setText("");
                }

            }

        });
    }

    private void addCoversion(HashMap<String,Object> conversion){
        Log.d("conversionid", "updateConversion: added");
        db.collection("Recent")
                .add(conversion).addOnSuccessListener(documentReference -> {
                    conversionId = documentReference.getId();

                });
    }

    private void checkforConversion(String receiverId){
        if(Messagelist.size()!=0){
            checkForConversionRemotely(
                    mauth.getUid(),receiverId);
            checkForConversionRemotely(receiverId,mauth.getUid());
        }
    }

    private void updateConversion(String message,long timestamp){
        Log.d("conversionid", "updateConversion: updated");
        DocumentReference documentReference = db.collection("Recent").document(conversionId);
        HashMap<String,Object> conversion = new HashMap<>();
        conversion.put("lastMessage",message);
        conversion.put("timestamp", timestamp);
        documentReference.update(conversion).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("result", "onSuccess: Document updated successfully");

            }
        });
    }

    private void checkForConversionRemotely(String senderid, String receiverid){
        db.collection("Recent").whereEqualTo("senderid",senderid)
                .whereEqualTo("receiverid",receiverid).get().addOnCompleteListener(conversionOncompletelistner);
    }

    private final OnCompleteListener<QuerySnapshot> conversionOncompletelistner = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size()>0){
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversionId = documentSnapshot.getId();
            Log.d("conversionid", ": "+conversionId);
        }

    };
}