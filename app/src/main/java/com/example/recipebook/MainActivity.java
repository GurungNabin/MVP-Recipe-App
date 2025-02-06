//package com.example.recipebook;//package com.example.recipebook;
//
//import android.os.Bundle;
//import android.util.Log;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.recipebook.RecipeAdapter;
//import com.example.recipebook.api.RecipeApiService;
//import com.example.recipebook.api.RetrofitClient;
//import com.example.recipebook.model.MyRecipe;
//import com.example.recipebook.model.Recipe;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private RecipeAdapter recipeAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//          recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        RecipeApiService recipeApi = RetrofitClient.getApiService();
//        Call<MyRecipe> call = recipeApi.getRecipes();
//
//        call.enqueue(new Callback<MyRecipe>() {
//            @Override
//            public void onResponse(Call<MyRecipe> call, Response<MyRecipe> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    MyRecipe myRecipe = response.body();
//                    List<Recipe> recipes = myRecipe.getRecipes();
//                    recipeAdapter = new RecipeAdapter(recipes, MainActivity.this);
//                    recyclerView.setAdapter(recipeAdapter);
//                } else {
//                    Log.e("API_ERROR", "Error: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyRecipe> call, Throwable t) {
//                Log.e("API_FAILURE", "Error: " + t.getMessage());
//            }
//        });
//    }
//
//}



// Using api and SQLiteDatabase

package com.example.recipebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.api.RecipeApiService;
import com.example.recipebook.api.RetrofitClient;
import com.example.recipebook.database.DBHelper;
import com.example.recipebook.databinding.ActivityMainBinding;
import com.example.recipebook.model.MyRecipe;
import com.example.recipebook.model.Recipe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private DBHelper dbHelper;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup navcontroller

        dbHelper = new DBHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchDataFromAPI();

    }

    private void fetchDataFromAPI(){
        RecipeApiService recipeApi = RetrofitClient.getApiService();
        Call<MyRecipe> call = recipeApi.getRecipes();

        call.enqueue(new Callback<MyRecipe>() {
            @Override
            public void onResponse(Call<MyRecipe> call, Response<MyRecipe> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MyRecipe myRecipe = response.body();
                    List<Recipe> recipes = myRecipe.getRecipes();
                    recipeAdapter = new RecipeAdapter(recipes, MainActivity.this);
                    recyclerView.setAdapter(recipeAdapter);

                    dbHelper.deleteAllData();

                    for (Recipe recipe : recipes){
                        dbHelper.insertRecipe(recipe);
                    }

                    Log.d("DB","Data stored locally");
                    displayLocalData();
                } else {
                    Log.e("API_ERROR", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MyRecipe> call, Throwable t) {
                Log.e("API_FAILURE", "Error: " + t.getMessage());
                displayLocalData();
            }
        });
    }

    private void displayLocalData(){
        List<Recipe> recipes = dbHelper.getAllRecipe();
        recipeAdapter = new RecipeAdapter(recipes, MainActivity.this);
        recyclerView.setAdapter(recipeAdapter);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.recyclerView, fragment);
        fragmentTransaction.commit();
    }

}
