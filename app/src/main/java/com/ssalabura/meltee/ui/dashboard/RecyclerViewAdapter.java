package com.ssalabura.meltee.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;
import com.ssalabura.meltee.util.BitmapTools;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<PhotoCardViewHolder> {

    private List<PhotoCard> photoCardList;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public RecyclerViewAdapter(Context context, List<PhotoCard> photoCardList) {
        this.context = context;
        this.photoCardList = photoCardList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PhotoCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View photoCardViewItem = mLayoutInflater.inflate(R.layout.cardview_item_layout, parent, false);
        return new PhotoCardViewHolder(photoCardViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoCardViewHolder holder, int position) {
        PhotoCard photoCard = this.photoCardList.get(position);

        holder.photo.setImageBitmap(BitmapTools.fromFile(photoCard.imagePath));
        holder.text.setText(photoCard.text);
    }

    @Override
    public int getItemCount() {
        return this.photoCardList.size();
    }
}
