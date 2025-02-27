package com.example.recipebook.Post.contract;

import com.example.recipebook.Post.model.PostModel;

import java.util.List;

public interface PostContract {
     interface View{
         void showPosts(List<PostModel> posts);
         void showError(String errorMessage);
         void showToast(String toastMessage);
         void showLoading();
         void hideLoading();

         void showPostDeleted(int postId);
     }

    interface Presenter{
         void loadPosts();
         void createPost(PostModel post);
         void updatePost(int id, PostModel post);
         void deletePost(int id,int position);

    }


}
