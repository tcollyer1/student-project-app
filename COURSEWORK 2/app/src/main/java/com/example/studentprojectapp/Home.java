package com.example.studentprojectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
        Switch notifToggle = findViewById(R.id.swt_notificationtoggle);

        // Set switch to on by default
        notifToggle.setChecked(true);
        notifToggle.setText("Notifications ON");

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

        notifToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notifToggle.setText("Notifications ON");
                    Toast.makeText(Home.this, "Notifications turned on.", Toast.LENGTH_SHORT).show();
                }
                else {
                    notifToggle.setText("Notifications OFF");
                    Toast.makeText(Home.this, "Notifications turned off.", Toast.LENGTH_SHORT).show();
                }
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