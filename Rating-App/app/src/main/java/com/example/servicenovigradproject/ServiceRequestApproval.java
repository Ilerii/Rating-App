package com.example.servicenovigradproject;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServiceRequestApproval extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootRef = mDatabase.getReference();
    private ArrayList<String> lst_documents;
    private ArrayList<Bitmap> lst_document_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request_approval);
        ((Button)findViewById(R.id.btnBackApproveRequests)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServiceRequestApproval.this, PendingServiceRequests.class));
            }
        });
        setupData();  // Obtain Data from Firebase database and fill in fields
        decodeImages();  // Convert Base64 strings back into images
        formatImages();
    }

    private void setupData() {

        EditText serviceName = (EditText)findViewById(R.id.ApproveServiceNameTB);
        EditText serviceRate = (EditText)findViewById(R.id.ApproveServiceRateTB);

        String UID = getIntent().getStringExtra("UID");
        DatabaseReference allRequestRef = mRootRef.child("requests");
        DatabaseReference userRequestRef = allRequestRef.child(UID);
        userRequestRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ServiceRequest serviceRequest = snapshot.getValue(ServiceRequest.class);
                        serviceName.setText(serviceRequest.getName());
                        serviceRate.setText(Double.toString(serviceRequest.getRate()));
                        lst_documents = serviceRequest.getDocuments();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private void decodeImages() {
        for (String base64Image: lst_documents){
            byte[] bytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            lst_document_images.add(bitmap);
        }
    }

    private void formatImages() {
        TableLayout tableLayout = (TableLayout)findViewById(R.id.tableApproveDocuments);
        tableLayout.bringToFront();
        for (int i = 0; i < lst_document_images.size(); i++){
            TableRow tr = new TableRow(this);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(lst_document_images.get(i));
            tr.addView(imageView);
        }
    }





}
