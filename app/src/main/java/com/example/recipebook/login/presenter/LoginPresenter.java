package com.example.recipebook.login.presenter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.recipebook.MainActivity;
import com.example.recipebook.login.LoginContract;
import com.example.recipebook.login.model.LoginModel;
//
//public class LoginPresenter implements LoginContract.Presenter {
//    private LoginContract.View view;
//    private LoginContract.Model model;
//
//    public LoginPresenter(LoginContract.View view, LoginContract.Model model) {
//        view = view;
//        model = model;
//    }
//
////    @Override
////    public void onLoginButtonClick(String email, String password){
////        if(email == null || email.isEmpty()){
////            mView.showUsernameEmptyError();
////            return;
////        }
////        if (password == null || password.isEmpty()){
////            mView.showPasswordEmptyError();
////            return;
////        }
////
////        boolean isValid = mModel.login(email, password);
////        if(isValid){
////            mView.showLoginSuccessMessage();
////        }else {
////            mView.showInvalidCredentialsError();
////        }
////    }
//
//    @Override
//    public void onLoginButtonClick(String username, String password) {
//        if (username.isEmpty()) {
//            view.showUsernameEmptyError();
//            return;
//        }
//        if (password.isEmpty()) {
//            view.showPasswordEmptyError();
//            return;
//        }
//
//        model.login(username, password, new LoginContract.Model.LoginCallback() {
//            @Override
//            public void onSuccess(String token) {
//                ((Activity) view).runOnUiThread(() -> {
//                    // Optionally save token here
//                    view.showLoginSuccessMessage();
//                    // Navigate to main screen
//                    Intent intent = new Intent((Activity) view, MainActivity.class);
//                    ((Activity) view).startActivity(intent);
//                    ((Activity) view).finish();
//                });
//            }
//
//            @Override
//            public void onError(String message) {
//                ((Activity) view).runOnUiThread(view::showInvalidCredentialsError);
//            }
//        });
//    }
//
//
//}


public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private LoginContract.Model model;

    // Constructor to inject the View and Model
    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        this.model = new LoginModel(); // You can also inject this if needed
    }

    @Override
    public void onLoginButtonClick(String username, String password) {
        if (username.isEmpty()) {
            if (view != null) {
                view.showUsernameEmptyError();
            } else {
                Log.e("LoginPresenter", "View is null - cannot show username error");
            }
            return;
        }

        if (password.isEmpty()) {
            if (view != null) {
                view.showPasswordEmptyError();
            } else {
                Log.e("LoginPresenter", "View is null - cannot show password error");
            }
            return;
        }

        model.login(username, password, new LoginContract.Model.LoginCallback() {
            @Override
            public void onSuccess(String token) {
                ((Activity) view).runOnUiThread(() -> {
                    view.showLoginSuccessMessage();
                    Intent intent = new Intent((Activity) view, MainActivity.class);
                    ((Activity) view).startActivity(intent);
                    ((Activity) view).finish();
                });
            }

            @Override
            public void onError(String message) {
                ((Activity) view).runOnUiThread(view::showInvalidCredentialsError);
            }
        });
    }
}
