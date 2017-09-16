package com.example.jamal.firebaseprofileapp;

import org.parceler.Parcel;

/**
 * Created by Jamal on 9/15/2017.
 */
@Parcel
public class UserProfile {
    private String mUserName;
    private String mFirstName;
    private String mLastName;
    private String mGender;
    private String mCountry;
    private String mBiography;
    private String mInterests;
    private boolean mIsActive;

    public boolean isActive() {
        return mIsActive;
    }

    public void setActive(boolean active) {
        mIsActive = active;
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

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    private String mPhoneNum;
    private String mEmailAddress;
    private String mImageUrl;
    public UserProfile()
    {

    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getBiography() {
        return mBiography;
    }

    public void setBiography(String biography) {
        mBiography = biography;
    }

    public String getInterests() {
        return mInterests;
    }

    public void setInterests(String interests) {
        mInterests = interests;
    }

    public String getPhoneNu() {
        return mPhoneNum;
    }

    public void setPhoneNu(String phoneNu) {
        mPhoneNum = phoneNu;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "mUserName='" + mUserName + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mGender='" + mGender + '\'' +
                ", mCountry='" + mCountry + '\'' +
                ", mBiography='" + mBiography + '\'' +
                ", mInterests='" + mInterests + '\'' +
                ", mIsActive=" + mIsActive +
                ", mPhoneNum='" + mPhoneNum + '\'' +
                ", mEmailAddress='" + mEmailAddress + '\'' +
                ", mImageUrl='" + mImageUrl + '\'' +
                '}';
    }
}
