package com.example.studentprojectapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ProjectDetails extends AppCompatActivity {
    private StudentProject sp;
    private boolean notif;
    private byte[] photo;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createNotificationChannel();

        sp = getProjectInfo();
        notif = getNotifPref();

        setTextViews();

        setPhoto();

        Button updateBtn = findViewById(R.id.btn_update);
        Button deleteBtn = findViewById(R.id.btn_delete);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUpdateProject();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(getApplicationContext(), ViewProjects.class);
                intent.putExtra("studentID", Integer.toString(sp.getStudentID()));
                intent.putExtra("notifsPref", Boolean.toString(notif));
                startActivity(intent);

                return true;

            case R.id.notification_toggle:
                if (notif) {
                    notif = false;
                    Toast.makeText(this, "Notifications are now disabled.", Toast.LENGTH_SHORT).show();
                }
                else {
                    notif = true;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setPhoto() {
        ImageView imgView = findViewById(R.id.img_projphoto);

        if (photo.length != 0) {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                imgView.setImageBitmap(bitmap);
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean getNotifPref() {
        Intent intent = getIntent();
        boolean pref = Boolean.parseBoolean(intent.getStringExtra("notifsPref"));

        return pref;
    }

    private void setTextViews() {
        TextView idTxt = findViewById(R.id.lbl_id_value);
        TextView titleTxt = findViewById(R.id.lbl_title_value);
        TextView descriptionTxt = findViewById(R.id.lbl_description_value);
        TextView yearTxt = findViewById(R.id.lbl_year_value);
        TextView nameTxt = findViewById(R.id.lbl_name_value);

        String fullName = sp.getFirst_name() + " " + sp.getSecond_name();
        String strYear = Integer.toString(sp.getYear());
        String strID = Integer.toString(sp.getStudentID());

        idTxt.setText(strID);
        titleTxt.setText(sp.getTitle());
        descriptionTxt.setText(sp.getDescription());
        yearTxt.setText(strYear);
        nameTxt.setText(fullName);

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

    private void goToUpdateProject() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, UpdateProject.class);

        intent.putExtra("projectID", Integer.toString(sp.getProjectID()));
        intent.putExtra("studentID", Integer.toString(sp.getStudentID()));
        intent.putExtra("title", sp.getTitle());
        intent.putExtra("description", sp.getDescription());
        intent.putExtra("year", Integer.toString(sp.getYear()));
        intent.putExtra("first_name", sp.getFirst_name());
        intent.putExtra("second_name", sp.getSecond_name());
        intent.putExtra("photo", photo);

        intent.putExtra("notifsPref", Boolean.toString(notif));

        startActivity(intent);
    }

    private void openHome(String studentID) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Home.class);
        intent.putExtra("studentID", studentID);
        intent.putExtra("notifsPref", Boolean.toString(notif));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear stack so user doesn't backtrack into menus for an already-deleted project
        startActivity(intent);
    }

    private void createDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure you want to delete " + sp.getTitle() + "?");
        dialog.setCancelable(false);

        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                deleteProject();
        }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.create().show();
    }

    private void deleteProject() {
        RequestQueue queue = Volley.newRequestQueue(ProjectDetails.this);

        int projectID = sp.getProjectID();
        String apiURL = "http://web.socem.plymouth.ac.uk/COMP2000/api/students/" + projectID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, apiURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) { // Display success toast on error as API returns no response
                Toast.makeText(ProjectDetails.this, "Project deleted.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);

        openHome(Integer.toString(sp.getStudentID()));

        if (notif) {
            Intent goToProjects = new Intent(getApplicationContext(), ViewProjects.class); // intent for going straight to projects on tap of notification
            goToProjects.putExtra("studentID", Integer.toString(sp.getStudentID()));
            goToProjects.putExtra("notifsPref", Boolean.toString(notif));
            showNotification(goToProjects);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Project Created";
            String description = "Notification to display when a project has been successfully deleted.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("0", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(Intent intent) {

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Project deleted")
                .setContentText("You deleted " + sp.getTitle() + ". Tap here to view your projects.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        try {
            notificationManager.notify(2, builder.build());
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(ProjectDetails.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}