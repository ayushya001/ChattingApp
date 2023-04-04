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

import com.example.gapp_sapp.Adapter.GrouplistAdapter;
import com.example.gapp_sapp.Adapter.RecentChatAdapter;
import com.example.gapp_sapp.Models.ConverstionModel;
import com.example.gapp_sapp.Models.GroupModel;
import com.example.gapp_sapp.Models.UserModel;
import com.example.gapp_sapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class GroupChatFragment extends Fragment {
    GrouplistAdapter adapter;
    RecyclerView rv;
    ArrayList<GroupModel> list;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);




        rv = view.findViewById(R.id.grprv);
        list = new ArrayList<>();
        adapter = new GrouplistAdapter(getContext(), list);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager2);
        rv.setNestedScrollingEnabled(false);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        db.collection("Groups").whereArrayContains("userIds", FirebaseAuth.getInstance().getUid())
                .orderBy("lastmessage", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
//                        list.clear();

                        list.clear();
                        for (DocumentSnapshot document : value.getDocuments()) {
                            GroupModel group = document.toObject(GroupModel.class);
                            list.add(group);
                        }

                        // Sort the list based on the lastmessage field
                        Collections.sort(list, new Comparator<GroupModel>() {
                            @Override
                            public int compare(GroupModel o1, GroupModel o2) {
                                return Long.compare(o2.getLastmessage(), o1.getLastmessage());
                            }
                        });

                        adapter.notifyDataSetChanged();
                    }
                });

//        db.collection("Groups").whereArrayContains("userIds", FirebaseAuth.getInstance().getUid())
//                .orderBy("lastmessage", Query.Direction.DESCENDING)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        list.clear();
//                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                            list.add(document.toObject(GroupModel.class));
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//                });


        //        FirebaseFirestore.getInstance().collection("Groups").whereArrayContains("userIds", FirebaseAuth.getInstance().getUid())
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.w("grp", "Listen failed.", error);
//                            return;
//                        }
//
//        for (QueryDocumentSnapshot document : value) {
//            System.out.println("grp " + document.getMetadata().isFromCache());
////                            Toast.makeText(GroupNamingActivity.this, "yes", Toast.LENGTH_SHORT).show();
//            Log.d("grp", document.getId() + " => " + document.getData());
//        }
//

//                    }
//                });
        return view;
    }
}

// else if (dc.getType() == DocumentChange.Type.MODIFIED) {
//         Toast.makeText(getContext(), "Modified", Toast.LENGTH_SHORT).show();
//         }