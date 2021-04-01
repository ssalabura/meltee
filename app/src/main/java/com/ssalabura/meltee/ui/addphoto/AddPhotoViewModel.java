package com.ssalabura.meltee.ui.addphoto;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddPhotoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddPhotoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add_photo fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}