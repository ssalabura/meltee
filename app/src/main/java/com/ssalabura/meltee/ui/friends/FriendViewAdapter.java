package com.ssalabura.meltee.ui.friends;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;
import com.ssalabura.meltee.database.Friend;

import java.util.List;

public class FriendViewAdapter extends RecyclerView.Adapter<FriendViewHolder> {
    private FriendsFragment fragment;
    private LayoutInflater inflater;
    private List<Friend> friendsList;

    public FriendViewAdapter(Context context, FriendsFragment fragment, List<Friend> friendsList) {
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
        Friend friend = friendsList.get(position);

        holder.button_remove.setOnClickListener(v -> fragment.removeFriend(friend.username));
        holder.username.setText(friend.username);
        if(friend.lastPhotoTimestamp != 0) {
            holder.lastPhotoTimestamp.setText(DateUtils.getRelativeTimeSpanString(friend.lastPhotoTimestamp));
        } else {
            holder.lastPhotoTimestamp.setText(R.string.never);
        }
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }
}
