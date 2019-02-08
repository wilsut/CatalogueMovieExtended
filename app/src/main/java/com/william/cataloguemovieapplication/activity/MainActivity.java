package com.william.cataloguemovieapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.william.cataloguemovieapplication.util.MovieAsyncTask;
import com.william.cataloguemovieapplication.R;
import com.william.cataloguemovieapplication.entity.Movie;
import com.william.cataloguemovieapplication.fragment.FavouriteFragment;
import com.william.cataloguemovieapplication.fragment.NowPlayingFragment;
import com.william.cataloguemovieapplication.fragment.SearchFragment;
import com.william.cataloguemovieapplication.fragment.UpComingFragment;
import com.william.cataloguemovieapplication.scheduler.DailyReminder;
import com.william.cataloguemovieapplication.scheduler.ReleasedTodayReminder;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private DailyReminder dailyReminder = new DailyReminder();
    private ReleasedTodayReminder releasedTodayReminder = new ReleasedTodayReminder();
    private ArrayList<Movie> movies = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_now_playing:
                    showNowPlaying();
                    return true;
                case R.id.navigation_up_coming:
                    showUpComing();
                    return true;
                case R.id.navigation_search_movie:
                    showSearchMovie();
                    return true;
                case R.id.navigation_favorite:
                    showFavourite();
                    return true;
            }
            return false;
        }
    };

    private void showNowPlaying() {
        getSupportActionBar().setTitle(R.string.now_playing);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        NowPlayingFragment mNowPlayingFragment = new NowPlayingFragment();

        for (Fragment fragment:getSupportFragmentManager().getFragments()) {
            mFragmentManager.beginTransaction().remove(fragment).commit();
        }

        mFragmentTransaction.add(R.id.frame_container, mNowPlayingFragment, NowPlayingFragment.class.getSimpleName());
        mFragmentTransaction.commit();
    }

    private void showUpComing() {
        getSupportActionBar().setTitle(R.string.up_coming);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        UpComingFragment mUpComingFragment = new UpComingFragment();

        for (Fragment fragment:getSupportFragmentManager().getFragments()) {
            mFragmentManager.beginTransaction().remove(fragment).commit();
        }

        mFragmentTransaction.add(R.id.frame_container, mUpComingFragment, UpComingFragment.class.getSimpleName());
        mFragmentTransaction.commit();
    }

    private void showSearchMovie() {
        getSupportActionBar().setTitle(R.string.search_movie);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        SearchFragment mSearchFragment = new SearchFragment();

        for (Fragment fragment:getSupportFragmentManager().getFragments()) {
            mFragmentManager.beginTransaction().remove(fragment).commit();
        }

        mFragmentTransaction.add(R.id.frame_container, mSearchFragment, SearchFragment.class.getSimpleName());
        mFragmentTransaction.commit();
    }

    private void showFavourite() {
        getSupportActionBar().setTitle(R.string.favorite);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        FavouriteFragment mFavouriteFragment = new FavouriteFragment();

        for (Fragment fragment:getSupportFragmentManager().getFragments()) {
            mFragmentManager.beginTransaction().remove(fragment).commit();
        }

        mFragmentTransaction.add(R.id.frame_container, mFavouriteFragment, FavouriteFragment.class.getSimpleName());
        mFragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setReminder();

        if (savedInstanceState == null)
            showNowPlaying();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_settings:
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                return true;
            case R.id.action_reminder_settings:
                intent = new Intent(this, PreferenceActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setReminder() {
        String DAILY = getString(R.string.key_daily_reminder);
        String RELEASE = getString(R.string.key_release_reminder);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (sharedPreferences.getBoolean(DAILY, true))
            dailyReminder.setAlarm(this);
        else
            dailyReminder.cancelAlarm(this);

        if (sharedPreferences.getBoolean(RELEASE, true)) {
            MovieAsyncTask task = new MovieAsyncTask();
            try {
                movies = task.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            releasedTodayReminder.setAlarm(this, movies);
        }
        else
            releasedTodayReminder.cancelAlarm(this);
    }
}
