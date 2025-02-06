package com.example.recipebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity {

    String name, difficulty, cuisine, image;
    int prepTime, cookTime, servings, caloriesPerServing;
    ArrayList<String> ingredients, instructions, tags, types;

    TextView recipeName, prepCookTime, cookingTime, servingPeople, difficultyCuisineMeal, cuisineType, caloriesRating;
    LinearLayout ingredientsList,instructionList,tagsList, mealType;
    ImageView recipeImage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_screen);

        Intent intent = getIntent();

        // Log all received extras
        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                Log.d("RecipeDetails", "Received Intent Extra - " + key + ": " + value);
            }
        } else {
            Log.e("RecipeDetails", "Intent extras are NULL");
        }

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

        name = intent.getStringExtra("name");
        difficulty = intent.getStringExtra("difficulty");
        cuisine = intent.getStringExtra("cuisine");
        image = intent.getStringExtra("image");
        prepTime = intent.getIntExtra("prepTime",0);
        cookTime = intent.getIntExtra("cookTime", 0);
        servings = intent.getIntExtra("servings", 0);
        caloriesPerServing = intent.getIntExtra("caloriesPerServing", 0);
        ingredients = intent.getStringArrayListExtra("ingredients");
        instructions = intent.getStringArrayListExtra("instructions");
        tags = intent.getStringArrayListExtra("tags");
        types = intent.getStringArrayListExtra("mealType");

        Log.d("RecipeDetails", "Retrieved Values - PrepTime: " + prepTime +
                ", CookTime: " + cookTime +
                ", Servings: " + servings +
                ", Calories: " + caloriesPerServing);


        recipeName.setText(name);
        difficultyCuisineMeal.setText(difficulty);
        cuisineType.setText(cuisine);
        Picasso.get().load(image).into(recipeImage);
        if (ingredients != null) {
            for (String ingredient : ingredients) {
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setPadding(8, 8, 8, 8);
                ingredientsList.addView(textView);
            }
        }
        if (instructions != null) {
            for (String ingredient : instructions) {
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setPadding(8, 8, 8, 8);
                instructionList.addView(textView);
            }
        }
        prepCookTime.setText("Time to prepare " + prepTime + " min");
        cookingTime.setText("Time to cook "+ cookTime + " min");
        servingPeople.setText(servings + " people");
        caloriesRating.setText(caloriesPerServing + " kcal");
        if(tags != null){
            for (String tags : tags){
                TextView textView = new TextView(this);
                textView.setText( tags);
                textView.setPadding(8, 8, 8, 8);
                tagsList.addView(textView);
            }
        }
        if(types != null){
            for (String types : types){
                TextView textView = new TextView(this);
                textView.setText( types);
                textView.setPadding(8, 8, 8, 8);
                mealType.addView(textView);
            }
        }

        Log.d("Type", "The types " + types);


    }
}

