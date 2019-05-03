package com.udemyandroid.minitwitter.data;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.app.Application;
import android.support.annotation.NonNull;

import com.udemyandroid.minitwitter.retrofit.request.RequestUserProfile;
import com.udemyandroid.minitwitter.retrofit.response.ResponseUserProfile;

public class ProfileViewModel extends AndroidViewModel {
    public ProfileRepository profileRepository;
    public LiveData<ResponseUserProfile> userProfile;
    public MutableLiveData<String> photoProfile;

    public ProfileViewModel(@NonNull Application application){
        super(application);
        profileRepository = new ProfileRepository();
        userProfile = profileRepository.getProfile();
        photoProfile = profileRepository.getPhotoProfile();
    }

    public void updateProfile(RequestUserProfile requestUserProfile){
        profileRepository.updateProfile(requestUserProfile);
    }

    public void uploadPhoto(String photo){
        profileRepository.uploadPhoto(photo);

    }
}
