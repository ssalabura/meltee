package com.ssalabura.meltee.ui.dashboard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;
import com.ssalabura.meltee.database.PhotoCard;
import com.ssalabura.meltee.util.BitmapTools;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PhotoCardViewAdapter extends RecyclerView.Adapter<PhotoCardViewHolder> {
    private LayoutInflater inflater;
    private List<PhotoCard> photoCardList;

    public PhotoCardViewAdapter(Context context, List<PhotoCard> photoCardList) {
        this.inflater = LayoutInflater.from(context);
        this.photoCardList = photoCardList;
    }

    @NonNull
    @Override
    public PhotoCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View photoCardViewItem = inflater.inflate(R.layout.cardview_photocard, parent, false);
        return new PhotoCardViewHolder(photoCardViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoCardViewHolder holder, int position) {
        PhotoCard photoCard = this.photoCardList.get(position);

        holder.sender.setText(photoCard.sender);
        holder.timestamp.setText(new SimpleDateFormat("KK:mm aa", Locale.ENGLISH).format(photoCard.timestamp));
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        holder.photo.setImageBitmap(Bitmap.createScaledBitmap(BitmapTools.fromByteArray(photoCard.photo), width, width*4/3, true));
        holder.message.setText(photoCard.message);
        if(photoCard.message == null || photoCard.message.isEmpty()) holder.message.setVisibility(View.GONE);
        holder.location.setText(photoCard.location);
        if(photoCard.location == null || photoCard.location.isEmpty()) holder.location.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return photoCardList.size();
    }
}
