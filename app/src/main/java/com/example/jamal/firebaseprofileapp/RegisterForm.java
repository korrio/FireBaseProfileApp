package com.example.jamal.firebaseprofileapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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
    private ImageView mUserImage;
    private int PICK_IMAGE_REQUEST = 1;
    private UserProfile mUserProfile = new UserProfile();
    private ProgressBar mImageProgress;
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();



    private View.OnClickListener mSaveProfList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(checkFields())
            {
                mUserProfile = getUserProfile();
                Toast.makeText(RegisterForm.this,mUserProfile.toString(),Toast.LENGTH_LONG).show();
                mDatabaseReference.child("Users").child(mUser.getUserName().toLowerCase()).child("active").setValue(true, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(RegisterForm.this,"user status has been changed",Toast.LENGTH_SHORT).show();
                        mDatabaseReference.child("UserProfiles").child(mUser.getUserName().toLowerCase()).setValue(mUserProfile, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Toast.makeText(RegisterForm.this,"user profile has been added",Toast.LENGTH_SHORT).show();
                                Intent userHome = new Intent(RegisterForm.this,UserHome.class);
                                Bundle bundle =new Bundle();
                                bundle.putParcelable("userProfile",Parcels.wrap(mUserProfile));
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
    private View.OnClickListener mUserImgLstnr = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            // Show only images, no videos or anything else
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            // Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        }
    };
    private void uploadImageToFirebase(Uri uri){
        Toast.makeText(RegisterForm.this, getFileName(uri), Toast.LENGTH_SHORT).show();
        Picasso.with(RegisterForm.this).load(uri)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).transform(new PicassoCircleTransformation())
                .into(mUserImage);
        //uploading image to firebase and retrieving uri to be stored in user profile
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStorageRef.child("profileImages/" + mUser.getUserName()+"_"+getFileName(uri));
        mImageProgress.setVisibility(View.VISIBLE);
        riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(RegisterForm.this,downloadUrl.toString(),Toast.LENGTH_SHORT).show();
                mUserProfile.setImageUrl(downloadUrl.toString());
                mImageProgress.setVisibility(View.GONE);
                /*mDatabaseReference.child("UserProfiles").child(mUser.getUserName().toLowerCase()).child("ImageUrl").setValue(downloadUrl.toString(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(RegisterForm.this,"User profile link saved",Toast.LENGTH_SHORT).show();

                    }
                });*/





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                //Toast.makeText(RegisterForm.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                //set default image for the user
                mUserProfile.setImageUrl("https://firebasestorage.googleapis.com/v0/b/fir-profileapp.appspot.com/o/profileImages%2Fuser.png?alt=media&token=94b61138-26c4-4e92-9253-ba576bf3151d");
                mImageProgress.setVisibility(View.GONE);


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null) {
            Uri uri = data.getData();
            uploadImageToFirebase(uri);

        }

    }
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private UserProfile getUserProfile() {
        mUserProfile.setUserName(mUser.getUserName());
        mUserProfile.setActive(true);
        mUserProfile.setFirstName(mFirstName.getText().toString());
        mUserProfile.setLastName(mLastName.getText().toString());
        mUserProfile.setGender(mGender.getSelectedItem().toString());
        mUserProfile.setCountry(mCountries.getSelectedItem().toString());
        mUserProfile.setBiography(mBiography.getText().toString());
        mUserProfile.setInterests(mInterests.getText().toString());
        mUserProfile.setPhoneNu(mPhoneNum.getText().toString());
        mUserProfile.setEmailAddress(mUser.getEmailAddress());
        if(mUserProfile.getImageUrl()==null)
        {
            mUserProfile.setImageUrl("https://firebasestorage.googleapis.com/v0/b/fir-profileapp.appspot.com/o/profileImages%2Fuser.png?alt=media&token=94b61138-26c4-4e92-9253-ba576bf3151d");
        }
        return mUserProfile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
        Init();
        InitListeners();
        InitSpinner();
        getUserObj();

    }
    private void getUserObj()
    {
        mUser = Parcels.unwrap(getIntent().getExtras().getParcelable("userObj"));

        if(getIntent().getExtras().containsKey("isEdit"))
        {
            boolean isEdit = getIntent().getExtras().getBoolean("isEdit");
            if(isEdit)
            {
                mUserProfile = Parcels.unwrap(getIntent().getExtras().getParcelable("userProfileEdit"));
                displayForm();
            }

        }else{
            mUserProfile = new UserProfile();
        }
    }
    private void displayForm()
    {
        mFirstName.setText(mUserProfile.getFirstName());
        mLastName.setText(mUserProfile.getLastName());
        mBiography.setText(mUserProfile.getBiography());
        mInterests.setText(mUserProfile.getInterests());
        mPhoneNum.setText(mUserProfile.getPhoneNu());
        Picasso.with(RegisterForm.this).load(mUserProfile.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).transform(new PicassoCircleTransformation())
                .into(mUserImage);
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
        mUserImage = (ImageView) findViewById(R.id.imgUserImg);
        mImageProgress = (ProgressBar) findViewById(R.id.prgImgUpload);

    }
    private void InitListeners()
    {
        mSaveProfile.setOnClickListener(mSaveProfList);
        mUserImage.setOnClickListener(mUserImgLstnr);
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
