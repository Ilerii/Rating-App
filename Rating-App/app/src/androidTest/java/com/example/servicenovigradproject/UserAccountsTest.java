package com.example.servicenovigradproject;

import android.widget.EditText;
import android.widget.Spinner;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;


import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserAccountsTest {

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

    @Test
    public void UserShouldHaveAllFieldsStoredCorrectly() {
        String fName = "Andrew";
        String lName = "Squires";
        String email = "fakemail@gmail.com";
        String password = "password123";
        String uType = "Employee";
        User Andrew = new User(fName, lName, email, password, uType);
        assertEquals(fName, Andrew.getFirstName());
        assertEquals(lName, Andrew.getLastName());
        assertEquals(password, Andrew.getPassword());
        assertEquals(uType, Andrew.getUserType());
        assertEquals(email, Andrew.getEmail());
    }

    @Test
    public void accountCreationShouldRegisterForEmployees() {
        try (ActivityScenario<Register> scenario = ActivityScenario.launch(Register.class)) {
            scenario.onActivity(activity -> {
                assertTrue("There should be no problems adding this user as Customer", activity.createAccount(new User("Andrew", "Squires", randEmail(), "qwerty", "Employee")));
            });
        }
    }

    @Test
    public void accountCreationShouldRegisterForCustomers(){
        try (ActivityScenario<Register> scenario = ActivityScenario.launch(Register.class)) {
            scenario.onActivity(activity -> {
                assertTrue("There should be no problems adding this user as Employee", activity.createAccount(new User("Sterling", "Kuepfer", randEmail(), "qwerty", "Customer")));
            });
        }
    }

    @Test
    public void accountCreationShouldFailBadPasswords() {
        try (ActivityScenario<Register> scenario = ActivityScenario.launch(Register.class)) {
            scenario.onActivity(activity -> {
                ((EditText) activity.findViewById(R.id.textboxFirstName)).setText("Andrew");
                ((EditText) activity.findViewById(R.id.textboxLastName)).setText("Squires");
                ((EditText) activity.findViewById(R.id.textboxPassword)).setText("smol"); //this password is too short
                ((EditText) activity.findViewById(R.id.textboxPasswordConfirm)).setText("smol");
                ((EditText) activity.findViewById(R.id.textboxEmail)).setText("myEmail@gmail.ca");
                ((EditText) activity.findViewById(R.id.textboxEmailConfirm)).setText("myEmail@gmail.ca");
                activity.roleSelected = "Employee";
                assertFalse("Password is less than 6 chars", activity.registerAccount());
                ((EditText) activity.findViewById(R.id.textboxPassword)).setText(""); //this password is too short
                ((EditText) activity.findViewById(R.id.textboxPasswordConfirm)).setText("");
                assertFalse("There is no password", activity.registerAccount());
            });
        }
    }

    @Test
    public void accountCreationShouldFailBadEmails() {
        try (ActivityScenario<Register> scenario = ActivityScenario.launch(Register.class)) {
            scenario.onActivity(activity -> {
                ((EditText) activity.findViewById(R.id.textboxFirstName)).setText("Andrew");
                ((EditText) activity.findViewById(R.id.textboxLastName)).setText("Squires");
                ((EditText) activity.findViewById(R.id.textboxPassword)).setText("qwerty"); //this password is too short
                ((EditText) activity.findViewById(R.id.textboxPasswordConfirm)).setText("qwerty");
                ((EditText) activity.findViewById(R.id.textboxEmail)).setText("oopsnoat");
                ((EditText) activity.findViewById(R.id.textboxEmailConfirm)).setText("oopsnoat");
                assertFalse("Emails must include an @ sign", activity.registerAccount());

                ((EditText) activity.findViewById(R.id.textboxEmail)).setText("properemail@gmail.com");
                ((EditText) activity.findViewById(R.id.textboxEmailConfirm)).setText("properemailthatisntthesame@gmail.com");
                assertFalse("Emails must be the same", activity.registerAccount());
            });
        }
    }
}


