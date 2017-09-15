package com.example.jamal.firebaseprofileapp;

import org.parceler.Parcel;

/**
 * Created by Jamal on 9/14/2017.
 */

@Parcel
public class User {
    private String mUserName;
    private String mEmailAddress;
    private String mPassword;
    private boolean mIsActive;

    public boolean isActive() {
        return mIsActive;
    }

    public void setActive(boolean active) {
        mIsActive = active;
    }

    public User()
    {

    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        mEmailAddress = emailAddress;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "mUserName='" + mUserName + '\'' +
                ", mEmailAddress='" + mEmailAddress + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mIsActive=" + mIsActive +
                '}';
    }
}
