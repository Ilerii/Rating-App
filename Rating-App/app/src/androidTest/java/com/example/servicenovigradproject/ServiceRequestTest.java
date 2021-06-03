package com.example.servicenovigradproject;

import org.junit.Rule;
import org.junit.Test;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import java.io.File;
import java.util.ArrayList;
import static org.junit.Assert.assertTrue;

public class ServiceRequestTest {

    @Rule
    public ActivityScenarioRule<PendingServiceRequests> pendingServiceRequests = new ActivityScenarioRule(PendingServiceRequests.class);

    @Test
    public void aServiceRequestIsProperlyCreated(){
        ServiceRequest newServiceRequest = new ServiceRequest("MY NAME", 100.00, new ArrayList<File>());
        assertTrue(newServiceRequest.getName().equals("MY NAME"));
        assertTrue(newServiceRequest.getRate()==100.00);
    }

    @Test
    public void aServiceRequestIsProperlyStoredInTheDatabase(){
        try(ActivityScenario<PendingServiceRequests> scenario = ActivityScenario.launch(PendingServiceRequests.class)){
            scenario.onActivity(activity -> {
                ServiceRequest newServiceRequest2 = new ServiceRequest("MY NAME", 100.00, new ArrayList<File>());
                activity.createNewRequest(newServiceRequest2);
                try {
                    assertTrue("The Service Request we stored in the database is correct", activity.compareServiceRequest(newServiceRequest2));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        catch(Exception e){}
    }

}
