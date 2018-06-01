package com.pasha.termo.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.pasha.termo.R;

public class SettingsActivity extends PreferenceActivity {
    public static final String KEY_PREF_THEME_WIDGET = "pref_theme_widget";
    public static final String KEY_PREF_THEME_NOTIFICATION = "pref_theme_notification";
    public static final String KEY_PREF_IS_NOTIFICATION = "pref_is_notification1";
    public static final String KEY_PREF_DIGITS_NOTIFICATION = "pref_digits_notification";
    public static final String KEY_PREF_NOTIFICATION_GRAPH = "pref_notification_graph";
    public static final String KEY_PREF_NOTIFICATION_SHOW_TERMO = "pref_notification_show_termo1";
    public static final String KEY_PREF_NOTIFICATION_SHOW_IAO = "pref_notification_show_iao1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}