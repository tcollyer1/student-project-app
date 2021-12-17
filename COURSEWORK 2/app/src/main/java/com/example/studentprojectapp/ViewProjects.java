package com.example.studentprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONObject;

public class ViewProjects extends AppCompatActivity {
//    private int studentID;
//    private String title;
//    private String description;
//    private int year;
//    private String first_name;
//    private String second_name;

    private StudentProject sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_projects);

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();

        Button detailsBtn = findViewById(R.id.btn_details);

        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProjectDetails();
            }
        });
    }

    private void goToProjectDetails() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, ProjectDetails.class);

        intent.putExtra("studentID", Integer.toString(sp.getStudentID()));
        intent.putExtra("title", sp.getTitle());
        intent.putExtra("description", sp.getDescription());
        intent.putExtra("year", Integer.toString(sp.getYear()));
        intent.putExtra("first_name", sp.getFirst_name());
        intent.putExtra("second_name", sp.getSecond_name());

        startActivity(intent);
    }






    public class MyAsyncTasks extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewProjects.this);
            progressDialog.setMessage("Processing data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(String... params) {
            RequestQueue queue = Volley.newRequestQueue(ViewProjects.this);
            String apiURL = "http://web.socem.plymouth.ac.uk/COMP2000/api/students/2";
            TextView textView = findViewById(R.id.projectDisplay);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiURL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            try {
                                //JSONObject object = response.getJSONObject(0);

                                int studentID;
                                String title;
                                String description;
                                int year;
                                String first_name;
                                String second_name;

                                //int projID = object.getInt("projectID");
                                studentID = object.getInt("studentID");
                                title = object.getString("title");
                                description = object.getString("description");
                                year = object.getInt("year");
                                first_name = object.getString("first_Name");
                                second_name = object.getString("second_Name");

                                sp = new StudentProject(studentID, title, description, year, first_name, second_name);

                                //String textViewStr = "Student ID: " + studID + ", Title: " + title + ", Description: " + description + ", Year: " + year + ", Full name: " + fName + " " + lName;
                                String textViewStr = title + " (" + year + ")";

                                textView.setText(textViewStr);
                            }
                            catch (Exception ex) {
                                Toast.makeText(ViewProjects.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                                textView.setText(ex.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ViewProjects.this, "No projects yet...", Toast.LENGTH_SHORT).show();
                    textView.setText("No projects to show!");
                }
            });

            queue.add(jsonObjectRequest);

            return null;
        }

        protected void onPostExecute(String s) {
            super.onPreExecute();
            progressDialog.dismiss();
        }
    }
}


