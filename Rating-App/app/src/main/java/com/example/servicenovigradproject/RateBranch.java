package com.example.servicenovigradproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class RateBranch extends AppCompatActivity {
    RatingBar ratingBar;
    Button submitRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_branch);

        ((Button)findViewById(R.id.btnRateBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(RateBranch.this, CustomerHome.class));
            }
        });

        ratingBar = findViewById(R.id.ratingBar);
        submitRating = findViewById(R.id.submitRating);

        ((Button)findViewById(R.id.submitRating)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String evaluation = String.valueOf(ratingBar.getRating());
                Toast.makeText(getApplicationContext(), evaluation + "Star", Toast.LENGTH_SHORT).show();
            }
        });
    }
}