package com.example.servicenovigradproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser != null){
           startActivity(new Intent(MainActivity.this, WelcomePage.class));
        }
    }


   public void clickLogin(View view){
       startActivity(new Intent(MainActivity.this, Login.class));
   }

   public void clickRegister(View view){
        startActivity(new Intent(MainActivity.this, Register.class));
   }
}