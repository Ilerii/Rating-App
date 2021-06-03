package com.example.servicenovigradproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class EditUsers extends AppCompatActivity {
    final String TAG = "AdminHome";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootRef = mDatabase.getReference();
    private ArrayList<UserAndID> allUsers = new ArrayList<>();
    private ArrayList<UserAndID> selectedUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_users);
        this.getAllUsers();
        //Do other stuff
        //Or not
    }

    public void onClickBack(View view){ startActivity(new Intent(EditUsers.this, AdminHome.class)); }

    private void getAllUsers(){
        DatabaseReference userRef = mRootRef.child("users");
        userRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot child: snapshot.getChildren()){
                            addUser(child);
                        }
                        //logAllUsers();
                        updateTable();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, error.toString());
                    }
                }
        );
    }

    private void updateTable(){
        TableLayout userLay = (TableLayout)findViewById(R.id.EditUsersUserTable);
        userLay.bringToFront();
        TableRow tr = new TableRow(this);
        TextView[] tv = new TextView[6];
        for(int e=0;e<6;e++){
            tv[e] = new TextView(this);
            tv[e].setTextColor(Color.BLACK);
            tv[e].setPadding(5,5,5,5);
            tv[e].setBackground(ContextCompat.getDrawable(this, R.drawable.borderlightgray));
        }
        tv[0].setText("First Name");
        tv[1].setText("Last Name");
        tv[2].setText("Email");
        tv[3].setText("Password");
        tv[4].setText("User Type");
        tv[5].setText("UserID");
        tr.addView(new TextView(this));
        for(int e=0;e<6;e++){
            tr.addView(tv[e]);
        }
        userLay.addView(tr);

        for(int i=0;i<allUsers.size();i++){
            tr = new TableRow(this);
            tv = new TextView[6];
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
            for(int e=0;e<6;e++){
                tv[e] = new TextView(this);
                tv[e].setTextColor(Color.BLACK);
                tv[e].setPadding(5,5,5,5);
                if(i%2==0)tv[e].setBackground(ContextCompat.getDrawable(this, R.drawable.border));
                else tv[e].setBackground(ContextCompat.getDrawable(this, R.drawable.borderlightgray));
            }
            tv[0].setText(allUsers.get(i).getFirstName());
            tv[1].setText(allUsers.get(i).getLastName());
            tv[2].setText(allUsers.get(i).getEmail());
            tv[3].setText(allUsers.get(i).getPassword());
            tv[4].setText(allUsers.get(i).getUserType());
            tv[5].setText(allUsers.get(i).getUID());
            for(int e=0;e<6;e++){
                tr.addView(tv[e]);
            }
            userLay.addView(tr);
        }
    }

    private void addUser(DataSnapshot snapshot){
        User base = snapshot.getValue(User.class);
        if(!(base.getDisabledFlag())){
            UserAndID full = new UserAndID(snapshot.getKey(), base);
            allUsers.add(full);
        }
    }

    private void addToSelected(int index){
        selectedUsers.add(allUsers.get(index));
    }
    private void removeFromSelected(int index){
        selectedUsers.remove(allUsers.get(index));
    }

    private void logSelectedUsers(){
        Log.e(TAG, "Selected Users:");
        for(int i=0;i<selectedUsers.size();i++){
            Log.e(TAG, selectedUsers.get(i).toString());
        }
    }

    private void logAllUsers(){
        Log.e(TAG, ((Integer)allUsers.size()).toString());
        for(int i=0;i<allUsers.size();i++){
            Log.e(TAG, allUsers.get(i).toString());
        }
    }

    public void deleteRow(int index){
        Log.e(TAG, ((Integer)index).toString());
        removeFromSelected(index);
        View cb = findViewById(index);
        ((CheckBox)cb).setChecked(false);
        TableRow tb = (TableRow)cb.getParent();
        TableLayout table = (TableLayout)findViewById(R.id.EditUsersUserTable);
        table.removeView(tb);
    }

    public void onClickDeleteSelectedUsers(View view){
        //THIS IS WHERE THE FUN BEGINS
        deleteAllSelectedUsers(selectedUsers);
    }

    public void deleteAllSelectedUsers(ArrayList<UserAndID> usersToDelete){
        Log.e(TAG, "PREPARE TO DIE");
        logSelectedUsers();
        //ALL SINS BELOW THIS LINE--------

        DatabaseReference userRef = mRootRef.child("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userChild : snapshot.getChildren()){
                    if(usersToDelete.contains(new UserAndID(userChild.getKey(), userChild.getValue(User.class)))){
                        if(userChild.getValue(User.class).getUserType().equals("Employee")){
                            Log.e(TAG,"THOT DETECTED");
                            mRootRef.child("branch").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot branchChild : snapshot.getChildren()){
                                        if(branchChild.getKey().equals(userChild.getKey())){
                                            branchChild.getRef().setValue(null);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), "There was an issue with deleting the Users", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        int index = allUsers.indexOf(new UserAndID(userChild.getKey(), userChild.getValue(User.class)));
                        Log.e(TAG, "GOING TO DELETE "+(new UserAndID(userChild.getKey(), userChild.getValue(User.class))).toString());
                        userChild.getRef().setValue(new User((User)(userChild.getValue(User.class)), true)); //Flag account with disabled
                        deleteRow(index);

                    }
                    Toast.makeText(getApplicationContext(), "User(s) deleted!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), "There was an issue with deleting the Users", Toast.LENGTH_LONG).show();
                //ERROR HANDLING MAYBE???
            }
        });
    }
}