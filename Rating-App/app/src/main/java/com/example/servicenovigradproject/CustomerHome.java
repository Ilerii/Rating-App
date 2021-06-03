package com.example.servicenovigradproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class CustomerHome extends AppCompatActivity {
    final String TAG = "CustomerHome";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRootRef;
    private User cUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        // gets User data
        DatabaseReference userRef = mRootRef.child("users");
        if(mUser == null) {
            finish();
            startActivity(new Intent(CustomerHome.this, MainActivity.class));
        }
        else{
            final TextView LabelWelcome = findViewById(R.id.txtTitle);

            mRootRef.child("users/" + mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    cUser = snapshot.getValue(User.class);
                    try {
                        if (cUser == null) {
                            LabelWelcome.setText("Welcome to Service Novigrad. \n ERROR FINDING USER");
                        }
                        else if (cUser.getDisabledFlag()) { //If the account is disabled it will let them know
                            LabelWelcome.setText("Your account is currently disabled, please contact the administrator");
                        }
                        else {
                            LabelWelcome.setText("Welcome " + cUser.getFirstName() + "!");
                        }
                    }
                    catch(NullPointerException npe){
                        LabelWelcome.setText("Your account was nuked in the great nukening of Oct 30, 2020");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    mAuth.signOut();
                    finish();
                    startActivity(new Intent(CustomerHome.this, MainActivity.class));
                }
            });
        }

        // Logout button
        ((Button)findViewById(R.id.btnCustomerLogout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(CustomerHome.this, MainActivity.class));
            }
        });

        // Request Button
        ((Button)findViewById(R.id.btnRequest)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHome.this, FileRequest.class));
            }
        });

        // Rate Branch Button
        ((Button)findViewById(R.id.btnRate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHome.this, RateBranch.class));
            }
        });

        // Pending Request Button
        ((Button)findViewById(R.id.btnPendingRequests)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHome.this, CustomerPendingRequest.class));
            }
        });

    }
}