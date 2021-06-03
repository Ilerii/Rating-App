package com.example.servicenovigradproject;

import org.junit.Rule;
import org.junit.Test;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class AccountDeletionTest {
    public String randEmail() {
        Random r = new Random();
        String toReturn = "";
        String abc = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 8; i++) {
            toReturn = toReturn + (abc.charAt((r.nextInt(abc.length()))));
        }
        toReturn = toReturn + "@gmail.ca";
        return toReturn;
    }

    @Rule
    public ActivityScenarioRule<Register> registerRule = new ActivityScenarioRule(Register.class);
    @Rule
    public ActivityScenarioRule<EditUsers> deleteRule = new ActivityScenarioRule(EditUsers.class);


    @Test
    public void userAndIDShouldHaveAllFieldsStoredCorrectly(){
        String fName = "Testfname";
        String lName = "Testlname";
        String email = "fakemail@gmail.com";
        String password = "password123";
        String uType = "Employee";
        String userID = "H8f9g2fh980FnOI0fn728fQ";
        UserAndID Testaccount = new UserAndID(userID, fName, lName, email, password, uType);
        assertEquals(fName, Testaccount.getFirstName());
        assertEquals(lName, Testaccount.getLastName());
        assertEquals(password, Testaccount.getPassword());
        assertEquals(uType, Testaccount.getUserType());
        assertEquals(email, Testaccount.getEmail());
        assertEquals(userID, Testaccount.getUID());
    }

    UserAndID fullUser;
    @Test
    public void deleteOneUserFromTheDatabase(){
        User newUser = new User("Testname", "Todelete", randEmail(), "qwerty", "Customer");
        try (ActivityScenario<Register> scenario = ActivityScenario.launch(Register.class)) {
            scenario.onActivity(activity -> {
                activity.createAccount(newUser);

                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot child: snapshot.getChildren()){
                            if((newUser.getEmail()).equals((child.getValue(User.class)).getEmail())){
                                fullUser = new UserAndID(child.getKey(), child.getValue(User.class));
                                assertTrue("The new user we just made should match the one stored in the database", newUser.equals(fullUser));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            });}

        try (ActivityScenario<EditUsers> delete = ActivityScenario.launch(EditUsers.class)){
            delete.onActivity(activity -> {
                ArrayList<UserAndID> toDelete = new ArrayList<>();
                toDelete.add(fullUser);
                activity.deleteAllSelectedUsers(toDelete);
                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAndID userToCompare = null;
                        for(DataSnapshot child: snapshot.getChildren()){
                            if((newUser.getEmail()).equals((child.getValue(User.class)).getEmail())){
                                userToCompare = new UserAndID(child.getKey(), child.getValue(User.class));
                            }
                        }
                        assertNotEquals("There was no field in the database with the correct value", userToCompare, newUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        });}
    }
}
