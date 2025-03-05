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

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.recipebook.R;
import com.example.recipebook.recipe.contract.RecipeDetailContract;
import com.example.recipebook.recipe.model.Recipe;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecipeDetailsPresenter implements RecipeDetailContract.Presenter{

    private RecipeDetailContract.View view;
    private Handler handler = new Handler(Looper.getMainLooper());


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
            Uri pdfUri = createPdf(recipe);
//            File pdfUri = createPdf(recipe);
            view.onDownloadSuccess(pdfUri);
        } catch (Exception e) {
            view.onDownloadFailure(e.getMessage());

        }
    }



    public Uri createPdf(Recipe recipe) throws IOException {
        Context context = (Context) view;
        Uri pdfUri = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Scoped storage: Saving the PDF using MediaStore for Android 10 and above.
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, recipe.getName().replaceAll("\\s+", "_") + ".pdf");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");

            // Save to Downloads/RecipeBook
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/RecipeBook");

            // Insert the file into MediaStore
            pdfUri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);

            if (pdfUri != null) {
                try (OutputStream outputStream = context.getContentResolver().openOutputStream(pdfUri)) {
                    if (outputStream != null) {
                        // Create the PDF document
                        PdfWriter writer = new PdfWriter(outputStream);
                        PdfDocument pdfDocument = new PdfDocument(writer);
                        Document document = new Document(pdfDocument);

                        // Add content to the PDF
                        document.add(new Paragraph("Recipe: " + recipe.getName())
                                .setBold()
                                .setFontSize(18)
                                .setTextAlignment(TextAlignment.CENTER));

                        document.add(new Paragraph("Ingredients").setFontSize(14).setBold());

                        List ingredientList = new List();
                        for (String ingredient : recipe.getIngredients()) {
                            ingredientList.add(new ListItem(ingredient));
                        }
                        document.add(ingredientList);

                        document.add(new Paragraph("Instructions").setFontSize(14).setBold());

                        List instructionList = new List();
                        for (String instruction : recipe.getInstructions()) {
                            instructionList.add(new ListItem(instruction));
                        }
                        document.add(instructionList);

                        document.close();
                    }
                } catch (Exception e) {
                    Log.e("PDF Error", "Error while writing the PDF", e);
                }
            } else {
                Log.e("PDF Error", "Failed to get PDF URI from MediaStore");
            }
        } else {
            // For Android versions below 10, use traditional external storage method (requires WRITE_EXTERNAL_STORAGE permission)
            File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "RecipeBook");
            if (!pdfDir.exists()) {
                boolean dirCreated = pdfDir.mkdirs();
                if (dirCreated) {
                    Log.d("PDF Directory", "Directory created successfully");
                } else {
                    Log.d("PDF Directory", "Directory creation failed");
                }
            }

            File pdfFile = new File(pdfDir, recipe.getName().replaceAll("\\s+", "_") + ".pdf");
            try (FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
                // Create the PDF document
                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument);

                // Add content to the PDF
                document.add(new Paragraph("Recipe: " + recipe.getName())
                        .setBold()
                        .setFontSize(18)
                        .setTextAlignment(TextAlignment.CENTER));

                document.add(new Paragraph("Ingredients").setFontSize(14).setBold());

                List ingredientList = new List();
                for (String ingredient : recipe.getIngredients()) {
                    ingredientList.add(new ListItem(ingredient));
                }
                document.add(ingredientList);

                document.add(new Paragraph("Instructions").setFontSize(14).setBold());

                List instructionList = new List();
                for (String instruction : recipe.getInstructions()) {
                    instructionList.add(new ListItem(instruction));
                }
                document.add(instructionList);

                document.close();
            }
            pdfUri = Uri.fromFile(pdfFile); // For backward compatibility with older versions
        }

        return pdfUri;
    }

}
