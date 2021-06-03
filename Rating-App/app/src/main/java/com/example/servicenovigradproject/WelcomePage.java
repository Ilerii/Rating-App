package com.example.servicenovigradproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomePage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRootRef;
    private User cUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        if(!(mUser != null)){
            finish();
            startActivity(new Intent(WelcomePage.this, MainActivity.class));
        }

        else {
            final TextView LabelWelcome = findViewById(R.id.textWelcomeTitle);
            final TextView LabelUID = findViewById(R.id.textUID);
            final TextView LabelRole = findViewById(R.id.textRole);

            mRootRef.child("users/" + mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    cUser = snapshot.getValue(User.class);
                    try {
                        if (cUser == null) {
                            LabelWelcome.setText("Welcome to Service Novigrad. \n ERROR FINDING USER");
                            LabelRole.setText("Your role is: UNKNOWN");
                            LabelUID.setText("UID: UNKNOWN");
                        }
                        else if (cUser.getDisabledFlag()) { //If the account is disabled it will let them know
                            LabelWelcome.setText("Your account is currently disabled, please contact the administrator");
                            LabelRole.setText("");
                            LabelUID.setText("");
                        }
                        else {
                            LabelWelcome.setText("Welcome to Service Novigrad " + cUser.getFirstName() + "!");
                            LabelRole.setText("Your role is " + cUser.getUserType());
                            LabelUID.setText("UID: " + mUser.getUid());
                        }
                    }
                    catch(NullPointerException npe){
                        LabelWelcome.setText("Your account was nuked in the great nukening of Oct 30, 2020");
                        LabelRole.setText("Logout, and make a new account");
                    }
                    ((Button)findViewById(R.id.logoutButton)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAuth.signOut();
                            finish();
                            startActivity(new Intent(WelcomePage.this, MainActivity.class));
                        }
                    });
                    ((Button)findViewById(R.id.continueButton)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (cUser.getUserType()){
                                case "Employee":
                                    finish();
                                    startActivity(new Intent(WelcomePage.this, EmployeeHome.class));
                                    break;
                                case "Customer":
                                    startActivity(new Intent(WelcomePage.this, CustomerHome.class));
                                    break;
                                case "admin":
                                    finish();
                                    startActivity(new Intent(WelcomePage.this, AdminHome.class));
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    mAuth.signOut();
                    finish();
                    startActivity(new Intent(WelcomePage.this, MainActivity.class));
                }
            });
        }
    }
}