package com.example.gapp_sapp.Adapter;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gapp_sapp.Models.UserModel;
import com.example.gapp_sapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupParticipateAdapter extends RecyclerView.Adapter<GroupParticipateAdapter.holder> {

    private ArrayList<String> selectedUserIds = new ArrayList<>();
    private Button btn;
    private Context context;
    private ArrayList<UserModel> friendlist;
    private OnSelectedUserIdsListener listener;

    public GroupParticipateAdapter(Context context, ArrayList<UserModel> friendlist, Button btn) {
        this.context = context;
        this.friendlist = friendlist;
        this.btn = btn;
    }

    public interface OnSelectedUserIdsListener {
        void onSelectedUserIds(ArrayList<String> selectedUserIds);
    }

    public void setOnSelectedUserIdsListener(OnSelectedUserIdsListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grouplist,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        UserModel user = friendlist.get(position);
        holder.name.setText(user.getFullname());
        holder.email.setText(user.getEmail());
        Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.user_icon2).into(holder.profile);
        if (selectedUserIds.contains(user.getId())) {
            holder.blank.setBackgroundResource(R.color.green);
        } else {
            holder.blank.setBackgroundResource(R.drawable.baseline_check_box_outline_blank_24);
        }
        holder.blank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUserIds.contains(user.getId())) {
                    selectedUserIds.remove(user.getId());
                    holder.blank.setBackgroundResource(R.drawable.baseline_check_box_outline_blank_24);

                } else {
                    selectedUserIds.add(user.getId());

                    holder.blank.setBackgroundResource(R.color.green);

                }
                updateButtonState();
                if (listener != null) {
                    listener.onSelectedUserIds(selectedUserIds);
                }
            }
        });
        updateButtonState();
    }

    private void updateButtonState() {
        if (selectedUserIds.isEmpty()) {
            btn.setVisibility(View.GONE);
        } else {
            btn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return friendlist.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        ImageView blank;
        CircleImageView profile;
        TextView name,email;

        public holder(@NonNull View itemView) {
            super(itemView);
            blank = itemView.findViewById(R.id.selectImg);
            profile = itemView.findViewById(R.id.senderImage);
            name = itemView.findViewById(R.id.fullName);
            email = itemView.findViewById(R.id.email);
        }
    }

}
