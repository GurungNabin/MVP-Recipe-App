package com.example.recipebook.recipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.recipebook.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private List<String> imageUrls;

    public ImageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_image, container, false);

        ImageView imageView = view.findViewById(R.id.recipeImage2);

        if (imageUrls.get(position).startsWith("http://") || imageUrls.get(position).startsWith("https://")) {

            Picasso.get()
                    .load(imageUrls.get(position))
                    .into(imageView);
        } else {

            File imageFile = new File(imageUrls.get(position));
            Picasso.get()
                    .load(imageFile)
                    .into(imageView);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
