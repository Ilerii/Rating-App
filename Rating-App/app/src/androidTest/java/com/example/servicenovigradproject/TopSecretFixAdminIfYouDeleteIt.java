package com.example.servicenovigradproject;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TopSecretFixAdminIfYouDeleteIt {

    @Rule
    public ActivityScenarioRule<Register> registerRule = new ActivityScenarioRule(Register.class);
    /*
    @Test
    public void fixTheAdminBecauseYourCodeIsGarbage(){
        try (ActivityScenario<Register> scenario = ActivityScenario.launch(Register.class)) {
            scenario.onActivity(activity -> {
                    activity.restoreAdmin();
            });
        }
    }*/
}
