package com.example.studentprojectapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class UpdateProject extends AppCompatActivity {
    private StudentProject sp;
    private StudentProject updatedSP;
    private boolean notifs;
    private byte[] photo;
    private int SELECT_IMAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_project);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createNotificationChannel();

        sp = getProjectInfo();
        notifs = getNotifPref();

        setTextFields();
        setTextViews();

        Button updateBtn = findViewById(R.id.btn_submitUpdate);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProject();
            }
        });

        Button uploadBtn = findViewById(R.id.btn_uploadphoto);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:

                intent = new Intent(getApplicationContext(), ProjectDetails.class);
                intent.putExtra("projectID", Integer.toString(sp.getProjectID()));
                intent.putExtra("studentID", Integer.toString(sp.getStudentID()));
                intent.putExtra("title", sp.getTitle());
                intent.putExtra("description", sp.getDescription());
                intent.putExtra("year", Integer.toString(sp.getYear()));
                intent.putExtra("first_name", sp.getFirst_name());
                intent.putExtra("second_name", sp.getSecond_name());
                intent.putExtra("notifsPref", Boolean.toString(notifs));
                intent.putExtra("photo", photo);

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

    private void uploadPhoto() {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Title"), SELECT_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView fileNameTxt = findViewById(R.id.lbl_filename);

        if (requestCode == 1) {
            Uri uri = data.getData();
            fileNameTxt.setText(data.getData().getPath());
//            Toast.makeText(this, "Photo received", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getNotifPref() {
        Intent intent = getIntent();
        boolean pref = Boolean.parseBoolean(intent.getStringExtra("notifsPref"));

        return pref;
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
        photo = intent.getByteArrayExtra("photo");

        return new StudentProject(projectID, studentID, title, description, year, first_name, second_name, null);
    }

    private void setTextFields() {
        String strYear = Integer.toString(sp.getYear());
        String strID = Integer.toString(sp.getStudentID());

        getTextFieldData()[0].setText(sp.getTitle());
        getTextFieldData()[1].setText(sp.getDescription());
        getTextFieldData()[2].setText(strYear);
    }

    private EditText[] getTextFieldData() {
        EditText titleTxt = findViewById(R.id.txt_title);
        EditText descriptionTxt = findViewById(R.id.txt_description);
        EditText yearTxt = findViewById(R.id.txt_year);

        EditText[] arr = {titleTxt, descriptionTxt, yearTxt};

        return arr;
    }

    private void setTextViews() {
        String strID = Integer.toString(sp.getStudentID());

        TextView idTxt = findViewById(R.id.info_id);
        TextView fNameTxt = findViewById(R.id.info_fname);
        TextView lNameTxt = findViewById(R.id.info_lname);

        idTxt.setText(strID);
        fNameTxt.setText(sp.getFirst_name());
        lNameTxt.setText(sp.getSecond_name());
    }

    private void openHome(String studentID) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Home.class);
        intent.putExtra("studentID", studentID);
        intent.putExtra("notifsPref", Boolean.toString(notifs));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    private void getUpdatedValues() {
        EditText[] projectDetails = getTextFieldData().clone();

        try {
            updatedSP = new StudentProject(sp.getProjectID(), sp.getStudentID(), projectDetails[0].getText().toString(), projectDetails[1].getText().toString(), Integer.parseInt(projectDetails[2].getText().toString()), sp.getFirst_name(), sp.getSecond_name(), "null");
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
                    public void onErrorResponse(VolleyError error) { // Ends up in error section due to no JSON response coming back from API
                    Toast.makeText(UpdateProject.this, "Project updated.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);

        openHome(Integer.toString(sp.getStudentID()));

        if (notifs) {
            Intent goToProjects = new Intent(getApplicationContext(), ViewProjects.class); // intent for going straight to projects on tap of notification
            goToProjects.putExtra("studentID", Integer.toString(sp.getStudentID()));
            goToProjects.putExtra("notifsPref", Boolean.toString(notifs));
            showNotification(goToProjects);
        }
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