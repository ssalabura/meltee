package com.ssalabura.meltee.ui.addphoto;

import android.view.View;
import android.widget.Button;

import androidx.camera.view.PreviewView;

import com.ssalabura.meltee.R;
import com.ssalabura.meltee.ui.dashboard.PhotoCardViewHolder;

public class AddPhotoViewHolder {
    PreviewView previewView;
    View card_preview;
    PhotoCardViewHolder card_preview_holder;

    Button button_back;
    Button button_take_photo;
    Button button_additional_info;
    Button button_send;

    enum State {PHOTO_TAKEN, PHOTO_NOT_TAKEN}

    AddPhotoViewHolder(View root) {
        previewView = root.findViewById(R.id.previewView);
        card_preview = root.findViewById(R.id.card_preview);
        card_preview_holder = new PhotoCardViewHolder(card_preview);
        button_back = root.findViewById(R.id.button_back);
        button_take_photo = root.findViewById(R.id.button_take_photo);
        button_additional_info = root.findViewById(R.id.button_additional_info);
        button_send = root.findViewById(R.id.button_send);
    }

    void changeState(State state) {
        switch(state) {
            case PHOTO_NOT_TAKEN:
                previewView.setVisibility(View.VISIBLE);
                card_preview.setVisibility(View.INVISIBLE);

                button_take_photo.setVisibility(View.VISIBLE);

                button_back.setVisibility(View.INVISIBLE);
                button_additional_info.setVisibility(View.INVISIBLE);
                button_send.setVisibility(View.INVISIBLE);
                break;
            case PHOTO_TAKEN:
                previewView.setVisibility(View.INVISIBLE);
                card_preview.setVisibility(View.VISIBLE);

                button_take_photo.setVisibility(View.INVISIBLE);

                button_back.setVisibility(View.VISIBLE);
                button_additional_info.setVisibility(View.VISIBLE);
                button_send.setVisibility(View.VISIBLE);
                break;
        }
    }
}
