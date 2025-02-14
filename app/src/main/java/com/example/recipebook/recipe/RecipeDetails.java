package com.example.recipebook.recipe;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.recipebook.R;
import com.example.recipebook.model.Recipe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity {

    private  ImageAdapter imageAdapter;
    private ViewPager viewPager;

    TextView recipeName, prepCookTime, cookingTime, servingPeople, difficultyCuisineMeal, cuisineType, caloriesRating;
    LinearLayout ingredientsList,instructionList,tagsList, mealType;
    ImageView recipeImage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_screen);
        initializeView();
        Recipe recipe = getIntent().getParcelableExtra("recipe");
        if (recipe != null) {
            setData(recipe);
        }
    }

    private void setData(Recipe recipe) {
        recipeName.setText(recipe.getName());
        prepCookTime.setText("Time to prepare " + recipe.getPrepTimeMinutes() + " min");
        cookingTime.setText("Time to cook "+ recipe.getCookTimeMinutes() + " min");
        servingPeople.setText(recipe.getServings() + " people");
        caloriesRating.setText(recipe.getCaloriesPerServing() + " kcal");


        if (recipe.getIngredients() != null) {
            for (String ingredient : recipe.getIngredients()) {
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setPadding(8, 8, 8, 8);
                ingredientsList.addView(textView);
            }
        }
        if (recipe.getInstructions() != null) {
            for (String ingredient : recipe.getInstructions()) {
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setPadding(8, 8, 8, 8);
                instructionList.addView(textView);
            }
        }
        if(recipe.getTags() != null){
            for (String tags : recipe.getTags()){
                TextView textView = new TextView(this);
                textView.setText( tags);
                textView.setPadding(8, 8, 8, 8);
                tagsList.addView(textView);
            }
        }
        if(recipe.getMealType() != null){
            for (String types : recipe.getMealType()){
                TextView textView = new TextView(this);
                textView.setText( types);
                textView.setPadding(8, 8, 8, 8);
                mealType.addView(textView);
            }
        }
        if(recipe.getImage() != null) {
            setImageAdapter(recipe.getImage());
        }
    }

    private void setImageAdapter(ArrayList<String> recipeImage) {
//        recipeImage.add("/storage/emulated/0/Android/data/com.example.recipebook/files/recipe_images/recipe_image_1739419973983.png");
        imageAdapter = new ImageAdapter(this, recipeImage);
        viewPager.setAdapter(imageAdapter);
    }

    private void initializeView() {
        recipeName = findViewById(R.id.recipeName);
        ingredientsList = findViewById(R.id.ingredientsList);
        instructionList = findViewById(R.id.instructionsList);
        prepCookTime = findViewById(R.id.prepCookTime);
        cookingTime = findViewById(R.id.cookingTime);
        servingPeople = findViewById(R.id.servings);
        difficultyCuisineMeal = findViewById(R.id.difficultyCuisineMeal);
        cuisineType = findViewById(R.id.cuisine);
        caloriesRating = findViewById(R.id.caloriesRating);
        tagsList = findViewById(R.id.tags);
        recipeImage = findViewById(R.id.recipeImage);
        mealType = findViewById(R.id.types);
        viewPager = findViewById(R.id.viewPagerMain);
    }
}
