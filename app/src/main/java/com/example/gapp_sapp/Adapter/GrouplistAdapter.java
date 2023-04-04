package com.example.gapp_sapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gapp_sapp.Activities.GroupMessages;
import com.example.gapp_sapp.Activities.MessageActivity;
import com.example.gapp_sapp.Models.GroupModel;
import com.example.gapp_sapp.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GrouplistAdapter extends  RecyclerView.Adapter<GrouplistAdapter.holder> {
    Context context;
    ArrayList<GroupModel> grplist;

    public GrouplistAdapter(Context context, ArrayList<GroupModel> grplist) {
        this.context = context;
        this.grplist = grplist;
    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.groupchat_container,parent,false);
        return new GrouplistAdapter.holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        GroupModel model = grplist.get(position);
        holder.grpName.setText(model.getGroupName());
//        holder.lastMessage.setText(model.getLastmessage());
        holder.lastMessage.setText(model.getLastConversation());
        String text = TimeAgo.using(model.getLastmessage());
//        holder.lastMessage.setText(text);

        Picasso.get().load(model.getGroupImage()).into(holder.grpimage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupMessages.class);
                intent.putExtra("grpidd",model.getGroupid());
                intent.putExtra("grpname",model.getGroupName());
                intent.putExtra("grpimage",model.getGroupImage());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return grplist.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        CircleImageView grpimage;
        TextView grpName,lastMessage;
        TextView time;
        public holder(@NonNull View itemView) {
            super(itemView);
            grpimage = itemView.findViewById(R.id.group_image);
            grpName = itemView.findViewById(R.id.groupName);
            lastMessage = itemView.findViewById(R.id.lastMessege);
            time = itemView.findViewById(R.id.lastTime);
        }
    }
}
