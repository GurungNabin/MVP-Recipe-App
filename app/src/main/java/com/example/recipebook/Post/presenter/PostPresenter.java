package com.example.recipebook.Post.presenter;

import android.util.Log;

import com.example.recipebook.Post.api.PostApiService;
import com.example.recipebook.Post.api.PostClient;
import com.example.recipebook.Post.contract.PostContract;
import com.example.recipebook.Post.model.PostModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostPresenter implements PostContract.Presenter {
    private final PostContract.View view;
    private final PostApiService apiService;

    public PostPresenter(PostContract.View view, PostApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void loadPosts() {
        view.showLoading();
        apiService.getPosts().enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                view.hideLoading();
                if(response.isSuccessful() && response.body() != null){
                    view.showPosts(response.body());
                }else {
                    view.showError("Failed to load posts.");
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                view.hideLoading();
                view.showError(t.getMessage());
            }
        });
    }

    @Override
    public void createPost(PostModel post) {
        view.showLoading();
        apiService.postData(post).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                view.hideLoading();
                if(response.isSuccessful()){
                    loadPosts();
                }else {
                    view.showError("Failed to create post.");
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                view.hideLoading();
                view.showError(t.getMessage());
            }
        });
    }



    @Override
    public void deletePost(int id, int position) {
        view.showLoading();
        apiService.deletePosts(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                view.hideLoading();
                if (response.isSuccessful()) {
                    view.showToast("Post deleted successfully");

                    view.showPostDeleted(position);
                } else {
                    view.showError("Failed to delete post.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                view.hideLoading();
                view.showError(t.getMessage());
            }
        });
    }





    @Override
    public void updatePost(int id, PostModel post) {
        view.showLoading();
        apiService.putPosts(id, post).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                view.hideLoading();
                if (response.isSuccessful()) {
                    loadPosts();
                } else {
                    view.showError("Failed to update post.");
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                view.hideLoading();
                view.showError(t.getMessage());
            }
        });
    }



}