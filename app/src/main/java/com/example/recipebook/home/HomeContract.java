package com.example.recipebook.home;

import org.jetbrains.annotations.Contract;

public class HomeContract {

   public interface View{
        void showProgress();

        void hideProgress();

        void setString(String string);
    }

   public interface Model {
        interface OnFinishedListener{
            void onFinished(String string);
        }

        void getNextMovie(HomeContract.Model.OnFinishedListener onFinishedListener);
    }

   public interface Presenter{
        void onButtonClick();

        void onDestroy();

        void onFinished(String string);
    }
}
