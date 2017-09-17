package com.example.jamal.firebaseprofileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

public class RegisterForm extends AppCompatActivity {
    private EditText mFirstName;
    private  EditText mLastName;
    private EditText mBiography;
    private EditText mInterests;
    private EditText mPhoneNum;
    private Spinner mCountries;
    private Spinner mGender;
    private Button mSaveProfile;
    private User mUser;
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();



    private View.OnClickListener mSaveProfList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(checkFields())
            {
                Toast.makeText(RegisterForm.this,"working",Toast.LENGTH_SHORT).show();
                mDatabaseReference.child("Users").child(mUser.getUserName().toLowerCase()).child("active").setValue(true, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(RegisterForm.this,"user status has been changed",Toast.LENGTH_SHORT).show();
                        mDatabaseReference.child("UserProfiles").child(mUser.getUserName().toLowerCase()).setValue(getUserProfile(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Toast.makeText(RegisterForm.this,"user profile has been added",Toast.LENGTH_SHORT).show();
                                Intent userHome = new Intent(RegisterForm.this,UserHome.class);
                                Bundle bundle =new Bundle();
                                bundle.putParcelable("userProfile",Parcels.wrap(getUserProfile()));
                                userHome.putExtras(bundle);
                                startActivity(userHome);
                                finish();


                            }
                        });


                    }
                });


            }


        }
    };

    @NonNull
    private UserProfile getUserProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserName(mUser.getUserName());
        userProfile.setActive(true);
        userProfile.setFirstName(mFirstName.getText().toString());
        userProfile.setLastName(mLastName.getText().toString());
        userProfile.setGender(mGender.getSelectedItem().toString());
        userProfile.setCountry(mCountries.getSelectedItem().toString());
        userProfile.setBiography(mBiography.getText().toString());
        userProfile.setInterests(mInterests.getText().toString());
        userProfile.setPhoneNu(mPhoneNum.getText().toString());
        userProfile.setImageUrl("url to image");
        userProfile.setEmailAddress(mUser.getEmailAddress());
        return userProfile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
        getUserObj();
        Init();
        InitListeners();
        InitSpinner();

    }
    private void getUserObj()
    {
        mUser = Parcels.unwrap(getIntent().getExtras().getParcelable("userObj"));
    }
    private void Init()
    {
        mFirstName = (EditText) findViewById(R.id.etFirstName);
        mLastName = (EditText) findViewById(R.id.etLastName);
        mBiography = (EditText) findViewById(R.id.etBio);
        mInterests = (EditText) findViewById(R.id.etIntrests);
        mPhoneNum = (EditText) findViewById(R.id.etPhone);
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
    private boolean checkFields()
    {
        String firstName = mFirstName.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String biography=mBiography.getText().toString().trim();
        String interests=mInterests.getText().toString().trim();
        String phoneNum=mPhoneNum.getText().toString().trim();
        if(firstName.isEmpty()||lastName.isEmpty()||biography.isEmpty()||interests.isEmpty()||phoneNum.isEmpty())
        {
            return false;
        }else{
            return true;
        }

    }

    private UserProfile dummyMethod()
    {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserName("jamal");
        userProfile.setBiography("abc");
        userProfile.setFirstName("Jamal Hussain");
        userProfile.setLastName("Khattak");
        userProfile.setEmailAddress("jamal@yahoo.com");
        return userProfile;

    }
}
