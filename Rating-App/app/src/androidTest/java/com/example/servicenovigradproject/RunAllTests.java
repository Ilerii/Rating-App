package com.example.servicenovigradproject;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AccountDeletionTest.class, RegisterTest.class, ServiceRequestTest.class, UserAccountsTest.class, CreateServiceTest.class})
public class RunAllTests {

}
