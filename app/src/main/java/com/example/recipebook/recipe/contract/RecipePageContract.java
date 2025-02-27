package com.example.recipebook.recipe.contract;

import androidx.fragment.app.Fragment;

public interface RecipePageContract {
    interface View{
        void loadFragment(Fragment fragment);
        void showToast(String message);
        void navigateToMainScreen();
    }

    interface Presenter{
        void handleNext(Fragment currentFragment);
        void saveToDatabase();
    }
}
