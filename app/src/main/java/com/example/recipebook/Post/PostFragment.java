package com.example.recipebook.Post;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.Post.api.PostApiService;
import com.example.recipebook.Post.api.PostClient;
import com.example.recipebook.Post.contract.PostContract;
import com.example.recipebook.Post.model.PostModel;
import com.example.recipebook.Post.presenter.PostPresenter;
import com.example.recipebook.R;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment implements PostContract.View {

    private RecyclerView postRecycleView;
    private PostAdapter postAdapter;
    private PostPresenter postPresenter;
    private List<PostModel> posts = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_card_main, container, false);
        PostApiService apiService = PostClient.getPostService();

        postRecycleView = view.findViewById(R.id.postRecycleView);
        postRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        postAdapter = new PostAdapter();
        postRecycleView.setAdapter(postAdapter);

        postPresenter = new PostPresenter(this, apiService);
        postPresenter.loadPosts();

        view.findViewById(R.id.idFabAddPost).setOnClickListener(v -> showAddPostDialog()
        );
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showPosts(List<PostModel> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        postAdapter.setPosts(this.posts, postPresenter);
        postAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Post loaded successfully!", Toast.LENGTH_SHORT).show();
    }


    private void showAddPostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_post, null);
        builder.setView(dialogView);

        EditText titleInput = dialogView.findViewById(R.id.editTitle);
        EditText bodyInput = dialogView.findViewById(R.id.editBody);
        Button addButton = dialogView.findViewById(R.id.btnAdd);

        AlertDialog dialog = builder.create();

        addButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String body = bodyInput.getText().toString().trim();

            if (!title.isEmpty() && !body.isEmpty()) {
                PostModel newPost = new PostModel(0, title, body);
                postPresenter.createPost(newPost);
                Toast.makeText(getContext(), "Post added successfully!", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage != null ? errorMessage : "An error occurred", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String toastMessage) {

    }


    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showPostDeleted(int po) {
        posts.remove(po);
        postAdapter.setPosts(this.posts, postPresenter);
        postAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Post deleted successfully!", Toast.LENGTH_SHORT).show();
    }


}
