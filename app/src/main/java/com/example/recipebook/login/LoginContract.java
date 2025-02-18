package com.example.recipebook.login;

public class LoginContract {
    // interface for the view
    public interface View{
        void showInvalidCredentialsError();
        void showLoginSuccessMessage();
        void showUsernameEmptyError();
        void showPasswordEmptyError();
    }

    // interface for the presenter
    public interface Presenter{
        void onLoginButtonClick(String email,String password);
    }

    // interface for the model
     public interface Model{
        boolean login(String email, String password);
    }
}
