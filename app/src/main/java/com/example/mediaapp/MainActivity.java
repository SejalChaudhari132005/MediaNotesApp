package com.example.mediaapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private EditText etTitle, etDescription, etNoteType;
    private ImageView ivPreview;
    private Button btnSelectImage, btnSaveNote, btnViewNotes;
    private Uri imageUri;
    private DBHelper dbHelper;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastX, lastY, lastZ;
    private static final int SHAKE_THRESHOLD = 600; 
    private long lastUpdate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        etTitle = findViewById(R.id.etNoteTitle);
        etDescription = findViewById(R.id.etNoteDescription);
        etNoteType = findViewById(R.id.etNoteType);
        ivPreview = findViewById(R.id.ivPreview);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSaveNote = findViewById(R.id.btnSaveNote);
        btnViewNotes = findViewById(R.id.btnViewNotes);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        });

        btnSaveNote.setOnClickListener(v -> saveNote());

        btnViewNotes.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NotesListActivity.class));
        });

        // Trigger the first notification after 5 minutes
        OneTimeWorkRequest initialRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(5, TimeUnit.MINUTES)
                .addTag("NoteReminder")
                .build();
        WorkManager.getInstance(this).enqueue(initialRequest);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String type = etNoteType.getText().toString().trim();
        String imagePath = (imageUri != null) ? imageUri.toString() : "";
        String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type.isEmpty()) type = "General";

        long id = dbHelper.insertNote(title, description, imagePath, date, type);
        if (id != -1) {
            Toast.makeText(this, "Note saved successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        etTitle.setText("");
        etDescription.setText("");
        etNoteType.setText("");
        ivPreview.setVisibility(View.GONE);
        imageUri = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ivPreview.setImageURI(imageUri);
            ivPreview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float speed = (Math.abs(x - lastX) + Math.abs(y - lastY) + Math.abs(z - lastZ)) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    Toast.makeText(this, "Device motion detected", Toast.LENGTH_SHORT).show();
                }
                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    @Override protected void onResume() { super.onResume(); if (accelerometer != null) sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL); }
    @Override protected void onPause() { super.onPause(); if (sensorManager != null) sensorManager.unregisterListener(this); }
}
