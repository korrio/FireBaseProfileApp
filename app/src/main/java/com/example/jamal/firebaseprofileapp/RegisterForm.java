package com.example.jamal.firebaseprofileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class RegisterForm extends AppCompatActivity {
    private Spinner mCountries;
    private Spinner mGender;
    private Button mSaveProfile;
    private View.OnClickListener mSaveProfList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent userHome = new Intent(RegisterForm.this,UserHome.class);
            startActivity(userHome);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
        Init();
        InitListeners();
        InitSpinner();
    }
    private void Init()
    {
        mCountries = (Spinner) findViewById(R.id.spnrCountries);
        mGender = (Spinner) findViewById(R.id.spnrGender);
        mSaveProfile = (Button) findViewById(R.id.btnSaveProf);

    }
    private void InitListeners()
    {
        mSaveProfile.setOnClickListener(mSaveProfList);
    }

    private void InitSpinner()
    {
        String[] countries = {"Pakistan","USA","Afghanistan","United Kingdom"};
        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<String>(RegisterForm.this,R.layout.spinner_item,countries);
        mCountries.setAdapter(countriesAdapter);
        String[] gender = {"Male","Female"};
        ArrayAdapter<String> genderAdapter =new ArrayAdapter<String>(RegisterForm.this,R.layout.spinner_item,gender);
        mGender.setAdapter(genderAdapter);

    }
}
