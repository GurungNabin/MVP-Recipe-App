package com.example.recipebook.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyApiRecipe {
    @SerializedName("recipes")
    private List<ApiRecipe> apiRecipes;

    public List<ApiRecipe> getApiRecipes() {
        return apiRecipes;
    }
}
