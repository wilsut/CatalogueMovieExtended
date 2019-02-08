package com.william.cataloguemovieapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.william.cataloguemovieapplication.BuildConfig;
import com.william.cataloguemovieapplication.activity.DetailActivity;
import com.william.cataloguemovieapplication.R;
import com.william.cataloguemovieapplication.adapter.MovieAdapter;
import com.william.cataloguemovieapplication.entity.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener, MovieAdapter.MovieDataListener {

    private static final String STATE_RESULT = "state_result";
    private ArrayList<Movie> movies;

    MovieAdapter movieAdapter;

    private RecyclerView rvMovie;
    private EditText etSearch;
    ProgressBar progressBar;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMovie = view.findViewById(R.id.rv_movie);
        etSearch = view.findViewById(R.id.et_search);
        Button btnSearch = view.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);

        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        rvMovie.setLayoutManager(new LinearLayoutManager(this.getContext()));
        movieAdapter = new MovieAdapter();
        movieAdapter.setMovieDataListener(this);
        rvMovie.setAdapter(movieAdapter);

        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList(STATE_RESULT);
            movieAdapter.setMovies(movies);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_RESULT, movies);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search) {
            String title = etSearch.getText().toString();
            searchMovies(title);
        }
    }

    @Override
    public void onMovieItemClicked(Movie movie) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    private void searchMovies(String title) {
        movies = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();

        String url = "https://api.themoviedb.org/3/search/movie?api_key=" +
                BuildConfig.TMDB_API_KEY + "&language=en-US&query=" + title;

        progressBar.setVisibility(View.VISIBLE);

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        Movie movie = new Movie(item);
                        movies.add(movie);
                    }
                    progressBar.setVisibility(View.GONE);
                    movieAdapter.setMovies(movies);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }
}
