package com.example.recipebook.home;

import org.jetbrains.annotations.Contract;

public class HomeContract {

    interface View{
        void showProgress();

        void hideProgress();

        void setString(String string);
    }

    interface Model {
        interface OnFinishedListener{
            void onFinished(String string);
        }

        void getNextMovie(HomeContract.Model.OnFinishedListener onFinishedListener);
    }

    interface Presenter{
        void onButtonClick();

        void onDestroy();

        void onFinished(String string);
    }
}
