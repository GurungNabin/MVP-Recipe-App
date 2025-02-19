package com.example.recipebook.recipe.api;

import com.example.recipebook.recipe.presenter.MyApiRecipe;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeApiService {
   @GET("recipes")
   Call<MyApiRecipe> getRecipes();

}
