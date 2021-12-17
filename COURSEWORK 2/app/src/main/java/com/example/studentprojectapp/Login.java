package com.example.studentprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView studentID = findViewById(R.id.txt_studentid);
        TextView password = findViewById(R.id.txt_password);
        Button btnLogin = findViewById(R.id.btn_logIn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strStudentId = studentID.getText().toString();
                String strPassword = password.getText().toString();

                if (validLogin(strStudentId, strPassword)) {
                    openHomeScreen(strStudentId);
                }
                else {
                    displayErrorSnackbar(btnLogin);
                }
            }
        });
    }

    // Gets if login details are correct
    private boolean validLogin(String id, String password) {
        if (!id.equals("")) {
            try {
                Integer.parseInt(id);
            }
            catch (Exception ex) {
                return false;
            }

            if (password.equals("comp2000")) {
                return true;
            }
        }
        return false;
    }

    private void openHomeScreen(String studentID) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Home.class);
        intent.putExtra("studentID", studentID);

        startActivity(intent);
    }

    private void displayErrorSnackbar(View v) {
        Snackbar.make(v, "Error: incorrect login details", Snackbar.LENGTH_SHORT).show();
    }
}