package com.ssalabura.meltee.ui.addphoto;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClickableFriendViewHolder extends RecyclerView.ViewHolder {
    public CardView cardView;
    public CircleImageView profilePicture;
    public TextView username;
    public boolean selected;

    public ClickableFriendViewHolder(@NonNull View itemView) {
        super(itemView);

        cardView = (CardView)itemView;
        profilePicture = itemView.findViewById(R.id.profilePicture);
        username = itemView.findViewById(R.id.textView_username);
    }

    public void click() {
        if(selected) {
            cardView.setCardBackgroundColor(0xFFEAE5E5);
            selected = false;
        } else {
            cardView.setCardBackgroundColor(0xFFA5A0A0);
            selected = true;
        }
    }
}
