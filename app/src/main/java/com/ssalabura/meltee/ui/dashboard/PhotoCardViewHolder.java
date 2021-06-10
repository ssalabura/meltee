package com.ssalabura.meltee.ui.dashboard;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PhotoCardViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView profilePicture;
    public TextView sender;
    public TextView timestamp;
    public ImageView photo;
    public TextView message;
    public TextView location;

    public PhotoCardViewHolder(@NonNull View itemView) {
        super(itemView);

        this.profilePicture = itemView.findViewById(R.id.profilePicture);
        this.sender = itemView.findViewById(R.id.textView_sender);
        this.timestamp = itemView.findViewById(R.id.textView_timestamp);
        this.photo = itemView.findViewById(R.id.imageView);
        this.message = itemView.findViewById(R.id.textView_message);
        this.location = itemView.findViewById(R.id.textView_location);
    }
}
