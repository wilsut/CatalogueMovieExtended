package com.william.cataloguemovieapplication.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.william.cataloguemovieapplication.R;
import com.william.cataloguemovieapplication.activity.MainActivity;

public class PreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private String DAILY;
    private String RELEASE;

    private SwitchPreference dailyPreference;
    private SwitchPreference releasePreference;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        init();
        setSummaries();
    }

    private void init() {
        DAILY = getResources().getString(R.string.key_daily_reminder);
        RELEASE = getResources().getString(R.string.key_release_reminder);

        dailyPreference = (SwitchPreference) findPreference(DAILY);
        releasePreference = (SwitchPreference) findPreference(RELEASE);
    }

    private void setSummaries() {
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        dailyPreference.setChecked(sh.getBoolean(DAILY, true));
        releasePreference.setChecked(sh.getBoolean(RELEASE, true));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(DAILY)) {
            dailyPreference.setChecked(sharedPreferences.getBoolean(DAILY, true));
        }
        if (key.equals(RELEASE)) {
            releasePreference.setChecked(sharedPreferences.getBoolean(RELEASE, true));
        }
    }
}