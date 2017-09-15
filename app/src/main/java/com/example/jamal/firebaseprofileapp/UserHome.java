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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

public class UserHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView mUserFullName;
    private TextView mUserEmailAddrs;
    private ImageView mProfileImage;
    private NavigationView mNavigationView;
    private UserProfile mUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("User Home Screen");
        setSupportActionBar(toolbar);
        init();

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
        } else if (id == R.id.navLogout) {
            Toast.makeText(UserHome.this, "You will be redirected back to login screen", Toast.LENGTH_SHORT).show();
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
    private void init()
    {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = mNavigationView.getHeaderView(0);
        mUserFullName = hView.findViewById(R.id.txtFullName);
        mUserEmailAddrs = hView.findViewById(R.id.txtEmailAddrs);
        mProfileImage = hView.findViewById(R.id.imgProf);
        mUserProfile = Parcels.unwrap(getIntent().getParcelableExtra("userProfileObj"));
        mUserFullName.setText(mUserProfile.getFirstName()+" "+mUserProfile.getLastName());
        mUserEmailAddrs.setText(mUserProfile.getEmailAddress());
        //profile image will be set later


    }
}
