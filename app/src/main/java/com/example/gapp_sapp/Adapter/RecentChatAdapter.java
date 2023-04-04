package com.example.gapp_sapp.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gapp_sapp.Activities.MessageActivity;
import com.example.gapp_sapp.Models.ConverstionModel;
import com.example.gapp_sapp.Models.UserModel;
import com.example.gapp_sapp.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.holder> {

    ArrayList<ConverstionModel> recentlist;
    Context context;

    public RecentChatAdapter(ArrayList<ConverstionModel> recentlist, Context context) {
        this.recentlist = recentlist;
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.converstaion_container,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        ConverstionModel model = recentlist.get(position);


        if (model.getSenderid().equals(FirebaseAuth.getInstance().getUid())) {
            holder.name.setText(model.getReceiverName());
            Picasso.get().load(model.getReceiverPic()).placeholder(R.drawable.user_icon2).into(holder.Profile);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("idd",model.getReceiverid());
                    intent.putExtra("name", model.getReceiverName());
                    intent.putExtra("profile",model.getReceiverPic());
                    context.startActivity(intent);
                }
            });
        }

        else if (model.getReceiverid().equals(FirebaseAuth.getInstance().getUid())) {
            holder.name.setText(model.getSenderName());
            if (model.getSenderPic().isEmpty()) {
                holder.Profile.setImageResource(R.drawable.user_icon2);
            } else{
                Picasso.get().load(model.getSenderPic()).into(holder.Profile);
            }
//            Picasso.get().load(model.getSenderPic()).placeholder(R.drawable.user_icon2).into(holder.Profile);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("idd",model.getSenderid());
                    intent.putExtra("name", model.getSenderName());
                    intent.putExtra("profile",model.getSenderPic());
                    context.startActivity(intent);
                }
            });
        }


        holder.lastMessage.setText(model.getLastMessage());
        String text = TimeAgo.using(model.getTimestamp());
        holder.lastTime.setText(text);
    }

    @Override
    public int getItemCount() {
        return recentlist.size();
    }

    public static class holder extends RecyclerView.ViewHolder{

        CircleImageView Profile;
        TextView name,lastMessage,lastTime;

        public holder(@NonNull View itemView) {
            super(itemView);
            Profile = itemView.findViewById(R.id.senderImage);
            name = itemView.findViewById(R.id.fullName);
            lastTime = itemView.findViewById(R.id.lastTime);
            lastMessage = itemView.findViewById(R.id.lastMessege);
        }
    }
}
