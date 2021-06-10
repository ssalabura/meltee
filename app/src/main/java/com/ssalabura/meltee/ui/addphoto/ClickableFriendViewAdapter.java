package com.ssalabura.meltee.ui.addphoto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;
import com.ssalabura.meltee.database.Friend;

import java.util.ArrayList;
import java.util.List;

public class ClickableFriendViewAdapter extends RecyclerView.Adapter<ClickableFriendViewHolder> {
    private LayoutInflater inflater;
    private List<Friend> friendsList;
    private List<String> selectedFriends;

    public ClickableFriendViewAdapter(Context context, List<Friend> friendsList) {
        this.inflater = LayoutInflater.from(context);
        this.friendsList = friendsList;
        selectedFriends = new ArrayList<>();
    }

    @NonNull
    @Override
    public ClickableFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View friendViewItem = inflater.inflate(R.layout.cardview_friend_clickable, parent, false);
        return new ClickableFriendViewHolder(friendViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ClickableFriendViewHolder holder, int position) {
        Friend friend = friendsList.get(position);
        holder.cardView.setOnClickListener(v -> {
            holder.click();
            if(holder.selected) {
                selectedFriends.add(friend.username);
            } else {
                selectedFriends.remove(friend.username);
            }
        });
        if(friend.profilePicture != null) {
            holder.profilePicture.setImageBitmap(friend.profilePicture);
        }
        holder.username.setText(friend.username);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public List<String> getSelected() {
        return selectedFriends;
    }
}