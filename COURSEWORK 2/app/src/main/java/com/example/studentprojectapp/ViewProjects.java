package com.example.studentprojectapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Base64;

public class ViewProjects extends AppCompatActivity {
    private ArrayList<String> projectHeaders = new ArrayList<>();
    private ArrayList<StudentProject> studentProjects = new ArrayList<>();

    private StudentProject sp;
    private int currentStudentID;
    private boolean notifs;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_projects);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentStudentID = getStudentID();
        notifs = getNotifPref();

        try {
            GetProjects getProjects = new GetProjects();
            getProjects.execute();
        }
        catch (Exception ex) {
            Toast.makeText(ViewProjects.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtra("studentID", Integer.toString(currentStudentID));
                intent.putExtra("notifsPref", Boolean.toString(notifs));
                startActivity(intent);

                return true;

            case R.id.notification_toggle:
                if (notifs) {
                    notifs = false;
                    Toast.makeText(this, "Notifications are now disabled.", Toast.LENGTH_SHORT).show();
                }
                else {
                    notifs = true;
                    Toast.makeText(this, "Notifications are now enabled.", Toast.LENGTH_SHORT).show();
                }
                return true;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private boolean getNotifPref() {
        Intent intent = getIntent();
        boolean pref = Boolean.parseBoolean(intent.getStringExtra("notifsPref"));

        return pref;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        intent.putExtra("photo", getBytes(current.getPhoto()));
        intent.putExtra("notifsPref", Boolean.toString(notifs));

        startActivity(intent);
    }

    private int getStudentID() {
        Intent intent = getIntent();
        int studentID = Integer.parseInt(intent.getStringExtra("studentID"));

        return studentID;
    }

    public class GetProjects extends AsyncTask<String, Integer, String> {
        String textViewStr;
        //ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewProjects.this);
            progressDialog.setMessage("Fetching projects, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
//            progressDialog.show();
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GetProjects.this.cancel(true);
                    dialog.dismiss();
                }

            });
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            RequestQueue queue = Volley.newRequestQueue(ViewProjects.this);
            String apiURL = "http://web.socem.plymouth.ac.uk/COMP2000/api/students/";
//            ListView listView = findViewById(R.id.listview);

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, apiURL, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject project = response.getJSONObject(i);

                                    if (i % (response.length() / 25) == 0) { // TODO: make this work
                                        publishProgress(i * 100 / response.length());
                                    }

                                    int studentID = project.getInt("studentID");

                                    if (studentID == currentStudentID) {
                                        int projectID = project.getInt("projectID");
                                        String title = project.getString("title");
                                        String description = project.getString("description");
                                        int year = project.getInt("year");
                                        String first_name = project.getString("first_Name");
                                        String second_name = project.getString("second_Name");
                                        String photo = project.getString("photo");

                                        sp = new StudentProject(projectID, studentID, title, description, year, first_name, second_name, photo);

                                        textViewStr = title + " (" + year + ")";

                                        projectHeaders.add(textViewStr);
                                        studentProjects.add(sp);
                                    }

                                    if (i == response.length() - 1) {
                                        progressDialog.dismiss();
                                        if (studentProjects.isEmpty()) {
                                            Toast.makeText(ViewProjects.this, "No projects to show!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                // This is the part with the ListView
                                ListView listView = findViewById(R.id.listview);

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewProjects.this, android.R.layout.simple_list_item_1, projectHeaders);

                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    // position = pos of element we're clicking
                                    @RequiresApi(api = Build.VERSION_CODES.O)
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
                    Toast.makeText(ViewProjects.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(request);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancel(true);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private byte[] getBytes(String base64) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        if (!base64.equals("null")) {
            try {
                byte[] bytes = Base64.getDecoder().decode(base64);
                options.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                bytes = compressImage(bitmap);

                return bytes;

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        return new byte[0];
    }

        public static byte[] compressImage(Bitmap b) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] compressedByteArray = stream.toByteArray();
        return compressedByteArray;
    }
}


