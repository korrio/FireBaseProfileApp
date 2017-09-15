package com.example.jamal.firebaseprofileapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
            if (checkFields())
            {
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("RegisterFrag", "onDataChange: "+dataSnapshot.child("Users").hasChild(mUser.getUserName().toLowerCase()));
                        if(!dataSnapshot.child("UsersProfiles").hasChild(mUser.getUserName().toLowerCase()))
                        {
                            //add user here
                            UserProfile userProfile = getUserProfile();
                            mDatabaseReference.child("UsersProfiles").child(mUser.getUserName()).setValue(userProfile);
                            changeUserStatus(mUser.getUserName(),true); //make user active
                            Toast.makeText(RegisterForm.this,"User profile has been added you will be redirected to the home page",Toast.LENGTH_SHORT).show();
                            //change the active status of the user
                            //send user to the home screen
                            Parcelable wrapped = Parcels.wrap(userProfile);
                            Intent userHome = new Intent(RegisterForm.this,UserHome.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("userProfileObj",wrapped);
                            startActivity(userHome,bundle);

                        }else{
                            Toast.makeText(RegisterForm.this,"Sorry user profile already  exists",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }else{
                Toast.makeText(RegisterForm.this,"Please enter all the fields",Toast.LENGTH_SHORT).show();
            }


            Intent userHome = new Intent(RegisterForm.this,UserHome.class);
            startActivity(userHome);

        }
    };

    @NonNull
    private UserProfile getUserProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserName(mUser.getUserName());
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
        mUser = Parcels.unwrap(getIntent().getParcelableExtra("userObj"));
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
    private void changeUserStatus(final String userName, final boolean status)
    {
        //this will change the status of the user
        mDatabaseReference.child("Users").child(userName.toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabaseReference.child("Users").child(userName.toLowerCase()).child("Active").setValue(status);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
