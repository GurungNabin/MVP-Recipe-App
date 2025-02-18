package com.example.recipebook.profile;

public class ProfilePresenter {
    private ProfileView view;

    public ProfilePresenter(ProfileView view){
        this.view = view;
    }

    public void loadProfile(){
        view.showProfileData("Profile Data");
    }
}
