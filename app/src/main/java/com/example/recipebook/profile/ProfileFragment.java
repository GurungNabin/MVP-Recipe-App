package com.example.recipebook.profile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment implements  ProfileView {
    private ProfilePresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ProfilePresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadProfile();
    }

    @Override
    public void showProfileData(String profileData){

    }


}
