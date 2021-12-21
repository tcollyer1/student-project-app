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
    private boolean notifs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get student's ID from login
        String studentID = getStudentID();
        String newWelcomeTxt = "Welcome, student " + studentID + "!";

        Button logOutBtn = findViewById(R.id.btn_logOut);
        Button viewProjectsBtn = findViewById(R.id.btn_viewProjects);
        Button addProjectBtn = findViewById(R.id.btn_addProject);

        TextView welcomeTxt = findViewById(R.id.lbl_welcome_home);
        Switch notifToggle = findViewById(R.id.swt_notificationtoggle);

        // Set switch to user's choice
        notifs = getNotifsPref();

        notifToggle.setChecked(notifs);
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
                openViewProjects(studentID);
            }
        });

        addProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddProject(studentID);
            }
        });

        notifToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notifToggle.setText("Notifications ON");
                    setNotifsPref(true);
                    Toast.makeText(Home.this, "Notifications turned on.", Toast.LENGTH_SHORT).show();
                }
                else {
                    notifToggle.setText("Notifications OFF");
                    setNotifsPref(false);
                    Toast.makeText(Home.this, "Notifications turned off.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getStudentID() {
        Intent intent = getIntent();
        String studentID = intent.getStringExtra("studentID");

        return studentID;
    }

    private void setNotifsPref(boolean pref) {
        notifs = pref;
    }

    private boolean getNotifsPref() {
        Intent intent = getIntent();
        String result = intent.getStringExtra("notifsPref");
        if (result != null) {
            return Boolean.parseBoolean(result);
        }

        return true; // on by default
    }

    private void logOut() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Login.class);
        startActivity(intent);
    }

    private void openViewProjects(String studentID) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, ViewProjects.class);
        intent.putExtra("studentID", studentID);
        intent.putExtra("notifsPref", Boolean.toString(notifs));
        startActivity(intent);
    }

    private void openAddProject(String studentID) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, AddProject.class);
        intent.putExtra("studentID", studentID);
        intent.putExtra("notifsPref", Boolean.toString(notifs));
        startActivity(intent);
    }
}