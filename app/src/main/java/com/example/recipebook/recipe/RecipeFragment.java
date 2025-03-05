package com.example.recipebook.recipe;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.MainActivity;
import com.example.recipebook.R;
import com.example.recipebook.recipe.presenter.RecipePresenter;
import com.example.recipebook.recipe.presenter.RecipePresenterImpl;
import com.example.recipebook.recipe.view.RecipeView;
import com.example.recipebook.recipe.api.RecipeApiService;
import com.example.recipebook.recipe.api.RetrofitClient;
import com.example.recipebook.recipe.database.DBHelper;
import com.example.recipebook.recipe.model.ApiRecipe;
import com.example.recipebook.recipe.presenter.MyApiRecipe;
import com.example.recipebook.recipe.model.Recipe;
import com.example.recipebook.recipe.repository.RecipeRepository;
import com.example.recipebook.recipe.view.RecipeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

public class RecipeFragment extends Fragment implements RecipeView {


    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipes;
     private RecipePresenter presenter;
    private DBHelper dbHelper;
    private ActivityResultLauncher<Intent> addRecipeLauncher;
    private boolean isLoading = false;

    private SearchView searchBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_main,container,false);

        dbHelper = new DBHelper(requireContext());
        recipes = new ArrayList<>();

        presenter = new RecipePresenterImpl(this, new RecipeRepository(requireContext()));

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipeAdapter = new RecipeAdapter(recipes,dbHelper, (MainActivity) requireActivity());
        recyclerView.setAdapter(recipeAdapter);

        searchBar = view.findViewById(R.id.searchView);
        searchBar.setOnClickListener(v->{
            searchBar.onActionViewExpanded();
            searchBar.requestFocus();
        });
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    fetchDataFromAPI();  // Fetch and show data from API

                } else {
                    // When there is a search query, show the filtered results
                    presenter.showSearchRecipe(newText, recipes);
                }
                return false;
            }

        });

        // floating action button to add new recipe
        FloatingActionButton fabAddRecipe = view.findViewById(R.id.idFabAddRecipe);
        fabAddRecipe.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), RecipePageActivity.class);
            addRecipeLauncher.launch(intent);
        });

        addRecipeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == getActivity().RESULT_OK){
//                        recipes.clear();
                        presenter.loadRecipes();
                    }
                }
        );


        FloatingActionButton downloadRecipe = view.findViewById(R.id.idFabRecipeListDownload);
        downloadRecipe.setOnClickListener(v -> {
            // This ensures the PDF is generated only when the user clicks the download button
            generateAndDownloadPDF();
        });



        presenter.loadRecipes();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (layoutManager != null && !isLoading &&
                        layoutManager.findLastCompletelyVisibleItemPosition() == recipes.size() - 1) {
                    isLoading = true;
                    presenter.loadRecipes();
                }
            }
        });

        fetchDataFromAPI();
        return view;
    }


    private void generateAndDownloadPDF() {
        if (recipes.isEmpty()) {
            Toast.makeText(getActivity(), "No recipes available to generate PDF.", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        String pdfFileName = "recipe_data_" + timestamp + ".pdf";

        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "RecipeBook/" + pdfFileName);

        try {
            PdfWriter writer = new PdfWriter(pdfFile);
            PdfDocument pdf = new PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf, PageSize.A4, false);
            document.setMargins(10, 10, 10, 10);

            Div headerDiv = new Div();

            String name = "Recipe Book";
            Paragraph nameParagraph = new Paragraph(name)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(22).setBold();

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Image image = new Image(ImageDataFactory.create(byteArray)).scale(0.2f, 0.2f);
            image.scale(.05f,.05f);


            headerDiv.add(image).setTextAlignment(TextAlignment.LEFT);
            headerDiv.add(nameParagraph).setTextAlignment(TextAlignment.RIGHT).setWidth(300); // Set width if needed


            document.add(headerDiv);

            // Add Current Date and Time
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateAndTime = sdfDate.format(new Date());
            Paragraph dateParagraph = new Paragraph("Date: " + currentDateAndTime)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(14);
            document.add(dateParagraph);

            Paragraph title = new Paragraph("Recipe Data")
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(18);
            document.add(title);

            float[] columnWidths = {0.5f, 2f, 2f, 2f, 1.5f, 1.5f, 1.5f, 1.5f};
            Table table = new Table(columnWidths);

            table.addCell(new Cell().add(new Paragraph("ID")).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph("Recipe Name")).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph("Ingredients")).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph("Instruction")).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph("Prep Time")).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph("Cook Time")).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph("Serving")).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph("Difficulty")).setTextAlignment(TextAlignment.CENTER));

            // Add Recipe Data Rows
            for (Recipe recipe : recipes) {
                table.addCell(String.valueOf(recipe.getId()));
                table.addCell(recipe.getName());

                // Format Ingredients
                StringBuilder ingredientsFormatted = new StringBuilder();
                for (String ingredient : recipe.getIngredients()) {
                    ingredientsFormatted.append("- ").append(ingredient).append("\n");
                }
                table.addCell(ingredientsFormatted.toString());

                // Format Instructions
                StringBuilder instructionsFormatted = new StringBuilder();
                for (String instruction : recipe.getInstructions()) {
                    instructionsFormatted.append("- ").append(instruction).append("\n");
                }
                table.addCell(instructionsFormatted.toString());

                table.addCell(String.valueOf(recipe.getPrepTimeMinutes()));
                table.addCell(String.valueOf(recipe.getCookTimeMinutes()));
                table.addCell(String.valueOf(recipe.getServings()));
                table.addCell(String.valueOf(recipe.getDifficulty()));
            }

            // Add the table to the document
            document.add(table);
            document.close();

            Toast.makeText(getActivity(), "PDF Generated Successfully!", Toast.LENGTH_SHORT).show();
            openGeneratedPDF(pdfFile);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error generating PDF", Toast.LENGTH_SHORT).show();
        }
    }
    // Method to open the PDF file
    private void openGeneratedPDF(File pdfFile) {
        // Use the correct authority based on your package name
        Uri pdfUri = FileProvider.getUriForFile(getActivity(), getContext().getPackageName() + ".provider", pdfFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Check if there's an app to handle the PDF file
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "No PDF viewer available", Toast.LENGTH_SHORT).show();
        }
    }

//    private void fetchDataFromAPI() {
//        RecipeApiService recipeApi = RetrofitClient.getApiService();
//        Call<MyApiRecipe> call = recipeApi.getRecipes();
//
//        call.enqueue(new Callback<MyApiRecipe>() {
//            @Override
//            public void onResponse(Call<MyApiRecipe> call, Response<MyApiRecipe> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    MyApiRecipe myApiRecipe = response.body();
//                    List<ApiRecipe> apiRecipes = myApiRecipe.getApiRecipes();
//
//                    Log.d("API_DATA", "Fetched API data: " + apiRecipes.size());
//
//
//                    for (ApiRecipe apiRecipe : apiRecipes) {
//                        Recipe recipe = getRecipe(apiRecipe);
//                        recipes.add(recipe);
//                    }
//                    recipeAdapter.notifyDataSetChanged();
//                } else {
//                    Log.e("API_ERROR", "Error: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyApiRecipe> call, Throwable t) {
//                Log.e("API_FAILURE", "Error data: " + t.getMessage());
//            }
//        });
//    }


    private void fetchDataFromAPI() {
        try {
            RecipeApiService recipeApi = RetrofitClient.getApiService();
            Call<MyApiRecipe> call = recipeApi.getRecipes();

            call.enqueue(new Callback<MyApiRecipe>() {
                @Override
                public void onResponse(Call<MyApiRecipe> call, Response<MyApiRecipe> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        MyApiRecipe myApiRecipe = response.body();
                        List<ApiRecipe> apiRecipes = myApiRecipe.getApiRecipes();

                        Log.d("API_DATA", "Fetched API data: " + apiRecipes.size());

                        for (ApiRecipe apiRecipe : apiRecipes) {
                            Log.d("IMAGE_URL", "Image URL: " + apiRecipe.getImage()); // Log the image URL

                            Recipe recipe = getRecipe(apiRecipe);
                            recipes.add(recipe);
                        }
                        recipeAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("API_ERROR", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<MyApiRecipe> call, Throwable t) {
                    Log.e("API_FAILURE", "Error data: " + t.getMessage());
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            Log.e("API_FAILURE", "Error initializing Retrofit: " + e.getMessage());
        }
    }



    private @NonNull Recipe getRecipe(ApiRecipe apiRecipe) {
        Recipe recipe = new Recipe();
        recipe.setName(apiRecipe.getName());
        recipe.setIngredients(apiRecipe.getIngredients());
        recipe.setInstructions(apiRecipe.getInstructions());
        recipe.setCookTimeMinutes(apiRecipe.getCookTimeMinutes());
        recipe.setPrepTimeMinutes(apiRecipe.getPrepTimeMinutes());
        recipe.setServings(apiRecipe.getServings());
        recipe.setDifficulty(apiRecipe.getDifficulty());
        recipe.setCuisine(apiRecipe.getCuisine());
        recipe.setCaloriesPerServing(apiRecipe.getCaloriesPerServing());
        recipe.setTags(apiRecipe.getTags());
        ArrayList<String> images = new ArrayList<>();
        images.add(apiRecipe.getImage());
        recipe.setImage(images);
        recipe.setMealType(apiRecipe.getMealType());

        recipe.setId(apiRecipe.getId());
        return recipe;
    }

    @Override
    public void onResume() {
        super.onResume();
//        recipes.clear();
        presenter.loadRecipes();

    }

    @Override
    public void showLoading() {
        isLoading = true;
    }

    @Override
    public void hideLoading() {
        isLoading = false;
    }

    @Override
    public void showDatabaseRecipes(List<Recipe> recipes) {
//        this.recipes.clear();
        this.recipes.addAll(recipes);
        recipeAdapter.notifyDataSetChanged();
//        generateAndDownloadPDF();
    }


    @Override
    public void showFilteredRecipes(List<Recipe> filteredRecipes) {
        this.recipes.clear();
        this.recipes.addAll(filteredRecipes);

        recipeAdapter.notifyDataSetChanged();
        presenter.loadRecipes();
    }




    @Override
    public void showError(String message) {

    }

    @Override
    public void navigateToRecipeDetails(Recipe recipe) {
//        Intent intent = new Intent(getContext(), RecipeDetails.class);
        Intent intent = new Intent(getContext(), RecipeDetails.class);
        intent.putExtra("recipe_id",recipe.getId());
        startActivity(intent);
    }

    @Override
    public void showDownloadErrorMessage() {
        Toast.makeText(getContext(), "can't download", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showDownloadComplete(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void searchALlRecipe(List<Recipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
        recipeAdapter.notifyDataSetChanged(); // Notify adapter to refresh the list


    }

}
