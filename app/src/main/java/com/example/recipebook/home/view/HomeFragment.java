package com.example.recipebook.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.R;
import com.example.recipebook.home.HomeContract;
import com.example.recipebook.home.model.HomeModel;
import com.example.recipebook.home.presenter.HomePresenter;

public class HomeFragment extends Fragment implements HomeContract.View {

    private TextView textView;

    private Button button;

    private ProgressBar progressBar;

    HomeContract.Presenter presenter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.home_main, container, false);

        textView = view.findViewById(R.id.homeTextView);
        button = view.findViewById(R.id.button);
        progressBar = view.findViewById(R.id.progressBar);
        presenter = new HomePresenter(this, new HomeModel());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Set LayoutManager

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onButtonClick();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
    }


    @Override
    public void setString(String string) {
        textView.setText(string);
    }
}
