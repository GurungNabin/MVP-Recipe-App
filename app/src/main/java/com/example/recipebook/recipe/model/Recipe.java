package com.example.recipebook.recipe.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    private int id;
    private String name;
    private ArrayList<String> ingredients;
    private ArrayList<String> instructions;
    private int prepTimeMinutes;
    private int cookTimeMinutes;
    private int servings;
    private String difficulty;
    private String cuisine;
    private int caloriesPerServing;
    private ArrayList<String> tags;
    private ArrayList<String> image;
    private float rating;
    private int reviewCount;
    private ArrayList<String> mealType;

    public Recipe(String name, ArrayList<String> ingredients, ArrayList<String> instructions, int prepTimeMinutes, int cookTimeMinutes, int servings, String difficulty, String cuisine,
                  int caloriesPerServing, ArrayList<String> tags, ArrayList<String> image, ArrayList<String> mealType) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.prepTimeMinutes = prepTimeMinutes;
        this.cookTimeMinutes = cookTimeMinutes;
        this.servings = servings;
        this.difficulty = difficulty;
        this.cuisine = cuisine;
        this.caloriesPerServing = caloriesPerServing;
        this.tags = tags;
        this.image = image;
        this.mealType = mealType;
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        ingredients = in.createStringArrayList();
        instructions = in.createStringArrayList();
        prepTimeMinutes = in.readInt();
        cookTimeMinutes = in.readInt();
        servings = in.readInt();
        difficulty = in.readString();
        cuisine = in.readString();
        caloriesPerServing = in.readInt();
        tags = in.createStringArrayList();
        image = in.createStringArrayList();
        rating = in.readFloat();
        reviewCount = in.readInt();
        mealType = in.createStringArrayList();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };



    public Recipe(int prepTime, int cookTime, int servings, int calories) {
        this.prepTimeMinutes = prepTime;
        this.cookTimeMinutes = cookTime;
        this.caloriesPerServing = servings;
        this.servings = calories;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeStringList(ingredients);
        dest.writeStringList(instructions);
        dest.writeInt(prepTimeMinutes);
        dest.writeInt(cookTimeMinutes);
        dest.writeInt(servings);
        dest.writeString(difficulty);
        dest.writeString(cuisine);
        dest.writeInt(caloriesPerServing);
        dest.writeStringList(tags);
        dest.writeStringList(image);
        dest.writeFloat(rating);
        dest.writeInt(reviewCount);
        dest.writeStringList(mealType);
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public ArrayList<String> getIngredients() { return ingredients; }
    public void setIngredients(ArrayList<String> value) { this.ingredients = value; }

    public ArrayList<String> getInstructions() { return instructions; }
    public void setInstructions(ArrayList<String> value) { this.instructions = value; }

    public int getPrepTimeMinutes() { return prepTimeMinutes; }
    public void setPrepTimeMinutes(int value) { this.prepTimeMinutes = value; }

    public int getCookTimeMinutes() { return cookTimeMinutes; }
    public void setCookTimeMinutes(int value) { this.cookTimeMinutes = value; }

    public int getServings() { return servings; }
    public void setServings(int value) { this.servings = value; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String value) { this.difficulty = value; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String value) { this.cuisine = value; }

    public int getCaloriesPerServing() { return caloriesPerServing; }
    public void setCaloriesPerServing(int value) { this.caloriesPerServing = value; }

    public ArrayList<String> getTags() { return tags; }
    public void setTags(ArrayList<String> value) { this.tags = value; }

    public ArrayList<String> getImage() { return image; }
    public void setImage(ArrayList<String> value) { this.image = value; }

    public float getRating() { return rating; }
    public void setRating(float value) { this.rating = value; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int value) { this.reviewCount = value; }

    public ArrayList<String> getMealType() { return mealType; }
    public void setMealType(ArrayList<String> value) { this.mealType = value; }


    public Recipe(int prepTimeMinutes, int cookTimeMinutes, int caloriesPerServing, int servings, String difficulty) {
        this.prepTimeMinutes = prepTimeMinutes;
        this.cookTimeMinutes = cookTimeMinutes;
        this.caloriesPerServing = caloriesPerServing;
        this.servings = servings;
        this.difficulty = difficulty;
    }

    public Recipe(ArrayList<String> ingredients, ArrayList<String> instructions) {
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public Recipe(String name, String cuisine, ArrayList<String> tags, ArrayList<String> image, ArrayList<String> mealType) {
        this.name = name;
        this.cuisine = cuisine;
        this.tags = tags;
        this.image = image;
        this.mealType = mealType;
    }


}
