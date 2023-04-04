package com.example.gapp_sapp.Adapter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gapp_sapp.Activities.MessageActivity;
import com.example.gapp_sapp.Activities.UserlistActivity;
import com.example.gapp_sapp.Models.ConverstionModel;
import com.example.gapp_sapp.Models.UserModel;
import com.example.gapp_sapp.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class listuserAdapter extends RecyclerView.Adapter<listuserAdapter.holder> {

    Context context;
    ArrayList<UserModel> friendlist;

    public listuserAdapter(Context context, ArrayList<UserModel> friendlist) {
        this.context = context;
        this.friendlist = friendlist;
    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userlist_container,parent,false);
        return new listuserAdapter.holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        UserModel user = friendlist.get(position);
        holder.full_name.setText(user.getFullname());
        holder.email.setText(user.getEmail());
        Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.user_icon2).into(holder.profile);




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("idd",user.getId());
                intent.putExtra("name",user.getFullname());
                intent.putExtra("profile",user.getProfilePhoto());
//                intent.putExtra("sendername",)
                context.startActivity(intent);
//                ((Activity)v.getContext()).finish();
                ((UserlistActivity)context).finish();
            }
        });





    }

    @Override
    public int getItemCount() {
        return friendlist.size();
    }

    public static class holder extends RecyclerView.ViewHolder{
        TextView full_name,email;
        CircleImageView profile;
        public holder(@NonNull View itemView) {
            super(itemView);
            full_name = itemView.findViewById(R.id.fullName);
            email = itemView.findViewById(R.id.email);

            profile = itemView.findViewById(R.id.senderImage);
        }
    }
}
