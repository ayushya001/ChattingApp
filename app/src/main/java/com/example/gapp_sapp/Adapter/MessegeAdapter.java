package com.example.gapp_sapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gapp_sapp.Models.MessegeModel;
import com.example.gapp_sapp.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessegeAdapter extends RecyclerView.Adapter {


    ArrayList<MessegeModel> arrayList;
    Context context;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public MessegeAdapter(ArrayList<MessegeModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new SenderHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout,parent,false);
            return new RecieverHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessegeModel messegeModel = arrayList.get(position);

        if (holder.getClass()==SenderHolder.class){
            ((SenderHolder)holder).sendermsg.setText(messegeModel.getMessege());
            String text = TimeAgo.using(messegeModel.getTimestamp());
            ((SenderHolder) holder).senderTime.setText(text);
        }else {
            ((RecieverHolder)holder).recieverMsg.setText(messegeModel.getMessege());
            String text = TimeAgo.using(messegeModel.getTimestamp());
            ((RecieverHolder) holder).receiverTime.setText(text);
        }



    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else{
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecieverHolder extends RecyclerView.ViewHolder{
        TextView recieverMsg,receiverTime;

        public RecieverHolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg = itemView.findViewById(R.id.reciverText);
            receiverTime = itemView.findViewById(R.id.reciverTime);
        }
    }
    public class SenderHolder extends RecyclerView.ViewHolder{
        TextView sendermsg,senderTime;

        public SenderHolder(@NonNull View itemView) {
            super(itemView);
            sendermsg = itemView.findViewById(R.id.Sendertext);
            senderTime = itemView.findViewById(R.id.SenderTime);

        }
    }
}
