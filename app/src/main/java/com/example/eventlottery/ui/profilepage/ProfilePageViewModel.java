package com.example.eventlottery.ui.profilepage;

import android.widget.Button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventlottery.User;

public class ProfilePageViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    public static ProfilePageViewModel get(Class<ProfilePageViewModel> profilePageViewModelClass) {
        return null;
    }
    private final MutableLiveData<User> uiState =
            new MutableLiveData(new User(null, null, null, true));
    public LiveData<User> getUiState() {
        return uiState;
    }

}