package com.william.cataloguemovieapplication.widget;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.william.cataloguemovieapplication.R;
import com.william.cataloguemovieapplication.entity.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.provider.BaseColumns._ID;
import static com.william.cataloguemovieapplication.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.william.cataloguemovieapplication.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.william.cataloguemovieapplication.db.DatabaseContract.MovieColumns.POPULARITY;
import static com.william.cataloguemovieapplication.db.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.william.cataloguemovieapplication.db.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.william.cataloguemovieapplication.db.DatabaseContract.MovieColumns.TITLE;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<Movie> movies = new ArrayList<>();
    private final Context mContext;

    StackRemoteViewsFactory(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();

        Cursor cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
        movies = populateMovie(cursor);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        try {
            Bitmap preview = Glide.with(mContext)
                    .asBitmap()
                    .load("https://image.tmdb.org/t/p/w185" + movies.get(position).getPosterPath())
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
            rv.setImageViewBitmap(R.id.imageView, preview);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        rv.setTextViewText(R.id.tv_title, movies.get(position).getTitle());

//        Bundle extras = new Bundle();
//        extras.putInt(FavoriteMovieWidget.EXTRA_ITEM, position);
//        Intent fillInIntent = new Intent();
//        fillInIntent.putExtras(extras);
//
//        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
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
