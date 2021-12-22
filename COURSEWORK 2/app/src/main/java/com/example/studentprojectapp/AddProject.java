package com.example.studentprojectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

public class AddProject extends AppCompatActivity {
    private int studentID;
    private boolean notifs;
    private StudentProject newSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createNotificationChannel();

        studentID = getStudentID();
        notifs = getNotifPref();
        setTxtFields();

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
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:

                intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtra("studentID", Integer.toString(studentID));
                intent.putExtra("notifsPref", Boolean.toString(notifs));
                startActivity(intent);
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("studentID", Integer.toString(studentID));
//                resultIntent.putExtra("notifsPref", Boolean.toString(notifs));
//                setResult(Activity.RESULT_OK, resultIntent);
//                this.finish();

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

    private int getStudentID() {
        Intent intent = getIntent();
        int studentID = Integer.parseInt(intent.getStringExtra("studentID"));

        return studentID;
    }

    private void setTxtFields() {
        TextView idTxt = findViewById(R.id.info_new_id);
        idTxt.setText(Integer.toString(studentID));
    }

    private boolean getNotifPref() {
        Intent intent = getIntent();
        boolean pref = Boolean.parseBoolean(intent.getStringExtra("notifsPref"));

        return pref;
    }

    private EditText[] getTextFieldData() {
        EditText titleTxt = findViewById(R.id.txt_new_title);
        EditText descriptionTxt = findViewById(R.id.txt_new_description);
        EditText yearTxt = findViewById(R.id.txt_new_year);
        EditText fnameTxt = findViewById(R.id.txt_new_fname);
        EditText lnameTxt = findViewById(R.id.txt_new_lname);;
        EditText arr[] = {titleTxt, descriptionTxt, yearTxt, fnameTxt, lnameTxt};

        return arr;
    }

    private void getValuesToPost() {
        EditText projectDetails[] = getTextFieldData().clone();

        try {
            newSP = new StudentProject(0, studentID, projectDetails[0].getText().toString(), projectDetails[1].getText().toString(), Integer.parseInt(projectDetails[2].getText().toString()), projectDetails[3].getText().toString(), projectDetails[4].getText().toString(), "null");
        } catch (Exception ex) {

        }
    }

    private void openHome(String studentID) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Home.class);

//        Intent goToProjects = new Intent(context, ViewProjects.class); // intent for going straight to projects on tap of notification
//        goToProjects.putExtra("studentID", studentID);
//        showNotification(goToProjects);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("studentID", studentID);
        intent.putExtra("notifsPref", Boolean.toString(notifs));

        startActivity(intent);
    }

    private void postProject() {
        RequestQueue queue = Volley.newRequestQueue(AddProject.this);

        String apiURL = "http://web.socem.plymouth.ac.uk/COMP2000/api/students/";

        getValuesToPost(); // sets newSP object to inputted values

        JSONObject postData = new JSONObject();

        try {
            postData.put("StudentID", studentID);
            postData.put("Title", newSP.getTitle());
            postData.put("Description", newSP.getDescription());
            postData.put("Year", newSP.getYear());
            postData.put("First_Name", newSP.getFirst_name());
            postData.put("Second_Name", newSP.getSecond_name());
        } catch (Exception ex) {
            Toast.makeText(AddProject.this, "oh no", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
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

        if (notifs) {
            Intent goToProjects = new Intent(getApplicationContext(), ViewProjects.class); // intent for going straight to projects on tap of notification
            goToProjects.putExtra("studentID", Integer.toString(studentID));
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
                .setContentTitle("Project added")
                .setContentText("Your project " + newSP.getTitle() + " has been created. Tap here to view your projects.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        try {
            notificationManager.notify(0, builder.build());
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(AddProject.this, ex.toString(), Toast.LENGTH_SHORT).show();
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