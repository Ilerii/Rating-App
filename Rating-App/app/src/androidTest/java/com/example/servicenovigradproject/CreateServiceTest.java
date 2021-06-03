package com.example.servicenovigradproject;

import android.widget.CheckBox;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreateServiceTest {

    public String randStr() {
        Random r = new Random();
        String toReturn = "";
        String abc = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 8; i++) {
            toReturn = toReturn + (abc.charAt((r.nextInt(abc.length()))));
        }
        return toReturn;
    }
    @Rule
    public ActivityScenarioRule<CreateService> registerRule = new ActivityScenarioRule(CreateService.class);

    @Test
    public void aNewServiceCannotHaveTheNameOfAnExistingService(){
        try (ActivityScenario<CreateService> scenario = ActivityScenario.launch(CreateService.class)) {
            scenario.onActivity(activity -> {
                ((EditText)activity.findViewById(R.id.ApproveServiceNameTB)).setText("Pizza Driver"); //This name already exists
                ((EditText)activity.findViewById(R.id.ApproveServiceRateTB)).setText("100");
                activity.findViewById(R.id.CreateServiceDriverLicense);
                activity.findViewById(R.id.CreateServiceHealthCard);
                activity.findViewById(R.id.CreateServicePhotoID);
                assertFalse("This should fail because we already have a service named Pizza Driver", activity.onClickCreateService(null));
            });
        }
        catch(Exception e){}
    }

    @Test
    public void addingAProperServiceSucceeds(){
        try (ActivityScenario<CreateService> scenario = ActivityScenario.launch(CreateService.class)) {
            scenario.onActivity(activity -> {
                ((EditText)activity.findViewById(R.id.ApproveServiceNameTB)).setText(randStr()); //This name already exists
                ((EditText)activity.findViewById(R.id.ApproveServiceRateTB)).setText("100");
                activity.findViewById(R.id.CreateServiceDriverLicense);
                activity.findViewById(R.id.CreateServiceHealthCard);
                activity.findViewById(R.id.CreateServicePhotoID);
                assertTrue("This should pass because the name is new", activity.onClickCreateService(null));
            });
        }
        catch(Exception e){}
    }

    @Test
    public void havingAnEmptyNameShouldCauseItToFail(){
        try (ActivityScenario<CreateService> scenario = ActivityScenario.launch(CreateService.class)) {
            scenario.onActivity(activity -> {
                ((EditText)activity.findViewById(R.id.ApproveServiceNameTB)).setText(""); //This name already exists
                ((EditText)activity.findViewById(R.id.ApproveServiceRateTB)).setText("100");
                activity.findViewById(R.id.CreateServiceDriverLicense);
                activity.findViewById(R.id.CreateServiceHealthCard);
                activity.findViewById(R.id.CreateServicePhotoID);
                assertFalse("This should fail because the name is blank", activity.onClickCreateService(null));
            });
        }
        catch(Exception e){}
    }

    @Test
    public void havingAnEmptyRateShouldCauseItToFail(){
        try (ActivityScenario<CreateService> scenario = ActivityScenario.launch(CreateService.class)) {
            scenario.onActivity(activity -> {
                ((EditText)activity.findViewById(R.id.ApproveServiceNameTB)).setText(randStr()); //This name already exists
                ((EditText)activity.findViewById(R.id.ApproveServiceRateTB)).setText("");
                activity.findViewById(R.id.CreateServiceDriverLicense);
                activity.findViewById(R.id.CreateServiceHealthCard);
                activity.findViewById(R.id.CreateServicePhotoID);
                assertFalse("This should fail because the name is blank", activity.onClickCreateService(null));
            });
        }
        catch(Exception e){}
    }

    @Test
    public void havingANonNumericRateIsNotAllowed(){
        try (ActivityScenario<CreateService> scenario = ActivityScenario.launch(CreateService.class)) {
            scenario.onActivity(activity -> {
                ((EditText)activity.findViewById(R.id.ApproveServiceNameTB)).setText(randStr()); //This name already exists
                ((EditText)activity.findViewById(R.id.ApproveServiceRateTB)).setText("IMNOTANUMBER");
                activity.findViewById(R.id.CreateServiceDriverLicense);
                activity.findViewById(R.id.CreateServiceHealthCard);
                activity.findViewById(R.id.CreateServicePhotoID);
                assertFalse("This should fail because the name is blank", activity.onClickCreateService(null));
            });
        }
        catch(Exception e){}
    }


 }
