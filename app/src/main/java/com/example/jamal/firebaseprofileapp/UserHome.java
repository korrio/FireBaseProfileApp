package com.example.jamal.firebaseprofileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.File;

public class UserHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView mUserFullName;
    private TextView mUserEmailAddrs;
    private ImageView mProfileImage;
    private NavigationView mNavigationView;
    private UserProfile mUserProfile;
    public static final String MY_PREFS_NAME = "UserInformation";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("User Home Screen");
        setSupportActionBar(toolbar);
        init();
        saveUserLocally();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSharedPreferences();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(UserHome.this,"You will be redirected to shared preference screen",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserHome.this,MyPreferenceActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navEditProfile) {
            Toast.makeText(UserHome.this,"User profile will be edited here",Toast.LENGTH_SHORT).show();
            User userObj = new User();
            userObj.setUserName(mUserProfile.getUserName());
            userObj.setEmailAddress(mUserProfile.getEmailAddress());
            Intent registerForm = new Intent(UserHome.this,RegisterForm.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("userObj",Parcels.wrap(userObj));
            bundle.putParcelable("userProfileEdit",Parcels.wrap(mUserProfile));
            bundle.putBoolean("isEdit",true);
            registerForm.putExtras(bundle);
            startActivity(registerForm);
        } else if (id == R.id.navLogout) {
            File preferenceFile = new File(
                    "/data/data/"+getPackageName()+"/shared_prefs/"+MY_PREFS_NAME+".xml");
            if(preferenceFile.exists())
            {
                if(preferenceFile.delete())
                {
                    Toast.makeText(UserHome.this, "You will be redirected back to login screen", Toast.LENGTH_SHORT).show();
                    Intent loginScreen = new Intent(UserHome.this,MainActivity.class);
                    startActivity(loginScreen);
                }
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void checkSharedPreferences()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UserHome.this);
        boolean isUpdatesEnabled = sharedPreferences.getBoolean("updates",true);
        boolean isBugReportEnabled = sharedPreferences.getBoolean("bugreport",true);
        if(isUpdatesEnabled)
        {
            Toast.makeText(UserHome.this,"Updates are enabled for this application",Toast.LENGTH_SHORT).show();
        }
        if(isBugReportEnabled)
        {
            Toast.makeText(UserHome.this,"Bug reports are enabled for this application",Toast.LENGTH_SHORT).show();
        }
    }
    private void saveUserLocally()
    {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
        editor.putString("UserName",mUserProfile.getUserName());
        editor.putString("FirstName",mUserProfile.getFirstName());
        editor.putString("LastName",mUserProfile.getLastName());
        editor.putString("Biography", mUserProfile.getBiography());
        editor.putString("EmailAddress",mUserProfile.getEmailAddress());
        editor.putString("ImageUrl",mUserProfile.getImageUrl());
        editor.putString("Interests",mUserProfile.getInterests());
        editor.putString("Country",mUserProfile.getCountry());
        editor.putString("Gender",mUserProfile.getGender());
        editor.putString("PhonNu",mUserProfile.getPhoneNu());
        editor.apply();
    }
    private void init()
    {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = mNavigationView.getHeaderView(0);
        mUserFullName = hView.findViewById(R.id.txtFullName);
        mUserEmailAddrs = hView.findViewById(R.id.txtEmailAddrs);
        mProfileImage = hView.findViewById(R.id.imgProf);
        if(getIntent().getExtras()!=null)
        {
            if(getIntent().getExtras().containsKey("userProfile")){
                mUserProfile = Parcels.unwrap(getIntent().getExtras().getParcelable("userProfile"));
                Log.d("UserHome", "init: "+Parcels.unwrap(getIntent().getExtras().getParcelable("userProfile")).toString());
                mUserFullName.setText(mUserProfile.getFirstName()+" "+mUserProfile.getLastName());
                mUserEmailAddrs.setText(mUserProfile.getEmailAddress());
                Picasso.with(UserHome.this).load(mUserProfile.getImageUrl())
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher).transform(new PicassoCircleTransformation())
                        .into(mProfileImage);
            }
        }else{
            SharedPreferences editor = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
            mUserProfile = new UserProfile();
            mUserProfile.setUserName(editor.getString("UserName",""));
            mUserProfile.setFirstName(editor.getString("FirstName",""));
            mUserProfile.setLastName(editor.getString("LastName",""));
            mUserProfile.setBiography(editor.getString("Biography", ""));
            mUserProfile.setEmailAddress(editor.getString("EmailAddress",""));
            mUserProfile.setImageUrl(editor.getString("ImageUrl",""));
            mUserProfile.setInterests(editor.getString("Interests",""));
            mUserProfile.setCountry(editor.getString("Country",""));
            mUserProfile.setGender(editor.getString("Gender",""));
            mUserProfile.setPhoneNu(editor.getString("PhonNu",""));
            mUserFullName.setText(mUserProfile.getFirstName()+" "+mUserProfile.getLastName());
            mUserEmailAddrs.setText(mUserProfile.getEmailAddress());
            Picasso.with(UserHome.this).load(mUserProfile.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher).transform(new PicassoCircleTransformation())
                    .into(mProfileImage);

        }


    }
}
