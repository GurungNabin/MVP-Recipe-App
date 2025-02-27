package com.example.recipebook.Post.api;

import com.example.recipebook.Post.model.PostModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostApiService {
    @GET("posts")
    Call<List<PostModel>> getPosts();

    @POST("posts")
    Call<PostModel> postData(@Body PostModel post);

    @PUT("posts/{id}")
    Call<PostModel> putPosts(@Path("id") int id, @Body PostModel post);

    @DELETE("posts/{id}")
    Call<Void> deletePosts(@Path("id") int id); // Correct annotation here

}
