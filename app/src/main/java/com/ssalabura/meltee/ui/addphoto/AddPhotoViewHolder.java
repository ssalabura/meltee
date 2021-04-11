package com.ssalabura.meltee.ui.addphoto;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.camera.view.PreviewView;

import com.ssalabura.meltee.R;

public class AddPhotoViewHolder {
    PreviewView previewView;
    ImageView imageView;
    Button button_back;
    Button button_take_photo;
    Button button_send;

    Bitmap imageBitmap;

    enum State {PHOTO_TAKEN, PHOTO_NOT_TAKEN}

    AddPhotoViewHolder(View root) {
        previewView = root.findViewById(R.id.previewView);
        imageView = root.findViewById(R.id.imageView);
        button_back = root.findViewById(R.id.button_back);
        button_take_photo = root.findViewById(R.id.button_take_photo);
        button_send = root.findViewById(R.id.button_send);
    }

    void changeState(State state) {
        switch(state) {
            case PHOTO_NOT_TAKEN:
                previewView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);

                button_back.setVisibility(View.INVISIBLE);
                button_take_photo.setVisibility(View.VISIBLE);
                button_send.setVisibility(View.INVISIBLE);
                break;
            case PHOTO_TAKEN:
                previewView.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);

                button_back.setVisibility(View.VISIBLE);
                button_take_photo.setVisibility(View.INVISIBLE);
                button_send.setVisibility(View.VISIBLE);
                break;
        }
    }

    void setImageBitmap(Bitmap bitmap) {
        imageBitmap = bitmap;
        imageView.setImageBitmap(bitmap);
    }

    Bitmap getImageBitmap() {
        return imageBitmap;
    }
}
