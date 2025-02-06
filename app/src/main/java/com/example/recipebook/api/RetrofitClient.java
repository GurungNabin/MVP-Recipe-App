package com.example.recipebook.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://dummyjson.com/";
    private static Retrofit retrofit;

    public static RecipeApiService getApiService(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(RecipeApiService.class);
    }
}
