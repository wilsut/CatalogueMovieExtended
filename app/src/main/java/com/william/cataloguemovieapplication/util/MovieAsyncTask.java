package com.william.cataloguemovieapplication.util;

import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.william.cataloguemovieapplication.BuildConfig;
import com.william.cataloguemovieapplication.entity.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    @Override
    protected ArrayList<Movie> doInBackground(String... strings) {
        final ArrayList<Movie> movies = new ArrayList<>();

        SyncHttpClient client = new SyncHttpClient();

        String url;
        url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" +
                BuildConfig.TMDB_API_KEY + "&language=en-US";

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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        return movies;
    }
}
