package com.example.studentprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.security.keystore.StrongBoxUnavailableException;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class UpdateProject extends AppCompatActivity {
    private StudentProject sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_project);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getProjectInfo();

        setTextFields();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent resultIntent = new Intent();
                resultIntent.putExtra("projectID", Integer.toString(sp.getProjectID()));
                resultIntent.putExtra("studentID", Integer.toString(sp.getStudentID()));
                resultIntent.putExtra("title", sp.getTitle());
                resultIntent.putExtra("description", sp.getDescription());
                resultIntent.putExtra("year", Integer.toString(sp.getYear()));
                resultIntent.putExtra("first_name", sp.getFirst_name());
                resultIntent.putExtra("second_name", sp.getSecond_name());
                setResult(Activity.RESULT_OK, resultIntent);
                this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private StudentProject getProjectInfo() {
        Intent intent = getIntent();

        int projectID = Integer.parseInt(intent.getStringExtra("projectID"));
        int studentID = Integer.parseInt(intent.getStringExtra("studentID")); // this is iffy
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        int year = Integer.parseInt(intent.getStringExtra("year")); //  this is iffy
        String first_name = intent.getStringExtra("first_name");
        String second_name = intent.getStringExtra("second_name");

        return new StudentProject(projectID, studentID, title, description, year, first_name, second_name);
    }

    private void setTextFields() {
        EditText idTxt = findViewById(R.id.txt_id);
        EditText titleTxt = findViewById(R.id.txt_title);
        EditText descriptionTxt = findViewById(R.id.txt_description);
        EditText yearTxt = findViewById(R.id.txt_year);
        EditText fnameTxt = findViewById(R.id.txt_fname);
        EditText lnameTxt = findViewById(R.id.txt_lname);

        String strYear = Integer.toString(sp.getYear());
        String strID = Integer.toString(sp.getStudentID());


        idTxt.setText(strID);
        titleTxt.setText(sp.getTitle());
        descriptionTxt.setText(sp.getDescription());
        yearTxt.setText(strYear);
        fnameTxt.setText(sp.getFirst_name());
        lnameTxt.setText(sp.getSecond_name());
    }
}