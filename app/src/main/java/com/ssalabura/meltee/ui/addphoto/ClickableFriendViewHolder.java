package com.ssalabura.meltee.ui.addphoto;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;

public class ClickableFriendViewHolder extends RecyclerView.ViewHolder {
    public CardView cardView;
    public TextView friend;
    public boolean selected;

    public ClickableFriendViewHolder(@NonNull View itemView) {
        super(itemView);

        cardView = (CardView)itemView;
        friend = itemView.findViewById(R.id.textView_friend);
    }

    public void click() {
        if(selected) {
            cardView.setCardBackgroundColor(0xFFEAE5E5);
            selected = false;
        } else {
            cardView.setCardBackgroundColor(Color.WHITE);
            selected = true;
        }
    }
}
