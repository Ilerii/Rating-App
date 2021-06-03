package com.example.servicenovigradproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class FileRequest extends AppCompatActivity {

    final private String TAG = "FileRequest";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRootRef;
    private User cUser;
    private String branchID;
    private Employee branchInfo;
    private ArrayList<Employee> employees;
    private ArrayList<String> employeeIds;
    private Hashtable<String, Service> services;
    private  Hashtable<String, Service> servicesByName;
    private Hashtable<String, Employee> addressToServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_request);

        addressToServices = new Hashtable<>();
        services = new Hashtable<>();
        servicesByName = new Hashtable<>();
        employees = new ArrayList<>();
        employeeIds = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        ((Button)findViewById(R.id.btnFileBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(FileRequest.this, CustomerHome.class));
            }
        });

        TextView tv = (TextView)findViewById(R.id.fileRequestRate);
        tv.setText("");
        Button hc = (Button)findViewById(R.id.addHealthCard);
        Button dl = (Button)findViewById(R.id.addDrivers);
        Button pi = (Button)findViewById(R.id.addPhotoID);
        hc.setVisibility(View.GONE);
        pi.setVisibility(View.GONE);
        dl.setVisibility(View.GONE);

        getServices();
        getDatabaseValues();
    }

    public void getServices(){
        mRootRef.child("services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child:snapshot.getChildren()){
                    services.put(child.getKey(),child.getValue(Service.class));
                    servicesByName.put(child.getValue(Service.class).getName(),child.getValue(Service.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDatabaseValues(){
        mRootRef.child("branch").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child:snapshot.getChildren()) {
                    Log.e(TAG, child.getKey()+" "+child.getValue(Employee.class).toString());
                    try {
                        if (child.getValue(Employee.class).isBranchSetUp()){
                            if (!(child.getValue(Employee.class).getOfferedServices().isEmpty())) {
                                employees.add(child.getValue(Employee.class));
                                employeeIds.add(child.getKey());
                                addressToServices.put(child.getValue(Employee.class).getAddress(), child.getValue(Employee.class));
                            }
                        }
                    }
                    catch(Exception e){}
                }
                populateSpinner(employees);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    List<String> adresess = new ArrayList<>();
    public void populateSpinner(ArrayList<Employee> branches){
        adresess.add("Select a Branch");
        for(Employee e:employees){
            adresess.add(e.getAddress());
        }
        Spinner spinner = (Spinner) findViewById(R.id.branchesSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, adresess);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                dataAdapter.notifyDataSetChanged();
                if(pos>0) {
                    updateSecondSpinner(adresess.get(pos));
                }
                else clearSecondSpinner();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(0);
    }

    public void clearSecondSpinner(){
        offeredServices.clear();
        Spinner spinner = (Spinner) findViewById(R.id.offeredServices);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, offeredServices);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setSelection(0);
    }

    List<String> offeredServices = new ArrayList<>();
    public void updateSecondSpinner(String address){
        offeredServices.clear();
        offeredServices.add("Select a service");
        for(String s:addressToServices.get(address).getOfferedServices()){
            offeredServices.add(services.get(s).getName());
        }

        Spinner spinner = (Spinner) findViewById(R.id.offeredServices);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, offeredServices);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                dataAdapter.notifyDataSetChanged();
                if(pos>0) {
                    updateFields();
                }
                else clearFields();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setSelection(0);
    }
    public void clearFields(){
        TextView tv = (TextView)findViewById(R.id.fileRequestRate);
        tv.setText("");
        Button hc = (Button)findViewById(R.id.addHealthCard);
        Button dl = (Button)findViewById(R.id.addDrivers);
        Button pi = (Button)findViewById(R.id.addPhotoID);
        hc.setVisibility(View.GONE);
        pi.setVisibility(View.GONE);
        dl.setVisibility(View.GONE);
    }
    public void updateFields(){
        Spinner secondSpinner = (Spinner)findViewById(R.id.offeredServices);
        TextView tv = (TextView)findViewById(R.id.fileRequestRate);
        Button hc = (Button)findViewById(R.id.addHealthCard);
        Button dl = (Button)findViewById(R.id.addDrivers);
        Button pi = (Button)findViewById(R.id.addPhotoID);
        Service selected = servicesByName.get(secondSpinner.getSelectedItem().toString());
        double currentRate = selected.getRate();
        Log.e(TAG,((Double)currentRate).toString());
        tv.setText("Rate: $"+((Double)currentRate).toString());
        if(selected.isDriverLicense()){
            dl.setVisibility(View.VISIBLE);
        }
        if(selected.isHealthCard()){
            hc.setVisibility(View.VISIBLE);
        }
        if(selected.isPhotoID()){
            pi.setVisibility(View.VISIBLE);
        }
    }

}
