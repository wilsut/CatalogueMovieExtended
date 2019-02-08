package com.wilsut.favorite.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.wilsut.favorite.DetailActivity;
import com.wilsut.favorite.R;
import com.wilsut.favorite.adapter.MovieAdapter;
import com.wilsut.favorite.entity.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.BaseColumns._ID;
import static com.wilsut.favorite.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.wilsut.favorite.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.wilsut.favorite.db.DatabaseContract.MovieColumns.POPULARITY;
import static com.wilsut.favorite.db.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.wilsut.favorite.db.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.wilsut.favorite.db.DatabaseContract.MovieColumns.TITLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment implements MovieAdapter.MovieDataListener {

    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private MovieAdapter movieAdapter;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovie.setLayoutManager(new LinearLayoutManager(this.getContext()));
        movieAdapter = new MovieAdapter();
        movieAdapter.setMovieDataListener(this);
        rvMovie.setAdapter(movieAdapter);

        new LoadNoteAsync().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadNoteAsync().execute();
    }

    @Override
    public void onMovieItemClicked(Movie movie) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    private class LoadNoteAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getActivity().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor movies) {
            super.onPostExecute(movies);
            progressBar.setVisibility(View.GONE);

            ArrayList<Movie> list = populateMovie(movies);
            movieAdapter.setMovies(list);
            movieAdapter.notifyDataSetChanged();

            if (list.size() == 0) {
                showSnackbarMessage("Tidak ada data saat ini");
            }
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvMovie, message, Snackbar.LENGTH_SHORT).show();
    }

    private ArrayList<Movie> populateMovie(Cursor cursor) {
        ArrayList<Movie> arrayList = new ArrayList();
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                movie.setPopularity(cursor.getInt(cursor.getColumnIndexOrThrow(POPULARITY)));

                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }
}
