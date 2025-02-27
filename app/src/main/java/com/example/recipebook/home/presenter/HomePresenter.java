package com.example.recipebook.home.presenter;

import com.example.recipebook.home.HomeContract;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View homeView;
    private HomeContract.Model homeModel;

    public HomePresenter(HomeContract.View homeView, HomeContract.Model homeModel){
        this.homeView = homeView;
        this.homeModel = homeModel;
    }
    @Override
    public void onButtonClick() {
        homeView.showProgress();
        homeModel.getNextMovie(new HomeContract.Model.OnFinishedListener() {
            @Override
            public void onFinished(String string) {
                homeView.hideProgress();
                homeView.setString(string);
            }
        });
    }

    @Override
    public void onDestroy() {
        homeView = null;
    }

    @Override
    public void onFinished(String string){
        if(homeView != null){
            homeView.setString(string);
            homeView.hideProgress();
        }
    }
}
