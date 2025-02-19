package com.example.recipebook.recipe.presenter;

import com.example.recipebook.recipe.model.ApiRecipe;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyApiRecipe {
    @SerializedName("recipes")
    private List<ApiRecipe> apiRecipes;

    public List<ApiRecipe> getApiRecipes() {
        return apiRecipes;
    }
}
