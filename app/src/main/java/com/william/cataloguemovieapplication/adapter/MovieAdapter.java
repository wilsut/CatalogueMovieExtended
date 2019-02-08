package com.william.cataloguemovieapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.william.cataloguemovieapplication.R;
import com.william.cataloguemovieapplication.entity.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private ArrayList<Movie> movies;
    private MovieDataListener movieDataListener;

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_item, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int i) {
        final Movie movie = movies.get(i);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185" + movie.getPosterPath())
                .into(holder.ivPoster);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieDataListener.onMovieItemClicked(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movies == null)
            return 0;
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle;
        TextView tvOverview;
        TextView tvReleaseDate;
        TextView tvPopularity;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvOverview = itemView.findViewById(R.id.tv_overview);
        }
    }

    public interface MovieDataListener {
        void onMovieItemClicked(Movie movie);
    }

    public ArrayList<Movie> getMovies() {
        if (movies == null) {
            movies = new ArrayList<>();
        }
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.getMovies().clear();
        this.getMovies().addAll(movies);
        notifyDataSetChanged();
    }

    public MovieDataListener getMovieDataListener() {
        return movieDataListener;
    }

    public void setMovieDataListener(MovieDataListener movieDataListener) {
        this.movieDataListener = movieDataListener;
    }
}
