package com.example.recipebook.food.presenter;

import com.example.recipebook.food.view.FoodView;

import java.util.Arrays;
import java.util.List;

public class FoodPresenter {
    private FoodView view;

    public FoodPresenter(FoodView view) {
        this.view = view;
    }

    public void loadFood() {
        // Fetch recipes and update view
        view.showLoading();
        List<String> recipes = Arrays.asList("Recipe 1", "Recipe 2");
        view.displayRecipes(recipes);
        view.hideLoading();
    }
}
