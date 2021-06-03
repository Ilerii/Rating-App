package com.example.servicenovigradproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    EditText TextBoxUsername, TextBoxPassword;
    TextView LoginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("LOGIN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

//        if(mUser != null){
//            finish();
//            startActivity(new Intent(Login.this, WelcomePage.class));
//        }

        // Get addresses of username and password textboxes, and status text label
        TextBoxUsername = findViewById(R.id.textboxUsername);
        TextBoxPassword = findViewById(R.id.textboxPassword);
        LoginStatus = findViewById(R.id.textLoginStatus);
    }

    // Action listener for sign in button
    public void clickSignIn(View view){

        // Save textbox info as string
        String username = TextBoxUsername.getText().toString();
        String password = TextBoxPassword.getText().toString();

        // Clear textboxes
        TextBoxUsername.setText("");
        TextBoxPassword.setText("");

        // Authenticate valid user credentials
        if (username.length() != 0 && password.length() != 0) {
            // Update status message
            LoginStatus.setText("Authenticating...");
            // Authenticate user with Firebase
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success
                                TextView status = findViewById(R.id.textLoginStatus);
                                status.setText("");
                                // Changes activity to WelcomePage
                                finish();
                                startActivity(new Intent(Login.this, WelcomePage.class));
                            } else {
                                // Sign in fail
                                TextView status = findViewById(R.id.textLoginStatus);
                                status.setText("Invalid Credentials");
                            }
                        }
                    });
        }

    }

    public void clickToRegister(View view){
        startActivity(new Intent(Login.this, Register.class));
    }
}