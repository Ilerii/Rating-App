package com.example.servicenovigradproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditService extends AppCompatActivity{
    final String TAG = "EditService";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRootRef;
    private List<String> categories; // list storing the options for the spinner
    private int position; // int storing the selected position of the spinner
    private ArrayList<Service> allServices;
    private ArrayList<String> allServiceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRootRef = mDatabase.getReference();

        allServices = new ArrayList<>();
        allServiceID = new ArrayList<>();
        categories = new ArrayList<>();
        getAllServices();
    }

    private void populateSpinner(){
        categories.clear();
        categories.add("Select an Option");
        for (Service service : allServices) {
            categories.add(service.getName());
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinnerSelectService);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                dataAdapter.notifyDataSetChanged();
                Log.e(TAG, ((Integer)pos).toString());
                if(pos>0) {
                    updateFields(pos - 1);
                    position = pos-1;
                }
                else position=-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(0);
    }

    private void getAllServices(){
        DatabaseReference serviceRef = mRootRef.child("services");
        serviceRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot child: snapshot.getChildren()){
                            Service service = child.getValue(Service.class);
                            allServices.add(service);
                            allServiceID.add(child.getKey());
                            Log.e(TAG, child.getKey());
                        }
                        populateSpinner();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, error.toString());
                    }
                }
        );
    }

    private void updateFields(int index){
        EditText name = (EditText) findViewById(R.id.textboxName);
        EditText rate = (EditText) findViewById(R.id.textboxRate);
        CheckBox driver = (CheckBox) findViewById(R.id.checkBoxDriversLicense);
        CheckBox health = (CheckBox) findViewById(R.id.checkBoxHealthCard);
        CheckBox photo = (CheckBox) findViewById(R.id.checkBoxPhotoID);
        Service selectedService = allServices.get(index);

        name.setText(selectedService.getName());
        rate.setText(Double.toString(selectedService.getRate()));
        driver.setChecked(selectedService.isDriverLicense());
        health.setChecked(selectedService.isHealthCard());
        photo.setChecked(selectedService.isPhotoID());
    }

    public void onClickConfirm(View view){
        Log.e(TAG, ((Integer)position).toString());

        if(position>=0){ //makes it so a service must be selected
            DatabaseReference serviceReference = mRootRef.child("services").child(allServiceID.get(position));
            EditText name = (EditText) findViewById(R.id.textboxName);
            EditText rate = (EditText) findViewById(R.id.textboxRate);
            CheckBox driver = (CheckBox) findViewById(R.id.checkBoxDriversLicense);
            CheckBox health = (CheckBox) findViewById(R.id.checkBoxHealthCard);
            CheckBox photo = (CheckBox) findViewById(R.id.checkBoxPhotoID);

            //updates values in firebase
            //TODO error handling if stuff is blank
            Service updatedService = new Service(name.getText().toString(), Double.parseDouble(rate.getText().toString()), driver.isChecked(), health.isChecked(), photo.isChecked());
            serviceReference.setValue(updatedService);

            allServices.set(position, updatedService);
            clearAllFields();
            populateSpinner();

            Toast.makeText(getApplicationContext(), "Service changed!", Toast.LENGTH_SHORT).show();
            //resets spinner selection
        }
    }
    private String toKill;
    public void onClickDelete(View view){
        toKill = allServiceID.get(position);
        Log.e(TAG, ((Integer)position).toString());
        if(position >= 0) {
            DatabaseReference toDelete = mRootRef.child("services").child(toKill);
            Log.e(TAG,"REMOVING "+allServiceID.get(position));
            mRootRef.child("branch").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child: snapshot.getChildren()){
                        Employee check = child.getValue(Employee.class);
                        Log.e(TAG,check.toString());
                        ArrayList<String> offered = check.getOfferedServices();
                        try {
                            if (!(offered == null)){
                                Log.e(TAG,"OFFERED ISNT NULL FOR EMPLOYEE "+check.toString());
                                if(offered.remove(toKill)){
                                    Log.e(TAG, "REMOVING " + toKill + " FROM " + check.toString());
                                    Employee replace = new Employee(check.getAddress(), check.getPhoneNumber(), true, check.getWorkingHours(), offered);
                                    child.getRef().setValue(replace);
                                }
                            }
                        }
                        catch(Exception e){}
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Log.e(TAG, toKill);
            Log.e(TAG, toDelete.getKey());
            toDelete.removeValue();
            allServices.remove(position);
            allServiceID.remove(position);
            position=0;
            populateSpinner();
            clearAllFields();

            Toast.makeText(getApplicationContext(), "Service deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearAllFields(){
        EditText name = (EditText) findViewById(R.id.textboxName);
        EditText rate = (EditText) findViewById(R.id.textboxRate);
        CheckBox driver = (CheckBox) findViewById(R.id.checkBoxDriversLicense);
        CheckBox health = (CheckBox) findViewById(R.id.checkBoxHealthCard);
        CheckBox photo = (CheckBox) findViewById(R.id.checkBoxPhotoID);

        name.setText("");
        rate.setText("");
        driver.setChecked(false);
        health.setChecked(false);
        photo.setChecked(false);
    }

    public void onClickReturn(View view){finish();startActivity(new Intent(EditService.this, AdminHome.class));}
}