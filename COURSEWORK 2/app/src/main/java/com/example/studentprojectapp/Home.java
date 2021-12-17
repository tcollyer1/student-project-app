package com.example.studentprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get student's ID from login
        String studentID = getStudentID();
        String newWelcomeTxt = "Welcome, student " + studentID + "!";

        Button logOutBtn = findViewById(R.id.btn_logOut);
        Button viewProjectsBtn = findViewById(R.id.btn_viewProjects);
        TextView welcomeTxt = findViewById(R.id.lbl_welcome_home);

        // Set welcome text
        welcomeTxt.setText(newWelcomeTxt);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        viewProjectsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewProjects();
            }
        });

    }

    private String getStudentID() {
        Intent intent = getIntent();
        String studentID = intent.getStringExtra("StudentID");

        return studentID;
    }

    private void logOut() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Login.class);
        startActivity(intent);
    }

    private void openViewProjects() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, ViewProjects.class);
        startActivity(intent);
    }
}