package com.example.recipebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
//    private Activity context;
    private Context context;

    public RecipeAdapter(List<Recipe> recipes, MainActivity mainActivity){

        this.context = mainActivity;
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
        String imagePath = recipe.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {

                Picasso.get()
                        .load(imagePath)
                        .into(holder.recipeImage);
            } else {
                Picasso.get()
                        .load("file://" + imagePath)
                        .into(holder.recipeImage);
            }
        } else {
            // If there's no image, set a default image or hide the ImageView
            holder.recipeImage.setImageResource(R.drawable.ic_launcher_foreground);  // Replace with a default image if desired
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, RecipeDetails.class);
                i.putExtra("name", recipe.getName());
                i.putExtra("ingredients", recipe.getIngredients());
                i.putExtra("instructions", recipe.getInstructions());
                i.putExtra("prepTime", recipe.getPrepTimeMinutes());

                i.putExtra("cookTime", recipe.getCookTimeMinutes());
                i.putExtra("servings", recipe.getServings());
                i.putExtra("difficulty", recipe.getDifficulty());
                i.putExtra("cuisine", recipe.getCuisine());
                i.putExtra("caloriesPerServing", recipe.getCaloriesPerServing());
                i.putExtra("tags", recipe.getTags());
                i.putExtra("image", recipe.getImage());
                i.putExtra("mealType", recipe.getMealType());




                context.startActivity(i);




            }
        });
    }

    @Override
    public int getItemCount(){
        return  recipeList.size();
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
