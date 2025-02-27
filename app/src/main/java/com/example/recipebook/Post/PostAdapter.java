package com.example.recipebook.Post;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.Post.contract.PostContract;
import com.example.recipebook.Post.model.PostModel;
import com.example.recipebook.R;

import java.util.ArrayList;
import java.util.List;

public class  PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<PostModel> posts = new ArrayList<>();
    private PostContract.Presenter presenter;

    public void setPosts(List<PostModel> posts, PostContract.Presenter presenter){
        this.posts = posts;
        this.presenter = presenter;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        PostModel post = posts.get(position);
        holder.postId.setText(String.valueOf(post.getId()));
        holder.postTitle.setText(post.getTitle());
        holder.postBody.setText(post.getBody());

        holder.deleteButton.setOnClickListener(v -> {
            if (presenter != null) {
                presenter.deletePost(post.getId(),position);
            }
        });
        holder.updateButton.setOnClickListener(v -> {
            if (presenter != null) {
                PostModel updatePost = new PostModel(post.getId(), "Updated Title", "Updated body");
                presenter.updatePost(post.getId(), updatePost);
                post.setTitle("Updated Title");
                post.setBody("Updated body");
                notifyItemChanged(position); // Notify adapter about the update
            }
        });
    }


    @Override
    public int getItemCount() {
        return posts != null ? posts.size() : 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postId, postTitle, postBody;
        Button deleteButton, updateButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postId = itemView.findViewById(R.id.postId);
            postTitle = itemView.findViewById(R.id.postTitle);
            postBody = itemView.findViewById(R.id.postBody);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            updateButton = itemView.findViewById(R.id.updateButton);
        }
    }
}
