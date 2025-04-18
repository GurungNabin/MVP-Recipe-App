package com.example.recipebook.login;

public class LoginContract {
    // interface for the view
    public interface View{
        void showInvalidCredentialsError();
        void showLoginSuccessMessage();
        void showUsernameEmptyError();
        void showPasswordEmptyError();
    }

     public interface Presenter{
        void onLoginButtonClick(String email,String password);
    }

//      public interface Model{
//        boolean login(String email, String password);
//    }

    public interface  Model{
        void login(String email,String password, LoginCallback callback);

        interface LoginCallback{
            void onSuccess(String token);
            void onError(String message);
        }
    }
}
