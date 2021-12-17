package com.example.studentprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewProjects extends AppCompatActivity {
//    private int studentID;
//    private String title;
//    private String description;
//    private int year;
//    private String first_name;
//    private String second_name;
    private ArrayList<String> projectHeaders = new ArrayList<>();
    private ArrayList<StudentProject> studentProjects = new ArrayList<>();

    private StudentProject sp;
    private int currentStudentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_projects);

        currentStudentID = getStudentID();

        try {
            MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
            myAsyncTasks.execute();
        }
        catch (Exception ex) {
            Toast.makeText(ViewProjects.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }


        //Button detailsBtn = findViewById(R.id.btn_details);



//        detailsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToProjectDetails();
//            }
//        });
    }

    private void goToProjectDetails(int index) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, ProjectDetails.class);

        StudentProject current = studentProjects.get(index);

        intent.putExtra("projectID", Integer.toString(current.getProjectID()));
        intent.putExtra("studentID", Integer.toString(current.getStudentID()));
        intent.putExtra("title", current.getTitle());
        intent.putExtra("description", current.getDescription());
        intent.putExtra("year", Integer.toString(current.getYear()));
        intent.putExtra("first_name", current.getFirst_name());
        intent.putExtra("second_name", current.getSecond_name());

        startActivity(intent);
    }

    private int getStudentID() {
        Intent intent = getIntent();
        int studentID = Integer.parseInt(intent.getStringExtra("studentID"));

        return studentID;
    }






    public class MyAsyncTasks extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String textViewStr;

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewProjects.this);
            progressDialog.setMessage("Fetching projects...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(String... params) {
            RequestQueue queue = Volley.newRequestQueue(ViewProjects.this);
            String apiURL = "http://web.socem.plymouth.ac.uk/COMP2000/api/students/";

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, apiURL, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject project = response.getJSONObject(i);

                                    int studentID = project.getInt("studentID");

                                    if (studentID == currentStudentID) {
                                        int projectID = project.getInt("projectID");
                                        String title = project.getString("title");
                                        String description = project.getString("description");
                                        int year = project.getInt("year");
                                        String first_name = project.getString("first_Name");
                                        String second_name = project.getString("second_Name");

                                        sp = new StudentProject(projectID, studentID, title, description, year, first_name, second_name);

                                        textViewStr = title + " (" + year + ")";

                                        projectHeaders.add(textViewStr);
                                        studentProjects.add(sp);
                                    }
                                }

                                // This is the part with the ListView
                                ListView listView = findViewById(R.id.listview);

                                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, projectHeaders);

                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    // position = pos of element we're clicking
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        goToProjectDetails(position);
                                    }
                                });
                            }
                            catch (Exception ex) {
                                Toast.makeText(ViewProjects.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ViewProjects.this, "No projects yet...", Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(request);

            return null;
        }

        protected void onPostExecute(String s) {
            super.onPreExecute();
            Toast.makeText(ViewProjects.this, "Projects fetched.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
}


