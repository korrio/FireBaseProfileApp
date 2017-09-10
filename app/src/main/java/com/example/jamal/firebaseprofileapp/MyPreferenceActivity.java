package com.example.jamal.firebaseprofileapp;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Jamal on 9/10/2017.
 */

public class MyPreferenceActivity extends android.preference.PreferenceActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new PreferenceFragment()).commit();
    }
    public static class PreferenceFragment extends android.preference.PreferenceFragment{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
