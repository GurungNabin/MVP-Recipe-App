//package com.example.recipebook;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import com.example.recipebook.model.Recipe;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class DBHelper extends SQLiteOpenHelper {
//
//    private static final String DATABASE_NAME = "recipes_db";
//    private static final int DATABASE_VERSION = 1;
//    private static final String TABLE_NAME = "recipes";
//
//    private static final String COLUMN_ID = "id";
//    private static final String COLUMN_NAME = "name";
//    private static final String COLUMN_INGREDIENTS = "ingredients";
//    private static final String COLUMN_INSTRUCTIONS = "instructions";
//    private static final String COLUMN_PREPTIME = "prepTime";
//    private static final String COLUMN_COOKTIME = "cookTime";
//    private static final String COLUMN_SERVINGS = "servings";
//    private static final String COLUMN_DIFFICULTY = "difficulty";
//    private static final String COLUMN_CUISINE = "cuisine";
//    private static final String COLUMN_CALORIESPERSERVING = "caloriesPerServing";
//    private static final String COLUMN_TAGS = "tags";
//    private static final String COLUMN_IMAGE = "image";
//    private static final String COLUMN_MEALTYPE = "mealType";
//
//    public DBHelper(Context context){
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
//                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COLUMN_NAME + " TEXT, " +
//                COLUMN_INGREDIENTS + " TEXT, " +
//                COLUMN_INSTRUCTIONS + " TEXT, " +
//                COLUMN_PREPTIME + " TEXT, " +
//                COLUMN_COOKTIME + " TEXT, " +
//                COLUMN_SERVINGS + " INTEGER, " +
//                COLUMN_DIFFICULTY + " TEXT, " +
//                COLUMN_CUISINE + " TEXT, " +
//                COLUMN_CALORIESPERSERVING + " INTEGER, " +
//                COLUMN_TAGS + " TEXT, " +
//                COLUMN_IMAGE + " TEXT, " +
//                COLUMN_MEALTYPE + " TEXT" +
//                ")";
//        db.execSQL(CREATE_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(db);
//    }
//
//
//
//
//    public boolean insertRecipe(Recipe recipe) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put(COLUMN_NAME, recipe.getName());
//        values.put(COLUMN_INGREDIENTS, String.join(",", recipe.getIngredients())); // Convert list to comma-separated string
//        values.put(COLUMN_INSTRUCTIONS, String.join(",", recipe.getInstructions())); // Convert list to comma-separated string
//        values.put(COLUMN_PREPTIME, recipe.getPrepTimeMinutes());
//        values.put(COLUMN_COOKTIME, recipe.getCookTimeMinutes());
//        values.put(COLUMN_SERVINGS, recipe.getServings());
//        values.put(COLUMN_DIFFICULTY, recipe.getDifficulty());
//        values.put(COLUMN_CUISINE, recipe.getCuisine());
//        values.put(COLUMN_CALORIESPERSERVING, recipe.getCaloriesPerServing());
//        values.put(COLUMN_TAGS, String.join(",", recipe.getTags())); // Convert list to comma-separated string
//        values.put(COLUMN_IMAGE, recipe.getImage());
//        values.put(COLUMN_MEALTYPE, String.join(",", recipe.getMealType())); // Convert list to comma-separated string
//
//        long result = db.insert(TABLE_NAME, null, values);
//        return result != -1;
//    }
//
//
//    public List<Recipe> getAllRecipe() {
//        List<Recipe> recipes = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                Recipe recipe = new Recipe();
//                recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
//                recipe.setIngredients(new ArrayList<>(List.of(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS)).split(",")))); // Convert string back to list
//                recipe.setInstructions(new ArrayList<>(List.of(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTIONS)).split(",")))); // Convert string back to list
//                recipe.setPrepTimeMinutes(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PREPTIME)));
//                recipe.setCookTimeMinutes(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COOKTIME)));
//                recipe.setServings(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVINGS)));
//                recipe.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY)));
//                recipe.setCuisine(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUISINE)));
//                recipe.setCaloriesPerServing(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALORIESPERSERVING)));
//                recipe.setTags(new ArrayList<>(List.of(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGS)).split(",")))); // Convert string back to list
//                recipe.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
//                recipe.setMealType(new ArrayList<>(List.of(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEALTYPE)).split(",")))); // Convert string back to list
//
//                recipes.add(recipe);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return recipes;
//    }
//
//
//
//    public void deleteAllData(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM " + TABLE_NAME);
//    }
//
//}



package com.example.recipebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.recipebook.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipes_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "recipes";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_INGREDIENTS = "ingredients";
    private static final String COLUMN_INSTRUCTIONS = "instructions";
    private static final String COLUMN_PREPTIME = "prepTime";
    private static final String COLUMN_COOKTIME = "cookTime";
    private static final String COLUMN_SERVINGS = "servings";
    private static final String COLUMN_DIFFICULTY = "difficulty";
    private static final String COLUMN_CUISINE = "cuisine";
    private static final String COLUMN_CALORIESPERSERVING = "caloriesPerServing";
    private static final String COLUMN_TAGS = "tags";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_MEALTYPE = "mealType";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_INGREDIENTS + " TEXT, " +
                COLUMN_INSTRUCTIONS + " TEXT, " +
                COLUMN_PREPTIME + " INTEGER, " +
                COLUMN_COOKTIME + " INTEGER, " +
                COLUMN_SERVINGS + " INTEGER, " +
                COLUMN_DIFFICULTY + " TEXT, " +
                COLUMN_CUISINE + " TEXT, " +
                COLUMN_CALORIESPERSERVING + " INTEGER, " +
                COLUMN_TAGS + " TEXT, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_MEALTYPE + " TEXT" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert a new recipe into the database
    public boolean insertRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, recipe.getName());
        values.put(COLUMN_INGREDIENTS, String.join(",", recipe.getIngredients()));
        values.put(COLUMN_INSTRUCTIONS, String.join(",", recipe.getInstructions()));
        values.put(COLUMN_PREPTIME, recipe.getPrepTimeMinutes());
        values.put(COLUMN_COOKTIME, recipe.getCookTimeMinutes());
        values.put(COLUMN_SERVINGS, recipe.getServings());
        values.put(COLUMN_DIFFICULTY, recipe.getDifficulty());
        values.put(COLUMN_CUISINE, recipe.getCuisine());
        values.put(COLUMN_CALORIESPERSERVING, recipe.getCaloriesPerServing());
        values.put(COLUMN_TAGS, String.join(",", recipe.getTags()));
        values.put(COLUMN_IMAGE, recipe.getImage());
        values.put(COLUMN_MEALTYPE, String.join(",", recipe.getMealType()));

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        if (result == -1) {
            Log.e("DB_ERROR", "Failed to insert recipe: " + recipe.getName());
        } else {
            Log.d("DB_SUCCESS", "Recipe inserted successfully: " + recipe.getName());
        }

        return result != -1;

    }

    public List<Recipe> getAllRecipe() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Recipe recipe = new Recipe();
                    recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                    recipe.setIngredients(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS)).split(","))));
                    recipe.setInstructions(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTIONS)).split(","))));
                    recipe.setPrepTimeMinutes(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PREPTIME)));
                    recipe.setCookTimeMinutes(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COOKTIME)));
                    recipe.setServings(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVINGS)));
                    recipe.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY)));
                    recipe.setCuisine(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUISINE)));
                    recipe.setCaloriesPerServing(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALORIESPERSERVING)));
                    recipe.setTags(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGS)).split(","))));
                    recipe.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
                    recipe.setMealType(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEALTYPE)).split(","))));

                    recipes.add(recipe);
                } while (cursor.moveToNext());
            } else {
                Log.e("DB_ERROR", "No data found in the database.");
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return recipes;
    }

    // Delete all data from the recipes table
//    public void deleteAllData() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM " + TABLE_NAME);
//        db.close();
//    }
}
