package com.ssalabura.meltee.ui.addphoto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;

import java.util.ArrayList;
import java.util.List;

public class ClickableFriendViewAdapter extends RecyclerView.Adapter<ClickableFriendViewHolder> {
    private LayoutInflater inflater;
    private List<String> friendsList;
    private List<String> selectedFriends;

    public ClickableFriendViewAdapter(Context context, List<String> friendsList) {
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
        String friendUsername = friendsList.get(position);
        holder.cardView.setOnClickListener(v -> {
            holder.click();
            if(holder.selected) {
                selectedFriends.add(friendUsername);
            } else {
                selectedFriends.remove(friendUsername);
            }
        });
        holder.friend.setText(friendsList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public List<String> getSelected() {
        return selectedFriends;
    }
}