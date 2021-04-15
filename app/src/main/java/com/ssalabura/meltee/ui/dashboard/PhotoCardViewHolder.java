package com.ssalabura.meltee.ui.dashboard;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;

public class PhotoCardViewHolder extends RecyclerView.ViewHolder {
    public TextView sender;
    public TextView timestamp;
    public ImageView photo;
    public TextView message;

    public PhotoCardViewHolder(@NonNull View itemView) {
        super(itemView);

        this.sender = itemView.findViewById(R.id.textView_sender);
        this.timestamp = itemView.findViewById(R.id.textView_timestamp);
        this.photo = itemView.findViewById(R.id.imageView);
        this.message = itemView.findViewById(R.id.textView_message);
    }
}
