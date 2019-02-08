package com.william.cataloguemovieapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.william.cataloguemovieapplication.R;
import com.william.cataloguemovieapplication.fragment.PreferenceFragment;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        getSupportFragmentManager().beginTransaction().add(R.id.setting_holder, new PreferenceFragment()).commit();
    }
}
