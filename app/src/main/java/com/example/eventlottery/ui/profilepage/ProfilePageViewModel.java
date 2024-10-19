package com.example.eventlottery.ui.profilepage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfilePageViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mText;
    public ProfilePageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profile page fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}