package com.example.recipebook.food.view;

import java.util.List;

public interface FoodView {
    // Method to display the list of recipes
    void displayRecipes(List<String> recipes);

    // Method to show a loading indicator while fetching data
    void showLoading();

    // Method to hide the loading indicator when data is loaded
    void hideLoading();
}

