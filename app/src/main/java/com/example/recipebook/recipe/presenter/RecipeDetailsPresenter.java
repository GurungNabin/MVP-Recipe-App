//package com.example.recipebook.recipe.presenter;
//
//import com.example.recipebook.recipe.contract.RecipeDetailContract;
//import com.example.recipebook.recipe.model.Recipe;
//
//public class RecipeDetailsPresenter {
////    private RecipeDetailsView view;
//    private RecipeDetailContract.View view;
//
//    public RecipeDetailsPresenter(RecipeDetailContract.View view) {
//        this.view = view;
//    }
//
//    public void loadRecipeDetails(Recipe recipe){
//        if(recipe != null){
//            view.showRecipeDetails(recipe);
//        }
//    }
//}


package com.example.recipebook.recipe.presenter;

import android.content.Context;
import android.os.Environment;

import com.example.recipebook.recipe.contract.RecipeDetailContract;
import com.example.recipebook.recipe.model.Recipe;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecipeDetailsPresenter implements RecipeDetailContract.Presenter{

    private RecipeDetailContract.View view;


    public RecipeDetailsPresenter(RecipeDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void loadRecipeDetails(Recipe recipe) {
        if(recipe != null){
            view.showRecipeDetails(recipe);
        }
    }

    @Override
    public void downloadRecipeAsPdf(Recipe recipe) {
        try {
            File pdfFile = createPdf(recipe);
            view.onDownloadSuccess(pdfFile);

        }catch (Exception e){
            view.onDownloadFailure(e.getMessage());
        }
    }

//    private File createPdf(Recipe recipe) throws IOException {
//        File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"RecipeBook");
//        if(!pdfDir.exists()){
//            pdfDir.mkdirs();
//        }
//        File pdfFile = new File(pdfDir, recipe.getName().replaceAll("\\s+","_") + ".pdf");
//
//        PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
//        PdfDocument pdfDocument = new PdfDocument(writer);
//        Document document = new Document(pdfDocument);
//
//
//        // Add recipe details
//        document.add(new Paragraph("Recipe: " + recipe.getName()).setBold());
//        document.add(new Paragraph("\nIngredients:\n" + recipe.getIngredients()));
//        document.add(new Paragraph("\nInstructions:\n" + recipe.getInstructions()));
//
//        document.close();
//        return pdfFile;
//    }

    public static File createPdf(Recipe recipe) throws IOException {
        File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "RecipeBook");
        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
        }

        File pdfFile = new File(pdfDir, recipe.getName().replaceAll("\\s+", "_") + ".pdf");

        PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Title
        document.add(new Paragraph("Recipe: " + recipe.getName())
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER));

        // Subtitle
        document.add(new Paragraph("Ingredients").setFontSize(14).setBold());

        // Ingredients as List
        List ingredientList = new List();
        for (String ingredient : recipe.getIngredients()) {
            ingredientList.add(new ListItem(ingredient));
        }
        document.add(ingredientList);
        document.add(new Paragraph("Instruction").setFontSize(14).setBold());

        // Instructions Subtitle
        List instructionList = new List();
        for (String instruction : recipe.getInstructions()) {
            instructionList.add(new ListItem(instruction));
        }
        document.add(instructionList);

        // Optional Image
//        if (recipe.getImage() != null) {
//            Image image = new Image(ImageDataFactory.create(String.valueOf(recipe.getImage())));
//            document.add(image);
//        }


        document.close();
        return pdfFile;
    }

}
