package com.ssalabura.meltee.ui.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;

import java.util.List;

public class FriendViewAdapter extends RecyclerView.Adapter<FriendViewHolder> {
    private FriendsFragment fragment;
    private LayoutInflater inflater;
    private List<String> friendsList;

    public FriendViewAdapter(Context context, FriendsFragment fragment, List<String> friendsList) {
        this.fragment = fragment;
        this.inflater = LayoutInflater.from(context);
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View friendViewItem = inflater.inflate(R.layout.cardview_friend, parent, false);
        return new FriendViewHolder(friendViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        String friend = friendsList.get(position);

        holder.button_remove.setOnClickListener(v -> fragment.removeFriend(friend));
        holder.friend.setText(friend);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }
}
