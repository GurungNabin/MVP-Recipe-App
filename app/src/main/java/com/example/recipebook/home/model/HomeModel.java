package com.example.recipebook.home.model;

import android.os.Handler;

import com.example.recipebook.home.HomeContract;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HomeModel implements HomeContract.Model {


    List<String> moviesList = Arrays.asList(
            "The Shawshank Redemption",
            "The Godfather",
            "The Dark Knight",
            "Pulp Fiction",
            "Forrest Gump",
            "The Lord of the Rings: The Return of the King",
            "Inception",
            "Fight Club",
            "The Matrix",
            "Interstellar"
    );

    @Override
    public void getNextMovie(final OnFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(getRandmString());
            }
        },1000);
    }

    private String getRandmString() {
        Random random = new Random();
        int index = random.nextInt(moviesList.size());
        return moviesList.get(index);
    }
}
