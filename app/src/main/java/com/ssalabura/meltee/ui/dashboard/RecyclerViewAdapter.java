package com.ssalabura.meltee.ui.dashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;

import java.io.IOException;
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

        int width = 1080;
        int height = 1440;
        int orientation = ExifInterface.ORIENTATION_UNDEFINED;
        try {
            ExifInterface exifInterface = new ExifInterface(photoCard.imagePath);
            orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if(orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                width = 1440;
                height = 1080;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(photoCard.imagePath), width, height, true);
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotate(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = rotate(bitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmap = rotate(bitmap, 270);
        }
        holder.photo.setImageBitmap(bitmap);
        holder.text.setText(photoCard.text);
    }

    private Bitmap rotate(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return rotated;
    }

    @Override
    public int getItemCount() {
        return this.photoCardList.size();
    }
}
