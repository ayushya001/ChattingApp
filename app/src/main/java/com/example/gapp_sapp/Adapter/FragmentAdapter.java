package com.example.gapp_sapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gapp_sapp.Fragment.ChatFragment;
import com.example.gapp_sapp.Fragment.GroupChatFragment;


public class FragmentAdapter extends FragmentStateAdapter  {
    private  String[] tabTitles = new String[]{"CHATS","GROUP CHATS"};

    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ChatFragment();

            case 1:
                return new GroupChatFragment();
        }
        return new ChatFragment();
    }

    @Override
    public int getItemCount() {
        return tabTitles.length;
    }
}
