package com.example.servicenovigradproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingServiceRequests extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootRef = mDatabase.getReference();
    private ArrayList<ServiceRequest> allRequests = new ArrayList<ServiceRequest>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_service_requests);
        ((Button)findViewById(R.id.btnBackServiceRequests)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PendingServiceRequests.this, EmployeeHome.class));
            }
        });
        this.getAllRequests(); // Obtain list of all pending requests; display them to screen
    }

    private void getAllRequests() {
        DatabaseReference requestRef = mRootRef.child("requests");
        requestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()){
                    addRequest(child);  // Add request to ArrayList allRequests for later reference
                }
                updateTable();  // Make table of clickable buttons representing pending requests
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addRequest(DataSnapshot child) {
        ServiceRequest request = child.getValue(ServiceRequest.class);
        request.setId(child.getKey());
        allRequests.add(request);
    }

    private void updateTable() {
        TableLayout tableLayout = (TableLayout)findViewById(R.id.tablePendingRequests);
        tableLayout.bringToFront();
        for (int i = 0; i < allRequests.size(); i++){
            TableRow tr = new TableRow(this);
            tr.setTag(i);
            Button btnServiceRequest = new Button(this);
            btnServiceRequest.setText(allRequests.get(i).getName());
            btnServiceRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PendingServiceRequests.this, ServiceRequestApproval.class);
                    intent.putExtra("UID", allRequests.get((Integer) tr.getTag()).getId());
                    startActivity(intent);
                }
            });
            tr.addView(btnServiceRequest);
        }
    }

    public void createNewRequest(ServiceRequest request){
        mRootRef.child("requests").child("test").setValue(request);
    }

    ServiceRequest requestO;
    boolean canProceed = false;
    public boolean compareServiceRequest(ServiceRequest request) throws InterruptedException {
        mRootRef.child("requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestO = snapshot.getValue(ServiceRequest.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        while(!canProceed){
            wait(100);
        }
        return request.equals(requestO);
    }

}