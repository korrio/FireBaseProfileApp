package com.example.jamal.firebaseprofileapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Jamal on 9/9/2017.
 */

public class LoginFragment extends Fragment {


    private View.OnClickListener mLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent registerForm = new Intent(getContext(),RegisterForm.class);
            startActivity(registerForm);

            Toast.makeText(getContext(),"Hello login",Toast.LENGTH_SHORT).show();


        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login,container,false);
        Button btn = (Button)view.findViewById(R.id.btnLogin);
        btn.setOnClickListener(mLoginListener);


        return view;
    }
}
