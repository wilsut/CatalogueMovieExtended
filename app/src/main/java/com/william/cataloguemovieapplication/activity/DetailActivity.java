package com.william.cataloguemovieapplication.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.william.cataloguemovieapplication.R;
import com.william.cataloguemovieapplication.entity.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.provider.BaseColumns._ID;
import static com.william.cataloguemovieapplication.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.william.cataloguemovieapplication.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.william.cataloguemovieapplication.db.DatabaseContract.MovieColumns.POPULARITY;
import static com.william.cataloguemovieapplication.db.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.william.cataloguemovieapplication.db.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.william.cataloguemovieapplication.db.DatabaseContract.MovieColumns.TITLE;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";
    private Movie movie;

    @BindView(R.id.fab_favorite)
    FloatingActionButton fabFavorite;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_overview)
    TextView tvOverview;
    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.tv_popularity)
    TextView tvPopularity;
    @BindView(R.id.iv_poster)
    ImageView ivPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvPopularity.setText(getString(R.string.popularity, Integer.toString(movie.getPopularity())));
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w342" + movie.getPosterPath())
                .into(ivPoster);
    }

    @OnClick(R.id.fab_favorite)
    public void saveToFavorite() {
        Cursor cursor = getContentResolver().query(Uri.parse(CONTENT_URI + "/" + movie.getId()), null, null, null, null);
        if (cursor.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(_ID, movie.getId());
            contentValues.put(TITLE, movie.getTitle());
            contentValues.put(OVERVIEW, movie.getOverview());
            contentValues.put(POSTER_PATH, movie.getPosterPath());
            contentValues.put(RELEASE_DATE, movie.getReleaseDate());
            contentValues.put(POPULARITY, movie.getPopularity());
            getContentResolver().insert(CONTENT_URI, contentValues);
            Toast.makeText(DetailActivity.this, movie.getTitle() + " save to favorite", Toast.LENGTH_LONG).show();
        } else {
            long deleted = getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + movie.getId()), null, null);
            if (deleted > 0) {
                getContentResolver().notifyChange(CONTENT_URI, null);
            }
            Toast.makeText(DetailActivity.this, movie.getTitle() + " remove from favorite", Toast.LENGTH_LONG).show();
        }
    }
}
