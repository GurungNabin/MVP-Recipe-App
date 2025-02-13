package com.example.recipebook.model;


import java.util.ArrayList;

public class Recipe {
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
    private String image;
    private float rating;
    private int reviewCount;
    private ArrayList<String> mealType;

    public Recipe(String name, String cuisine, ArrayList<String> tags, String image, ArrayList<String> mealType) {
        this.name = name;
        this.cuisine = cuisine;
        this.tags = tags;
        this.image = image;
        this.mealType = mealType;
    }


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

    public String getImage() { return image; }
    public void setImage(String value) { this.image = value; }

    public double getRating() { return rating; }
    public void setRating(float value) { this.rating = value; }

    public long getReviewCount() { return reviewCount; }
    public void setReviewCount(int value) { this.reviewCount = value; }

    public ArrayList<String> getMealType() { return mealType; }
    public void setMealType(ArrayList<String> value) { this.mealType = value; }

    public Recipe(String name, ArrayList<String> ingredients, ArrayList<String> instructions, int prepTimeMinutes, int cookTimeMinutes, int servings, String difficulty, String cuisine,
                  int caloriesPerServing, ArrayList<String> tags, String image, ArrayList<String> mealType) {
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

    public Recipe(){

    }
}


//
//package com.example.recipebook.model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Recipe {
//        private String name;
//    private List<String> ingredients;
//    private List<String> instructions;
//    private int prepTimeMinutes;
//    private int cookTimeMinutes;
//    private int servings;
//    private String difficulty;
//    private String cuisine;
//    private int caloriesPerServing;
//    private List<String> tags;
//    private String image;
//    private float rating;
//    private int reviewCount;
//    private List<String> mealType;
//
//
//    public String getName() { return name; }
//    public void setName(String value) { this.name = value; }
//
//    public List<String> getIngredients() { return ingredients; }
//    public void setIngredients(List<String> value) { this.ingredients = value; }
//
//    public List<String> getInstructions() { return instructions; }
//    public void setInstructions(List<String> value) { this.instructions = value; }
//
//    public int getPrepTimeMinutes() { return prepTimeMinutes; }
//    public void setPrepTimeMinutes(int value) { this.prepTimeMinutes = value; }
//
//    public int getCookTimeMinutes() { return cookTimeMinutes; }
//    public void setCookTimeMinutes(int value) { this.cookTimeMinutes = value; }
//
//    public int getServings() { return servings; }
//    public void setServings(int value) { this.servings = value; }
//
//    public String getDifficulty() { return difficulty; }
//    public void setDifficulty(String value) { this.difficulty = value; }
//
//    public String getCuisine() { return cuisine; }
//    public void setCuisine(String value) { this.cuisine = value; }
//
//    public int getCaloriesPerServing() { return caloriesPerServing; }
//    public void setCaloriesPerServing(int value) { this.caloriesPerServing = value; }
//
//    public List<String> getTags() { return tags; }
//    public void setTags(List<String> value) { this.tags = value; }
//
//    public String getImage() { return image; }
//    public void setImage(String value) { this.image = value; }
//
//    public double getRating() { return rating; }
//    public void setRating(float value) { this.rating = value; }
//
//    public long getReviewCount() { return reviewCount; }
//    public void setReviewCount(int value) { this.reviewCount = value; }
//
//    public List<String> getMealType() { return mealType; }
//    public void setMealType(List<String> value) { this.mealType = value; }
//
//    // Constructor
//    public Recipe(String name, List<String> ingredients, List<String> instructions, int prepTimeMinutes, int cookTimeMinutes, int servings, String difficulty, String cuisine,
//                  int caloriesPerServing, List<String> tags, String image, List<String> mealType) {
//        this.name = name;
//        this.ingredients = ingredients;
//        this.instructions = instructions;
//        this.prepTimeMinutes = prepTimeMinutes;
//        this.cookTimeMinutes = cookTimeMinutes;
//        this.servings = servings;
//        this.difficulty = difficulty;
//        this.cuisine = cuisine;
//        this.caloriesPerServing = caloriesPerServing;
//        this.tags = tags;
//        this.image = image;
//        this.mealType = mealType;
//    }
//
//    public Recipe() {
//    }
//}
