package com.example.servicenovigradproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class CreateService extends AppCompatActivity {

    // Class Variables
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private DatabaseReference mServicesRef;
    private double ServiceRate;
    private String ServiceName;
    private String[] lstServiceNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);
        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mServicesRef = mRootRef.child("services");

        mServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstServiceNames = new String[(int) snapshot.getChildrenCount()];
                int i = 0;
                for (DataSnapshot ds : snapshot.getChildren()){
                    Service s = ds.getValue(Service.class);
                    lstServiceNames[i] = s.getName();
                    i++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//        System.out.println("@Reflectile 1 " + lstServiceNames.length);
    }

    public void onClickGoBack(View view){startActivity(new Intent(CreateService.this, AdminHome.class));}

    public boolean onClickCreateService(View view){
        // Obtain inputs
        EditText CreateServiceNameTB = findViewById(R.id.ApproveServiceNameTB);
        EditText CreateServiceRateTB = findViewById(R.id.ApproveServiceRateTB);
        CheckBox CreateServiceDriverLicense = findViewById(R.id.CreateServiceDriverLicense);
        CheckBox CreateServiceHealthCard = findViewById(R.id.CreateServiceHealthCard);
        CheckBox CreateServicePhotoID = findViewById(R.id.CreateServicePhotoID);

        ServiceName = CreateServiceNameTB.getText().toString();
        String ServiceRateStr = CreateServiceRateTB.getText().toString();
        boolean DriverLicense = CreateServiceDriverLicense.isChecked();
        boolean HealthCard = CreateServiceHealthCard.isChecked();
        boolean PhotoID = CreateServicePhotoID.isChecked();

        // Check for empty fields
        if (validFields(ServiceName, ServiceRateStr) && noSameName(ServiceName)){
            // Create service and add to firebase
            String id = mServicesRef.push().getKey();
            Service service = new Service(ServiceName, ServiceRate, DriverLicense, HealthCard, PhotoID);
            mServicesRef.child(id).setValue(service);
            Toast.makeText(getApplicationContext(), "Service created!", Toast.LENGTH_SHORT).show();
            // Clear Selection
            CreateServiceNameTB.setText("");
            CreateServiceRateTB.setText("");
            CreateServiceDriverLicense.setChecked(false);
            CreateServiceHealthCard.setChecked(false);
            CreateServicePhotoID.setChecked(false);
            return true;
        }
        return false;
    }

    private boolean noSameName(String serviceName) {
        if (Arrays.stream(lstServiceNames).anyMatch(serviceName::equals)){
            Toast.makeText(getApplicationContext(), "Name already exists", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validFields(String serviceName, String serviceRateStr) {
        if (serviceName.trim().equals("")){
            Toast.makeText(getApplicationContext(), "Name is blank", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            // Convert rate to double with two decimal places
            ServiceRate = Math.round(Double.parseDouble(serviceRateStr) * 100.0) / 100.0;
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Invalid rate", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}