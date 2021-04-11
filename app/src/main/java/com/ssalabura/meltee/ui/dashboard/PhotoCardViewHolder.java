package com.ssalabura.meltee.ui.dashboard;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;

public class PhotoCardViewHolder extends RecyclerView.ViewHolder {
    TextView sender;
    ImageView photo;
    TextView text;

    PhotoCardViewHolder(@NonNull View itemView) {
        super(itemView);

        this.sender = itemView.findViewById(R.id.textView_sender);
        this.photo = itemView.findViewById(R.id.imageView);
        this.text = itemView.findViewById(R.id.textView_text);
    }
}
