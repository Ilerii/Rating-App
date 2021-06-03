package com.example.servicenovigradproject;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class TopSecretAdd50NewAccountsNOTATEST {
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
    /*
    @Test
    public void accountCreationShouldRegisterForEmployees() {
        for(int i=0;i<25;i++) {
            try (ActivityScenario<Register> scenario = ActivityScenario.launch(Register.class)) {
                scenario.onActivity(activity -> {
                    assertTrue("There should be no problems adding this user as Customer", activity.createAccount(new User("Andrew", "Squires", randEmail(), "qwerty", "Employee")));
                });
            }
        }
    }

    @Test
    public void accountCreationShouldRegisterForCustomers() {
        for (int i = 0; i < 25; i++) {
            try (ActivityScenario<Register> scenario = ActivityScenario.launch(Register.class)) {
                scenario.onActivity(activity -> {
                    assertTrue("There should be no problems adding this user as Employee", activity.createAccount(new User("Sterling", "Kuepfer", randEmail(), "qwerty", "Customer")));
                });
            }
        }
    }*/
}
