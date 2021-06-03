package com.example.servicenovigradproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//q94M8HsayiRVequ1CsKRisZhkvR2 <- admins UID

public class AdminHome extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home2);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();
    }

    public void onClickToEditUsers(View view){startActivity(new Intent(AdminHome.this, EditUsers.class)); }
    public void onClickToCreateService(View view){startActivity(new Intent(AdminHome.this, CreateService.class)); }
    public void onClickToEditService(View view){startActivity(new Intent(AdminHome.this, EditService.class)); }
    public void onClickLogout(View view){
        mAuth.signOut();
        finish();
        startActivity(new Intent(AdminHome.this, MainActivity.class));
    }
}