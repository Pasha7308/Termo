package com.pasha.termo.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.pasha.termo.R;

public class SettingsActivity extends PreferenceActivity {
    public static final String KEY_PREF_THEME = "pref_theme";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}