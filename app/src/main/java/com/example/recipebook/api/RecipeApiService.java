package com.example.recipebook.api;

import com.example.recipebook.model.MyApiRecipe;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeApiService {
   @GET("recipes")
   Call<MyApiRecipe> getRecipes();

}
