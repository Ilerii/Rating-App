package com.example.servicenovigradproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditBranchInfo extends AppCompatActivity {

    final private String TAG = "EditBranch";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRootRef;
    private User cUser;
    private String branchID;
    private Employee branchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_branch); // reuses the setup branch xml as it'd be the exact same thing
        ((Button)findViewById(R.id.btnBackSetupBranch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditBranchInfo.this, EmployeeHome.class));
            }
        });
        TextView title = findViewById(R.id.textTitleSetupBranch);
        title.setText(R.string.Title_EditBranch); // changes title text to make more sense in this context

        Button update = findViewById(R.id.btnCreateBranch);
        update.setText(R.string.btn_Update); //Changes text of button to make more sense in this context
        update.setOnClickListener(this::updateBranch); // makes the Update branch button use this onClick as opposed to the one in SetupBranch

        //retrieves the info from firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mRootRef.child("users");
        if (mAuth == null) {
            finish();
            startActivity(new Intent(EditBranchInfo.this, MainActivity.class));
        } else if (mAuth.getUid() != null) {
            userRef.child(mAuth.getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            cUser = snapshot.getValue(User.class);
                            branchID = mUser.getUid();
                            System.out.println(cUser);
                            System.out.println(branchID);

                            // gets Employee data
                            mRootRef.child("branch").child(branchID).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot branchSnapshot) {
                                            branchInfo = branchSnapshot.getValue(Employee.class);
                                            System.out.println(branchInfo);
                                            //fills fields with data from firebase
                                            fillFields();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.e(TAG, error.toString());
                                            mAuth.signOut();
                                            finish();
                                            startActivity(new Intent(EditBranchInfo.this, MainActivity.class));
                                        }
                                    }
                            );
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, error.toString());
                            mAuth.signOut();
                            finish();
                            startActivity(new Intent(EditBranchInfo.this, MainActivity.class));
                        }


                    });
        }
    }


    public void updateBranch(View view){
        EditText boxAddress = (EditText) findViewById(R.id.textboxAddress);
        EditText boxPhoneNumber = (EditText) findViewById(R.id.textboxPhoneNumber);
        EditText boxSundayOpen = (EditText) findViewById(R.id.textboxSundayOpen);
        EditText boxSundayClose = (EditText) findViewById(R.id.textboxSundayClose);
        EditText boxMondayOpen = (EditText) findViewById(R.id.textboxMondayOpen);
        EditText boxMondayClose = (EditText) findViewById(R.id.textboxMondayClose);
        EditText boxTuesdayOpen = (EditText) findViewById(R.id.textboxTuesdayOpen);
        EditText boxTuesdayClose = (EditText) findViewById(R.id.textboxTuesdayClose);
        EditText boxWednesdayOpen = (EditText) findViewById(R.id.textboxWednesdayOpen);
        EditText boxWednesdayClose = (EditText) findViewById(R.id.textboxWednesdayClose);
        EditText boxThursdayOpen = (EditText) findViewById(R.id.textboxThursdayOpen);
        EditText boxThursdayClose = (EditText) findViewById(R.id.textboxThursdayClose);
        EditText boxFridayOpen = (EditText) findViewById(R.id.textboxFridayOpen);
        EditText boxFridayClose = (EditText) findViewById(R.id.textboxFridayClose);
        EditText boxSaturdayOpen = (EditText) findViewById(R.id.textboxSaturdayOpen);
        EditText boxSaturdayClose = (EditText) findViewById(R.id.textboxSaturdayClose);

        boxAddress.setError(null);
        boxPhoneNumber.setError(null);

        String address = boxAddress.getText().toString();
        String phoneNumber = boxPhoneNumber.getText().toString();

        String[][] inputedHours = new String[7][2];

        inputedHours[0][0] = boxSundayOpen.getText().toString();
        inputedHours[0][1] = boxSundayClose.getText().toString();
        inputedHours[1][0] = boxMondayOpen.getText().toString();
        inputedHours[1][1] = boxMondayClose.getText().toString();
        inputedHours[2][0] = boxTuesdayOpen.getText().toString();
        inputedHours[2][1] = boxTuesdayClose.getText().toString();
        inputedHours[3][0] = boxWednesdayOpen.getText().toString();
        inputedHours[3][1] = boxWednesdayClose.getText().toString();
        inputedHours[4][0] = boxThursdayOpen.getText().toString();
        inputedHours[4][1] = boxThursdayClose.getText().toString();
        inputedHours[5][0] = boxFridayOpen.getText().toString();
        inputedHours[5][1] = boxFridayClose.getText().toString();
        inputedHours[6][0] = boxSaturdayOpen.getText().toString();
        inputedHours[6][1] = boxSaturdayClose.getText().toString();


        ArrayList<DailyHours> workingHours = new ArrayList<>(7);

        boolean allFieldsValid = true;
        //TODO process times
        String[][] stringTime = new String[2][2];
        for(int x=0;x<7;x++){
            // checks if the time input fields are empty
            if(inputedHours[x][0].isEmpty()){
                addError("Please input a time",x,true);
                allFieldsValid = false;
                continue;
            }
            if(inputedHours[x][1].isEmpty()){
                addError("Please input a time",x,false);
                allFieldsValid = false;
                continue;
            }
            // splits the time into 2 strings representing hours and minutes and stores them in tempTime
            String[] tempTime = inputedHours[x][0].split(":");
            stringTime[0][0] = tempTime[0];
            if(tempTime.length != 2){ //ensure a nullPointerException does not occur if the time is inputted incorrectly or if CLOSED is inputted
                stringTime[0][1] = "-1";
            }
            else{
                stringTime[0][1] = tempTime[1];
            }
            // same as above but for the closing time
            tempTime = inputedHours[x][1].split(":");
            stringTime[1][0] = tempTime[0];
            if(tempTime.length==1){
                stringTime[1][1] = "-1";
            }
            else{
                stringTime[1][1] = tempTime[1];
            }

            int openHour;
            int openMinute;
            int closeHour;
            int closeMinute;
            // sets values for if CLOSED was entered
            if(stringTime[0][0].toLowerCase().equals("closed")){
                openHour = -1;
                openMinute = -1;
                if(stringTime[1][0].toLowerCase().equals("closed")){
                    closeHour = -1;
                    closeMinute = -1;
                }
                else{ //error
                    addError("Both Open and Close hours must have a time or be CLOSED",x,true);
                    addError("Both Open and Close hours must have a time or be CLOSED",x,false);
                    allFieldsValid = false;
                    continue;
                }
            }
            else if(stringTime[1][0].toLowerCase().equals("closed")){ // means open hour was not 'closed' but close hour was, thus an error
                addError("Both Open and Close hours must have a time or be CLOSED",x,true);
                addError("Both Open and Close hours must have a time or be CLOSED",x,false);
                allFieldsValid = false;
                continue;
            }
            else{ // sets values if CLOSED was not entered
                if(stringTime[0][1].equals("-1")){ // value was set to -1 above when only 1 number was made from the split. If this statement is called it means the input from the split is not CLOSED so it is an error
                    addError("Invalid input, time must be between 0:00 and 23:59",x,true);
                    allFieldsValid = false;
                    continue;
                }
                if(stringTime[1][1].equals("-1")){
                    addError("Invalid input, time must be between 0:00 and 23:59",x,true);
                    allFieldsValid = false;
                    continue;
                }
                // gets integer versions of time from the string variants
                System.out.println(stringTime[0][0]);
                openHour = Integer.parseInt(stringTime[0][0]);
                System.out.println(stringTime[0][1]);
                openMinute = Integer.parseInt(stringTime[0][1]);
                System.out.println(stringTime[1][0]);
                closeHour = Integer.parseInt(stringTime[1][0]);
                System.out.println(stringTime[1][1]);
                closeMinute = Integer.parseInt(stringTime[1][1]);
                // ensures all times are within a valid range
                if(openHour<0 || openHour>23 || openMinute<0 || openMinute>59){ //error
                    //add error for incorrect open hour input
                    addError("Invalid input, time must be between 0:00 and 23:59",x,true);
                    if(closeHour<0 || closeHour>23 || closeMinute<0 || closeMinute>59){
                        //add error for incorrect close hour input
                        addError("Invalid input, time must be between 0:00 and 23:59",x,false);
                    }
                    allFieldsValid = false;
                    continue;
                }
                else if(closeHour<0 || closeHour>23 || closeMinute<0 || closeMinute>59){
                    //add error for incorrect close hour input
                    addError("Invalid input, time must be between 0:00 and 23:59",x,false);
                    allFieldsValid = false;
                    continue;
                }
            }
            // there were no errors with the input so add the time to our list of working hours
            workingHours.add(new DailyHours(openHour,openMinute,closeHour,closeMinute));
        }

        //error handling for address
        if(address.equals("")){
            boxAddress.setError("Please enter an address");
            allFieldsValid = false;
        }
        //error handling for phonenumber
        if(phoneNumber.equals("")){
            boxPhoneNumber.setError("Please enter a phone number");
            allFieldsValid = false;
        }

        // every input was valid so update firebase
        if(allFieldsValid){
            Employee employee = new Employee(address,phoneNumber,true);
            employee.setWorkingHours(workingHours);
            mRootRef.child("branch").child(branchID).setValue(employee);
            Toast.makeText(this,"Data updated successfully",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "One or more fields are invalid, please check your inputs for highlighted boxes", Toast.LENGTH_SHORT).show();
        }
    }

    // Fills all the fields with the existing data from firebase
    private void fillFields(){
        EditText boxAddress = (EditText) findViewById(R.id.textboxAddress);
        EditText boxPhoneNumber = (EditText) findViewById(R.id.textboxPhoneNumber);

        ArrayList<DailyHours> workingHours = branchInfo.getWorkingHours(); // gets the working hours for the week
        // fills address and phone number boxes with existing info
        boxAddress.setText(branchInfo.getAddress());
        boxPhoneNumber.setText(branchInfo.getPhoneNumber());

        // fills all the textboxes for the times with the existing info
        for(int x=0;x<7;x++) {
            DailyHours dayWorkingHour = workingHours.get(x);
            //sets value to closed -1 is stored (-1 represents closed on firebase)
            if(dayWorkingHour.getStartHour()==-1 && dayWorkingHour.getEndHour()==-1){
                fillTime(x,"CLOSED",true);
                fillTime(x,"CLOSED",false);
            }
            else{
                String time = "";
                // the if statements make sure that the time is of the format hh:mm
                // and to make it so a time such as 4:04 isn't displayed as 4:4 which it would be otherwise
                if(dayWorkingHour.getStartHour()<10)
                    time += 0;
                time += dayWorkingHour.getStartHour() + ":";
                if(dayWorkingHour.getStartMinute()<10)
                    time += 0;
                time += dayWorkingHour.getStartMinute();
                fillTime(x,time,true); // fills info for opening time

                time = ""; // resets time for the closing time
                if(dayWorkingHour.getEndHour()<10)
                    time += 0;
                time += dayWorkingHour.getEndHour() + ":";
                if(dayWorkingHour.getEndMinute()<10)
                    time += 0;
                time += dayWorkingHour.getEndMinute();
                fillTime(x,time,false); // fills info for closing time
            }
        }
    }

    /**
     *
     * @param day int representing day of the week Sunday = 0, Saturday = 6
     * @param time string representing workingHour time
     * @param open boolean representing whether the inputted time is for the open slot or closed slot
     */
    private void fillTime(int day, String time, boolean open){
        EditText timeBox;
        switch(day){
            case 0:
                if(open)
                    timeBox = (EditText) findViewById(R.id.textboxSundayOpen);
                else
                    timeBox = (EditText) findViewById(R.id.textboxSundayClose);
                break;
            case 1:
                if(open)
                    timeBox = (EditText) findViewById(R.id.textboxMondayOpen);
                else
                    timeBox = (EditText) findViewById(R.id.textboxMondayClose);
                break;
            case 2:
                if(open)
                    timeBox = (EditText) findViewById(R.id.textboxTuesdayOpen);
                else
                    timeBox = (EditText) findViewById(R.id.textboxTuesdayClose);
                break;
            case 3:
                if(open)
                    timeBox = (EditText) findViewById(R.id.textboxWednesdayOpen);
                else
                    timeBox = (EditText) findViewById(R.id.textboxWednesdayClose);
                break;
            case 4:
                if(open)
                    timeBox = (EditText) findViewById(R.id.textboxThursdayOpen);
                else
                    timeBox = (EditText) findViewById(R.id.textboxThursdayClose);
                break;
            case 5:
                if(open)
                    timeBox = (EditText) findViewById(R.id.textboxFridayOpen);
                else
                    timeBox = (EditText) findViewById(R.id.textboxFridayClose);
                break;
            case 6:
                if(open)
                    timeBox = (EditText) findViewById(R.id.textboxSaturdayOpen);
                else
                    timeBox = (EditText) findViewById(R.id.textboxSaturdayClose);
                break;
            default:
                Log.e("SetupBranch", "Invalid dayIndex passed");
                return;
        }
        timeBox.setText(time);
    }

    /**
     * Sets the error of a specified time input box
     * @param error error message to be displayed
     * @param dayIndex int representing the day of the week (used to find the correct textbox) Sunday = 0, Saturday = 6
     * @param open if true put error on openHour box, if false put error on closeHour box
     */
    private void addError(String error, int dayIndex, boolean open){
        EditText errorBox;
        switch(dayIndex){
            case 0:
                if(open)
                    errorBox = (EditText) findViewById(R.id.textboxSundayOpen);
                else
                    errorBox = (EditText) findViewById(R.id.textboxSundayClose);
                break;
            case 1:
                if(open)
                    errorBox = (EditText) findViewById(R.id.textboxMondayOpen);
                else
                    errorBox = (EditText) findViewById(R.id.textboxMondayClose);
                break;
            case 2:
                if(open)
                    errorBox = (EditText) findViewById(R.id.textboxTuesdayOpen);
                else
                    errorBox = (EditText) findViewById(R.id.textboxTuesdayClose);
                break;
            case 3:
                if(open)
                    errorBox = (EditText) findViewById(R.id.textboxWednesdayOpen);
                else
                    errorBox = (EditText) findViewById(R.id.textboxWednesdayClose);
                break;
            case 4:
                if(open)
                    errorBox = (EditText) findViewById(R.id.textboxThursdayOpen);
                else
                    errorBox = (EditText) findViewById(R.id.textboxThursdayClose);
                break;
            case 5:
                if(open)
                    errorBox = (EditText) findViewById(R.id.textboxFridayOpen);
                else
                    errorBox = (EditText) findViewById(R.id.textboxFridayClose);
                break;
            case 6:
                if(open)
                    errorBox = (EditText) findViewById(R.id.textboxSaturdayOpen);
                else
                    errorBox = (EditText) findViewById(R.id.textboxSaturdayClose);
                break;
            default:
                Log.e("SetupBranch", "Invalid dayIndex passed");
                return;
        }
        errorBox.setError(error);
    }
}