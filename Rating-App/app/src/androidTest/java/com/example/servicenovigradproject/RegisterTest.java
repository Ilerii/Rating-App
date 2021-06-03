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

public class RegisterTest {
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


    UserAndID fullUser;

    @Test
    public void registerUser() {
        User newUser = new User("Testname", "Todelete", randEmail(), "qwerty", "Customer");
        try (ActivityScenario<Register> scenario = ActivityScenario.launch(Register.class)) {
            scenario.onActivity(activity -> {
                activity.createAccount(newUser);

                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if ((newUser.getEmail()).equals(((User) child.getValue(User.class)).getEmail())) {
                                fullUser = new UserAndID(child.getKey(), child.getValue(User.class));
                                assertTrue("The new user we just made should match the one stored in the database", newUser.equals(fullUser));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            });
        }
    }
}