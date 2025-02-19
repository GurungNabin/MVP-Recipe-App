package com.example.recipebook.recipe.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.MainActivity;
import com.example.recipebook.R;
import com.example.recipebook.food.RecipeDetails;
import com.example.recipebook.recipe.database.DBHelper;
import com.example.recipebook.recipe.model.Recipe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private Context context;
    private DBHelper dbHelper;

    public RecipeAdapter(List<Recipe> recipes,DBHelper dbHelper, MainActivity mainActivity){

        this.context = mainActivity;
        this.dbHelper = dbHelper;
        this.recipeList = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position){
        Recipe recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.getName());
        holder.recipeDifficulty.setText(recipe.getDifficulty());
        holder.recipeCuisine.setText(recipe.getCuisine());
//        String imagePath = String.valueOf(recipe.getImage());
        String imagePath = recipe.getImage().get(0);
//        imagePath = imagePath.replaceAll("^\\{|\\}$", "");

        if (imagePath != null && !imagePath.isEmpty()) {
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {

                Picasso.get()
                        .load(imagePath)
                        .into(holder.recipeImage);
            } else {

                File imageFile = new File(imagePath);
                Picasso.get()
                        .load(imageFile)
                        .into(holder.recipeImage);
            }
        } else {
            // If there's no image, set a default image or hide the ImageView
            holder.recipeImage.setImageResource(R.drawable.ic_launcher_foreground);  // Replace with a default image if desired
        }

        // set the recipe object as the tag on the itemview
        holder.itemView.setTag(recipe);


        // Long press listener to show confirmation dialog
        holder.itemView.setOnLongClickListener(v -> {
            // Create and show a confirmation dialog
            Recipe recipeToDelete = recipeList.get(position);
            new AlertDialog.Builder(context)
                    .setTitle("Delete Recipe")
                    .setMessage("Are you sure you want to delete this recipe?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteRecipe(position);
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true; // Return true to indicate that the long press has been handled
        });




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, RecipeDetails.class);
                Intent intent = new Intent(context, RecipeDetails.class);
                intent.putExtra("recipe", recipe);
                context.startActivity(intent);
            }
        });
    }


    private void deleteRecipe(int position) {
        Recipe recipeToDelete = recipeList.get(position);

        // Remove from the list and notify the adapter
        recipeList.remove(position);
        notifyItemRemoved(position);

        // Check if dbHelper is not null before calling delete
        if (dbHelper != null) {
            dbHelper.deleteRecipe(recipeToDelete);  // Ensure that dbHelper is initialized
            Toast.makeText(context, "Recipe deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "DBHelper is null, cannot delete recipe.", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public int getItemCount(){
        return  recipeList.size();
    }

    public void setRecipeList(List<Recipe> newRecipeList){
        this.recipeList = newRecipeList;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder{
        TextView recipeName, recipeDifficulty, recipeCuisine;
        ImageView recipeImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeDifficulty = itemView.findViewById(R.id.recipeDifficulty);
            recipeCuisine = itemView.findViewById(R.id.recipeCuisine);
            recipeImage = itemView.findViewById(R.id.recipeImage);
        }

    }
}