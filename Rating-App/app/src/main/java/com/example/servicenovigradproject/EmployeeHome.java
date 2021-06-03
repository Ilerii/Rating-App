package com.example.servicenovigradproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeHome extends AppCompatActivity {
    final String TAG = "EmployeeHome";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRootRef;
    private User cUser;
    private String branchID;
    private Employee branchInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        // gets User data
        DatabaseReference userRef = mRootRef.child("users");
        DatabaseReference branchRef = mRootRef.child("branch");
        if(mUser == null){
            finish();
            startActivity(new Intent(EmployeeHome.this, MainActivity.class));
        }
        else{
            branchID = mAuth.getUid();
            Log.e(TAG, branchID);

            // gets Employee data
            branchRef.child(branchID).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot branchSnapshot) {
                        branchInfo = branchSnapshot.getValue(Employee.class);
                        System.out.println(branchInfo);
                        if (branchInfo == null) {
                            Log.e(TAG,"EMPLOYEE BE NULL :(");
                            branchRef.child(branchID).setValue(new Employee());
                            findViewById(R.id.btnSetupBranch).setVisibility(View.VISIBLE);
                            findViewById(R.id.layoutTop).setVisibility(View.GONE);
                            findViewById(R.id.layoutBottom).setVisibility(View.GONE);
                        }
                        // If branch has not been setup makes only available option to set up branch
                        else if (!branchInfo.isBranchSetUp()) {
                            findViewById(R.id.btnSetupBranch).setVisibility(View.VISIBLE);
                            findViewById(R.id.layoutTop).setVisibility(View.GONE);
                            findViewById(R.id.layoutBottom).setVisibility(View.GONE);
                        }
                        // If branch has been setup, setup branch button is killed and other buttons become visible
                        else {
                            View setupButton = findViewById(R.id.btnSetupBranch);
                            ((ViewGroup)setupButton.getParent()).removeView(setupButton);
                            findViewById(R.id.layoutTop).setVisibility(View.VISIBLE);
                            findViewById(R.id.layoutBottom).setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, error.toString());
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(EmployeeHome.this, MainActivity.class));
                    }
                });
            }


        // Logout button
        ((Button)findViewById(R.id.btnEmployeeLogout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(EmployeeHome.this, MainActivity.class));
            }
        });
        // Setup branch button
        ((Button)findViewById(R.id.btnSetupBranch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeHome.this, SetupBranch.class));
            }
        });
        // Edit branch button
        ((Button)findViewById(R.id.btnEditBranchInfo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeHome.this, EditBranchInfo.class));
            }
        });
        // Offered services button
        ((Button)findViewById(R.id.btnEditOfferedServices)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeHome.this, EditOfferedServices.class));
            }
        });
        // Service request button
        ((Button)findViewById(R.id.btnToServiceRequests)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeHome.this, PendingServiceRequests.class));
            }
        });




    }


}