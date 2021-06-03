package com.example.servicenovigradproject;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public String roleSelected; //stores the position for the currently selected item from the role spinner
    private final String TAG = "Register";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRootRef;
    private String errors = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRootRef = mDatabase.getReference();

        //Creates stuff needed for the spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinnerRoleSelect);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Select an Option");
        categories.add("Employee");
        categories.add("Customer");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void clickToLogin(View view) {
        startActivity(new Intent(Register.this, Login.class));
    }

    public void clickRegisterAccount(View view) {
        registerAccount();
    }

    public boolean registerAccount(){
        TextView TextError = findViewById(R.id.textError);
        errors ="";

        //creates all EditText variables for the text boxes, and the spinner object for the role drop down
        EditText TextBoxFirstName = (EditText) findViewById(R.id.textboxFirstName);
        EditText TextBoxLastName = (EditText) findViewById(R.id.textboxLastName);
        Spinner spinnerRole = (Spinner) findViewById(R.id.spinnerRoleSelect);
        EditText TextBoxPassword = (EditText) findViewById(R.id.textboxPassword);
        EditText TextBoxPasswordConfirm = (EditText) findViewById(R.id.textboxPasswordConfirm);
        EditText TextBoxEmail = (EditText) findViewById(R.id.textboxEmail);
        EditText TextBoxEmailConfirm = (EditText) findViewById(R.id.textboxEmailConfirm);

        //obtains all inputs
        boolean validAccount = true;
        String firstName = TextBoxFirstName.getText().toString();
        String lastName = TextBoxLastName.getText().toString();
        String password = TextBoxPassword.getText().toString();
        String passwordConfirm = TextBoxPasswordConfirm.getText().toString();
        String email = TextBoxEmail.getText().toString();
        String emailConfirm = TextBoxEmailConfirm.getText().toString();

        //checks to see if inputs are valid, if not displays appropriate error messages to user
            if(

        emptyField(firstName) ||!

        verifyName(firstName))

        {
            errors = addError(errors, "Please enter a valid first name");
            TextBoxFirstName.setError("Please enter a valid first name");
            validAccount = false;
        }
            else

        {
            TextBoxFirstName.setError(null);
        }
            if(

        emptyField(lastName) ||!

        verifyName(lastName))

        {
            errors = addError(errors, "Please enter a valid last name");
            TextBoxLastName.setError("Please enter a valid last name");
            validAccount = false;
        }
            else

        {
            TextBoxLastName.setError(null);
        }
            if(roleSelected.equals("Select an Option"))

        {
            errors = addError(errors, "Please select a role");
            ((TextView) spinnerRole.getSelectedView()).setError("Please select a role");
            validAccount = false;
        }
            else

        {
            ((TextView) spinnerRole.getSelectedView()).setError(null);
        }
        //Added specific invalid password cases (Nick)
            if(

        emptyField(password) ||

        badMatch(password, passwordConfirm) ||!

        validPassword(password))

        {
            if (emptyField(password)) {
                errors = addError(errors, "Password required");
                TextBoxPassword.setError("\"Password required");
            } else if (!validPassword(password)) {
                errors = addError(errors, "Passwords must be at least 6 characters long");
                TextBoxPassword.setError("\"Passwords must be at least 6 characters long");
            } else {
                errors = addError(errors, "Password confirmation must match password");
                TextBoxPassword.setError("\"Password confirmation must match password");
            }
            //TextBoxPassword.setText("");
            //TextBoxPasswordConfirm.setText("");
            validAccount = false;
        }
            else

        {
            TextBoxPassword.setError(null);
        }
        //Combined email verification cases (Nick)
            if(

        emptyField(email)||!

        verifyEmail(email)  ||

        badMatch(email, emailConfirm))

        {
            errors = addError(errors, "Emails must be valid");
            TextBoxEmail.setError("Emails must be valid");
            validAccount = false;
        }
            else

        {
            TextBoxEmail.setError(null);
        }


        //attempts to create account on firebase
        if(validAccount) {
            TextError.setError(null);
            TextError.setTextColor(Color.BLACK);
            TextError.setText("Registering account...");
            errors = "";

            // Converts First and Last Name toUpper, and email all to lower
            firstName = firstCharToUpper(firstName);
            lastName = firstCharToUpper(lastName);
            email = allToLower(email);

            if (createAccount(new User(firstName, lastName, email, password, roleSelected))) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Changes activity to WelcomePage
                                    finish();
                                    startActivity(new Intent(Register.this, WelcomePage.class));
                                } else {
                                    // Sign in fail
                                    TextError.setTextColor(Color.RED);
                                    TextError.setText("Registration Failed, Please try Again");
                                }
                            }
                        });
            }
        }
        else {
            //Displays errors to user
            TextError.setTextColor(Color.RED);
            TextError.setText(errors);
            TextError.setError(errors);
        }
        return validAccount;
    }

    boolean accountCreated = true;
    public boolean createAccount(User user){
        final User nUser = user;
        accountCreated = true;
        try {
            mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.e(TAG, "createUserWithEmailAndPassword:success");
                                mRootRef.child("users").child(FirebaseAuth.getInstance().getUid()).setValue(nUser);
                                accountCreated = true;

                            } else {
                                Log.e(TAG, "createUserWithEmailAndPassword:faliure", task.getException());
                                errors = ((FirebaseAuthException) task.getException()).getErrorCode();
                                accountCreated = false;
                            }
                        }
                    });
        }
        catch(Exception e){
            return false;
        }
        return accountCreated;
    }
    //checks to see if the elements match
    private boolean badMatch(String element1, String element2){
        return !element1.equals(element2);
    }

    //checks to see if a given field was empty
    private boolean emptyField(String element){
        return element.length() == 0;
    }

    //Names can only contain alpha chars or ' - or space
    private boolean verifyName(String name){
        char[] charArray = name.toCharArray();
        for(char c: charArray){
            if(!Character.isLetter(c)){
                if(c != '\'' && c != ' ' && c != '-'){
                    return false;
                }
            }
        }
        return true;
    }

    //Converts the first char to UpperCase
    private String firstCharToUpper(String toConvert){
        char[] charArray = toConvert.toCharArray();
        int asciiValue = (int)charArray[0];
        if(asciiValue >= 97 && asciiValue <= 122){//If the value is a lower case char
            asciiValue -= 32; //Converts to upper case
        }
        charArray[0] = (char)asciiValue;
        return String.valueOf(charArray);
    }

    private String allToLower(String toConvert){
        char[] charArray = toConvert.toCharArray();
        for(int i=0;i<charArray.length;i++){
            if((int)charArray[i] >= 65 && (int)charArray[i] <= 90){
                charArray[i] = (char)((int)charArray[i]+32);
            }
        }
        return String.valueOf(charArray);
    }

    private boolean verifyEmail(String email){
        return ((contains(email,'@'))&&contains(email, '.'));
    }

    private boolean contains(String searchIn, char toFind){
        char[] charArray = searchIn.toCharArray();
        for(char c: charArray){
            if(c == toFind) {
                return true;
            }
        }
        return false;
    }

    private boolean validPassword(String pass){
        if(pass.length()<6){return false;}
        return true;
    }

    //Adds a new error to the list of errors in the registration fields
    private String addError(String errorList, String newError){
        if(errorList.equals(""))
            return "-" + newError;
        else
            return errorList + "\n-" + newError;
    }

    //methods needed for spinner interface
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        roleSelected = parent.getItemAtPosition(pos).toString();
    }
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void restoreAdmin(){
        mAuth.signOut();
        mAuth.signInWithEmailAndPassword("admin@admin.ca", "qwerty").addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                mRootRef.child("users").child("q94M8HsayiRVequ1CsKRisZhkvR2").setValue(new User("admin", "admin", "admin@admin.ca", "qwerty", "admin", false));
            }
        });
    }
}