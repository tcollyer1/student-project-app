package com.example.studentprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class AddProject extends AppCompatActivity {
    private int studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        studentID = getStudentID();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent resultIntent = new Intent();
                resultIntent.putExtra("studentID", Integer.toString(studentID));
                setResult(Activity.RESULT_OK, resultIntent);
                this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getStudentID() {
        Intent intent = getIntent();
        int studentID = Integer.parseInt(intent.getStringExtra("studentID"));

        return studentID;
    }
}