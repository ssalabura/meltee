package com.ssalabura.meltee.ui.friends;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendViewHolder extends RecyclerView.ViewHolder {
    public Button button_remove;
    public CircleImageView profilePicture;
    public TextView username;
    public TextView lastPhotoTimestamp;

    public FriendViewHolder(@NonNull View itemView) {
        super(itemView);

        button_remove = itemView.findViewById(R.id.button_remove);
        profilePicture = itemView.findViewById(R.id.profilePicture);
        username = itemView.findViewById(R.id.textView_username);
        lastPhotoTimestamp = itemView.findViewById(R.id.textView_lastPhotoTimestamp);
    }
}
