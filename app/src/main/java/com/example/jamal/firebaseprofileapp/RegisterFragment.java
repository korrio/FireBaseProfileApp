package com.example.jamal.firebaseprofileapp;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jamal on 9/9/2017.
 */

public class RegisterFragment extends Fragment {
    private EditText mUserName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegister;
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    private View.OnClickListener mRegLstnr = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(checkFields())
            {
                if(checkEmailFormat(mEmail.getText().toString().trim()))
                {
                    Toast.makeText(getContext(),"Email and everything is correct",Toast.LENGTH_SHORT).show();

                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("RegisterFrag", "onDataChange: "+dataSnapshot.child("Users").hasChild(mUserName.getText().toString().toLowerCase()));
                            if(!dataSnapshot.child("Users").hasChild(mUserName.getText().toString().toLowerCase()))
                            {
                                //add user here
                                User user = new User();
                                user.setUserName(mUserName.getText().toString().toLowerCase());
                                user.setEmailAddress(mEmail.getText().toString());
                                user.setPassword(mPassword.getText().toString());
                                user.setActive(false);
                                mDatabaseReference.child("Users").child(user.getUserName()).setValue(user);
                                Toast.makeText(getContext(),"User has been addedm use login tab to login",Toast.LENGTH_SHORT).show();
                                clearFields();
                            }else{
                                Toast.makeText(getContext(),"Sorry user already  exists",Toast.LENGTH_SHORT).show();
                                //show possible suggestions
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    /*User user = new User();
                    user.setUserName(mUserName.getText().toString().toLowerCase());
                    user.setEmailAddress(mEmail.getText().toString());
                    user.setPassword(mPassword.getText().toString());
                    mDatabaseReference.child("Users").child(user.getUserName()).setValue(user);
                    Toast.makeText(getContext(),"User has been successfully created",Toast.LENGTH_SHORT).show();*/

                }else{
                    Toast.makeText(getContext(),"Sorry email format is not valid", Toast.LENGTH_SHORT).show();
                    return;
                }

            }else{
                Toast.makeText(getContext(),"Please enter all fields",Toast.LENGTH_SHORT).show();
            }


        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register,container,false);
        initReferences(view);
        initListeners();
        return view;
    }
    private void initReferences(View view)
    {
        mUserName = view.findViewById(R.id.etUserName);
        mEmail = view.findViewById(R.id.etEmail);
        mPassword = view.findViewById(R.id.etPassword);
        mRegister = view.findViewById(R.id.btnRegister);

    }
    private void initListeners()
    {
        mRegister.setOnClickListener(mRegLstnr);
    }
    private boolean checkFields()
    {
        String userName = mUserName.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if(userName.isEmpty()||email.isEmpty()||password.isEmpty())
        {
            return false;
        }else{
            return true;
        }

    }
    private boolean checkEmailFormat(String email)
    {
        boolean flag = false;
        String regexExpr = "^[\\w!#$%&’*+/=\\-?^_`{|}~]+(\\.[\\w!#$%&’*+/=\\-?^_`{|}~]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern = Pattern.compile(regexExpr,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        flag = matcher.matches();
        return flag;
    }
    private void clearFields()
    {
        mUserName.setText("");
        mEmail.setText("");
        mPassword.setText("");

    }


}
