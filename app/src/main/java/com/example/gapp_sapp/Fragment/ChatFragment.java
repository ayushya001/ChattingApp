package com.example.gapp_sapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.gapp_sapp.Adapter.RecentChatAdapter;
import com.example.gapp_sapp.Adapter.listuserAdapter;

import com.example.gapp_sapp.Models.ConverstionModel;
import com.example.gapp_sapp.Models.MessegeModel;
import com.example.gapp_sapp.Models.UserModel;
import com.example.gapp_sapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ChatFragment extends Fragment {

    RecentChatAdapter adapter;
    ArrayList<ConverstionModel> recentlist;

    RecyclerView chatrv;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        chatrv = view.findViewById(R.id.chatrv);

        recentlist = new ArrayList<>();
        adapter = new RecentChatAdapter(recentlist, getContext());
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        chatrv.setLayoutManager(layoutManager2);
        chatrv.setNestedScrollingEnabled(false);
        chatrv.setHasFixedSize(true);
        chatrv.setAdapter(adapter);
        listenConversation();

        return view;
    }



        private void listenConversation() {
            db.collection("Recent")
                    .whereEqualTo("senderid", auth.getUid())
                    .addSnapshotListener(eventListener);

            db.collection("Recent")
                    .whereEqualTo("receiverid", auth.getUid())
                    .addSnapshotListener(eventListener);

            // Add a new query that checks for conversations where the current user is the receiver
//            db.collection("Recent")
//                    .whereEqualTo("receiverid", auth.getUid())
//                    .orderBy("timestamp", Query.Direction.DESCENDING)
//                    .addSnapshotListener(eventListener);
        }
//

//    //
private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
    if (error != null) {
        return;
    }
    for (DocumentChange dc : value.getDocumentChanges()) {
        if (dc.getType() == DocumentChange.Type.ADDED) {
            recentlist.add(dc.getDocument().toObject(ConverstionModel.class));
        } else if (dc.getType() == DocumentChange.Type.MODIFIED) {
            ConverstionModel modifiedConversation = dc.getDocument().toObject(ConverstionModel.class);
            // Find the existing conversation in the list and update its properties
            for (int i = 0; i < recentlist.size(); i++) {
                ConverstionModel conversation = recentlist.get(i);
                String senderid = dc.getDocument().getString("senderid");
                String receiverid = dc.getDocument().getString("receiverid");
                if (conversation.getSenderid().equals(senderid) && conversation.getReceiverid().equals(receiverid)
                        || conversation.getSenderid().equals(receiverid) && conversation.getReceiverid().equals(senderid)) {
                    conversation.setLastMessage(modifiedConversation.getLastMessage());
                    conversation.setTimestamp(modifiedConversation.getTimestamp());
                    break;
                }
            }
        }
    }
        Collections.sort(recentlist, (obj1, obj2) -> Long.compare(obj2.getTimestamp(), obj1.getTimestamp()));
        adapter.notifyDataSetChanged();
};


};







