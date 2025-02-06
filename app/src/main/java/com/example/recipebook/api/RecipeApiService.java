package com.example.recipebook.api;

import com.example.recipebook.model.MyRecipe;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeApiService {
   @GET("recipes")
    Call<MyRecipe> getRecipes();

}
