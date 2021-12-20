package com.example.studentprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class AddProject extends AppCompatActivity {
    private int studentID;
    private StudentProject newSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        studentID = getStudentID();

        Button submitBtn = findViewById(R.id.btn_submitnewproject);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postProject();
            }
        });
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

    private EditText[] getTextFieldData() {
        EditText idTxt = findViewById(R.id.txt_new_id);
        EditText titleTxt = findViewById(R.id.txt_new_title);
        EditText descriptionTxt = findViewById(R.id.txt_new_description);
        EditText yearTxt = findViewById(R.id.txt_new_year);
        EditText fnameTxt = findViewById(R.id.txt_new_fname);
        EditText lnameTxt = findViewById(R.id.txt_new_lname);

        EditText arr[] = {idTxt, titleTxt, descriptionTxt, yearTxt, fnameTxt, lnameTxt};

        return arr;
    }

    private void getValuesToPost() {
        EditText projectDetails[] = getTextFieldData().clone();

        try {
            newSP = new StudentProject(0, studentID, projectDetails[1].getText().toString(), projectDetails[2].getText().toString(), Integer.parseInt(projectDetails[3].getText().toString()), projectDetails[4].getText().toString(), projectDetails[5].getText().toString(), "null");
        } catch (Exception ex) {

        }
    }

    private void openHome(String studentID) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Home.class);
        intent.putExtra("studentID", studentID);
        startActivity(intent);
    }

    private void postProject() {
        RequestQueue queue = Volley.newRequestQueue(AddProject.this);

        String apiURL = "http://web.socem.plymouth.ac.uk/COMP2000/api/students/";

        getValuesToPost(); // sets updatedSP object to inputted values

        JSONObject postData = new JSONObject();

        try {
            postData.put("StudentID", newSP.getStudentID());
            postData.put("Title", newSP.getTitle());
            postData.put("Description", newSP.getDescription());
            postData.put("Year", newSP.getYear());
            postData.put("First_Name", newSP.getFirst_name());
            postData.put("Second_Name", newSP.getSecond_name());
        } catch (Exception ex) {
            Toast.makeText(AddProject.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiURL, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddProject.this, "Submitted!", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);

        openHome(Integer.toString(studentID));
    }
}