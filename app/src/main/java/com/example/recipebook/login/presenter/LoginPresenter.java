package com.example.recipebook.login.presenter;

import com.example.recipebook.login.LoginContract;
import com.example.recipebook.login.model.LoginModel;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;
    private LoginContract.Model mModel;

    public LoginPresenter(LoginContract.View view, LoginContract.Model model) {
        mView = view;
        mModel = model;
    }

    @Override
    public void onLoginButtonClick(String email, String password){
        if(email == null || email.isEmpty()){
            mView.showUsernameEmptyError();
            return;
        }
        if (password == null || password.isEmpty()){
            mView.showPasswordEmptyError();
            return;
        }

        boolean isValid = mModel.login(email, password);
        if(isValid){
            mView.showLoginSuccessMessage();
        }else {
            mView.showInvalidCredentialsError();
        }
    }

}
