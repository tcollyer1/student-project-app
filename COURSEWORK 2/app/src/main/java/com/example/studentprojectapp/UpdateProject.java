package com.example.studentprojectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

public class UpdateProject extends AppCompatActivity {
    private StudentProject sp;
    private StudentProject updatedSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_project);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createNotificationChannel();

        sp = getProjectInfo();

        setTextFields();

        Button updateBtn = findViewById(R.id.btn_submitUpdate);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProject();
            }
        });
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
        String photo = intent.getStringExtra("photo");

        return new StudentProject(projectID, studentID, title, description, year, first_name, second_name, photo);
    }

    private void setTextFields() {
        String strYear = Integer.toString(sp.getYear());
        String strID = Integer.toString(sp.getStudentID());

        getTextFieldData()[0].setText(strID);
        getTextFieldData()[1].setText(sp.getTitle());
        getTextFieldData()[2].setText(sp.getDescription());
        getTextFieldData()[3].setText(strYear);
        getTextFieldData()[4].setText(sp.getFirst_name());
        getTextFieldData()[5].setText(sp.getSecond_name());
    }

    private EditText[] getTextFieldData() {
        EditText idTxt = findViewById(R.id.txt_id);
        EditText titleTxt = findViewById(R.id.txt_title);
        EditText descriptionTxt = findViewById(R.id.txt_description);
        EditText yearTxt = findViewById(R.id.txt_year);
        EditText fnameTxt = findViewById(R.id.txt_fname);
        EditText lnameTxt = findViewById(R.id.txt_lname);

        EditText[] arr = {idTxt, titleTxt, descriptionTxt, yearTxt, fnameTxt, lnameTxt};

        return arr;
    }

    private void openHome(String studentID) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Home.class);
        intent.putExtra("studentID", studentID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    private void getUpdatedValues() {
        EditText[] projectDetails = getTextFieldData().clone();

        try {
            updatedSP = new StudentProject(sp.getProjectID(), Integer.parseInt(projectDetails[0].getText().toString()), projectDetails[1].getText().toString(), projectDetails[2].getText().toString(), Integer.parseInt(projectDetails[3].getText().toString()), projectDetails[4].getText().toString(), projectDetails[5].getText().toString(), "null");
        } catch (Exception ex) {
            Toast.makeText(UpdateProject.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProject() {
        RequestQueue queue = Volley.newRequestQueue(UpdateProject.this);

        int projectID = sp.getProjectID();
        String apiURL = "http://web.socem.plymouth.ac.uk/COMP2000/api/students/" + projectID;

        getUpdatedValues(); // sets updatedSP object to new textview values

        JSONObject updateData = new JSONObject();

        try {
            updateData.put("StudentID", updatedSP.getStudentID());
            updateData.put("Title", updatedSP.getTitle());
            updateData.put("Description", updatedSP.getDescription());
            updateData.put("Year", updatedSP.getYear());
            updateData.put("First_Name", updatedSP.getFirst_name());
            updateData.put("Second_Name", updatedSP.getSecond_name());
        } catch (Exception ex) {
            Toast.makeText(UpdateProject.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, apiURL, updateData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(UpdateProject.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);

        openHome(Integer.toString(sp.getStudentID()));

        Intent goToProjects = new Intent(getApplicationContext(), ViewProjects.class); // intent for going straight to projects on tap of notification
        goToProjects.putExtra("studentID", Integer.toString(sp.getStudentID()));
        showNotification(goToProjects);
    }

    private void showNotification(Intent intent) {

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Project updated")
                .setContentText("You just updated " + updatedSP.getTitle() + ". Tap here to view your projects.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        try {
            notificationManager.notify(1, builder.build());
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(UpdateProject.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Project Created";
            String description = "Notification to display when a project has been successfully added.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("0", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}