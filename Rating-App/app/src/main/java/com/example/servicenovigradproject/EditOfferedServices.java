package com.example.servicenovigradproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditOfferedServices extends AppCompatActivity {
    final String TAG = "EditOfferedServices";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootRef = mDatabase.getReference();
    private DatabaseReference mServiceRef = mRootRef.child("services");
    private ArrayList<Service> allServices = new ArrayList<>();
    private ArrayList<Service> selectedServices = new ArrayList<>();
    private ArrayList<String> currentServices = new ArrayList<>();
    private ArrayList<String> serviceIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_offered_services);
        ((Button)findViewById(R.id.btnBackEditOfferedServices)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditOfferedServices.this, EmployeeHome.class));
            }
        });
        this.getCurrentServices();
    }

    private void getCurrentServices(){
        mRootRef.child("branch/"+mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentServices = snapshot.getValue(Employee.class).getOfferedServices(); //List of all services this branch offers
                logCurrentServices();
                getAllServices();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getAllServices(){
        mServiceRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot child: snapshot.getChildren()){
                            addService(child); //adds to list of all services that could be offered
                            addID(child.getKey()); //adds to list of all the services ids
                            //seviceIDs and allServices are in the same order, serviceIDs.get(i) is the ID of allServices.get(i)
                        }
                        //logAllServices();
                        updateTable();//Fills the table with stuff
                        readyTheButton();//Only when the table is ready can the button do stuff
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, error.toString());
                    }
                }
        );
    }

    private void addID(String id){
        serviceIDs.add(id);
    }

    private void readyTheButton(){
        ((Button)findViewById(R.id.btnEditServiceEditOfferedServices)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBranchOfferedServices(selectedServices);
            }
        });
    }

    private void updateBranchOfferedServices(ArrayList<Service> selectedServices){
        mRootRef.child("branch/"+mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Employee old = snapshot.getValue(Employee.class);
                old.setOfferedServices(servicesToIDs(selectedServices));
                mRootRef.child("branch/"+mAuth.getUid()).setValue(old);
                Toast.makeText(getApplicationContext(), "Offered services successfully updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "There was an issue with updating the services", Toast.LENGTH_LONG).show();

            }
        });
    }
    private ArrayList<String> servicesToIDs(ArrayList<Service> services){
        ArrayList<String> strings = new ArrayList<>();
        for(Service service :services){
            if(allServices.contains(service)){
                strings.add(serviceIDs.get(allServices.indexOf(service)));
            }
        }
        return strings;
    }
    private void updateTable(){
        TableLayout userLay = (TableLayout)findViewById(R.id.EditOfferedServicesServiceTable);
        userLay.bringToFront();
        TableRow tr = new TableRow(this);
        TextView[] tv = new TextView[6];
        for(int e=0;e<5;e++){
            tv[e] = new TextView(this);
            tv[e].setTextColor(Color.BLACK);
            tv[e].setPadding(5,5,5,5);
            tv[e].setBackground(ContextCompat.getDrawable(this, R.drawable.borderlightgray));
        }
        tv[0].setText("Service Name");
        tv[1].setText("Rate ($)");
        tv[2].setText("Driver License");
        tv[3].setText("Health Card");
        tv[4].setText("Photo ID");
        tr.addView(new TextView(this));
        for(int e=0;e<5;e++){
            tr.addView(tv[e]);
        }
        userLay.addView(tr);

        for(int i=0;i<allServices.size();i++){
            tr = new TableRow(this);
            tv = new TextView[5];
            CheckBox cb = new CheckBox(this);
            cb.setId(i);
            cb.setPadding(5,5,5,5);
            cb.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(((CheckBox)view).isChecked()){
                        addToSelected(view.getId());
                    }
                    else{
                        removeFromSelected(view.getId());
                    }
                }
            });
            tr.addView(cb);
            if(currentServices.contains(serviceIDs.get(i))){
                cb.setChecked(true);
                addToSelected(i);
            }
            for(int e=0;e<5;e++){
                tv[e] = new TextView(this);
                tv[e].setTextColor(Color.BLACK);
                tv[e].setPadding(5,5,5,5);
                if(i%2==0)tv[e].setBackground(ContextCompat.getDrawable(this, R.drawable.border));
                else tv[e].setBackground(ContextCompat.getDrawable(this, R.drawable.borderlightgray));
            }
            tv[0].setText(allServices.get(i).getName());
            tv[1].setText(Double.toString(allServices.get(i).getRate()));
            tv[2].setText(Boolean.toString(allServices.get(i).isDriverLicense()));
            tv[3].setText(Boolean.toString(allServices.get(i).isHealthCard()));
            tv[4].setText(Boolean.toString(allServices.get(i).isPhotoID()));
            for(int e=0;e<5;e++){
                tr.addView(tv[e]);
            }
            userLay.addView(tr);
        }
    }

    private void addService(DataSnapshot snapshot){
        Service newService = new Service(snapshot.getValue(Service.class));
        allServices.add(newService);
    }

    private void logSelectedServices(){
        Log.e(TAG, "Selected Services:");
        for(int i=0;i<selectedServices.size();i++){
            Log.e(TAG, selectedServices.get(i).toString());
        }
    }

    private void logAllServices(){
        Log.e(TAG, ((Integer)allServices.size()).toString());
        for(int i=0;i<allServices.size();i++){
            Log.e(TAG, allServices.get(i).toString());
        }
    }

    private void logAllServiceIDs(){
        Log.e(TAG, "ALL IDS L="+((Integer)serviceIDs.size()).toString());
        for(int i=0;i<serviceIDs.size();i++){
            Log.e(TAG, serviceIDs.get(i));
        }
    }

    private void logCurrentServices(){
        Log.e(TAG, "CURRENT SERVICES L+"+((Integer)currentServices.size()).toString());
        for(int i=0;i<currentServices.size();i++) {
            Log.e(TAG, currentServices.get(i));
        }
    }

    private void addToSelected(int index){
        selectedServices.add(allServices.get(index));
        logSelectedServices();
    }
    private void removeFromSelected(int index ){
        selectedServices.remove(allServices.get(index));
        logSelectedServices();
    }
}