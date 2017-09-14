package com.example.jamal.firebaseprofileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Jamal on 9/9/2017.
 */

public class LoginFragment extends Fragment {
    private EditText mUserName;
    private EditText mPassword;
    private Button mLogin;
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();


    private View.OnClickListener mLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(checkFields())
            {
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("RegisterFrag", "onDataChange: "+dataSnapshot.child("Users").hasChild(mUserName.getText().toString().toLowerCase()));
                        if(dataSnapshot.child("Users").hasChild(mUserName.getText().toString().toLowerCase()))
                        {
                            User user = dataSnapshot.child("Users").child(mUserName.getText().toString().toLowerCase()).getValue(User.class);
                            Toast.makeText(getContext(),user.toString(),Toast.LENGTH_SHORT).show();
                            if(user.getUserName().toLowerCase().equals(mUserName.getText().toString().toLowerCase())&&user.getPassword().toLowerCase().equals(mPassword.getText().toString().toLowerCase())){
                                Toast.makeText(getContext(),"Username and password are okay",Toast.LENGTH_SHORT).show();
                                //now check if the active status is true or false
                                //if the active status is true so redirect to home screen
                                //if the active status is false so redirect to the profile screen so profile is first set up
                                if(user.isActive())
                                {
                                    //redirect to home screen
                                    //user object will be sent to this class with picture and other details
                                    Intent userHome = new Intent(getContext(),UserHome.class);
                                    startActivity(userHome);
                                }else{
                                    //redirect to profile screen
                                    //user object will be sent to the profile screen where it will be used
                                    //to populate some fields inside form
                                    Intent registerForm = new Intent(getContext(),RegisterForm.class);
                                    startActivity(registerForm);
                                }

                        }else{
                                Toast.makeText(getContext(),"sorry username or password is wrong",Toast.LENGTH_SHORT).show();

                            }


                        }else{
                            Toast.makeText(getContext(),"sorry username or password is wrong",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }else{
                Toast.makeText(getContext(),"Please enter username and password properly",Toast.LENGTH_SHORT).show();
            }




        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login,container,false);
        initReferences(view);
        initListeners();

        return view;
    }
    private void initReferences(View view)
    {
        mUserName = view.findViewById(R.id.etUserName);
        mPassword = view.findViewById(R.id.etPassword);
        mLogin = view.findViewById(R.id.btnLogin);
    }
    private void initListeners()
    {
        mLogin.setOnClickListener(mLoginListener);

    }
    private boolean checkFields()
    {
        String userName = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if(userName.isEmpty()||password.isEmpty())
        {
            return false;
        }else{
            return true;
        }

    }
}
