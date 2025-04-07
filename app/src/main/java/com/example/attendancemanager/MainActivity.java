package com.example.attendancemanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private EditText etName;
    private Button btnMarkAttendance;
    private TextView tvAttendanceStatus;
    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private List<AttendanceRecord> attendanceList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Corrected: Use R.layout

        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("attendance");

        initViews();
        setupRecyclerView();
        setupListeners();
        fetchAttendance();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        Log.d("MainActivity", "initViews - etName: " + etName);
        btnMarkAttendance = findViewById(R.id.btnMarkAttendance);
        Log.d("MainActivity", "initViews - btnMarkAttendance: " + btnMarkAttendance);
        recyclerView = findViewById(R.id.recyclerViewAttendance);
        Log.d("MainActivity", "initViews - recyclerView: " + recyclerView);
        tvAttendanceStatus = findViewById(R.id.tvAttendanceStatus);
        Log.d("MainActivity", "initViews - tvAttendanceStatus: " + tvAttendanceStatus);
    }


    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceList = new ArrayList<>();
        adapter = new AttendanceAdapter(attendanceList);
        recyclerView.setAdapter(adapter);
        Log.d("MainActivity", "setupRecyclerView - RecyclerView setup complete");
    }

    private void setupListeners() {
        btnMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "setupListeners - Mark Attendance button clicked");
                String name = etName.getText().toString().trim();
                if (!name.isEmpty()) {
                    Log.d("MainActivity", "setupListeners - Name entered: " + name);
                    markAttendance(name);
                    etName.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a student name", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "setupListeners - Toast: Please enter a student name");
                }
            }
        });
        Log.d("MainActivity", "setupListeners - OnClickListener set for Mark Attendance button");
    }

    private void fetchAttendance() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendanceList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    AttendanceRecord record = data.getValue(AttendanceRecord.class);
                    if (record != null) {
                        attendanceList.add(record);
                        Log.d("MainActivity", "fetchAttendance - Record fetched: " + record.getName() + " on " + record.getDate());
                    }
                }
                adapter.notifyDataSetChanged();
                updateAttendanceStatusTextView();
                Log.d("MainActivity", "fetchAttendance - Attendance data updated in RecyclerView");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load attendance: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("MainActivity", "fetchAttendance - Failed to load attendance: " + error.getMessage());
            }
        });
        Log.d("MainActivity", "fetchAttendance - ValueEventListener added to databaseReference");
    }

    private void markAttendance(final String name) {
        final String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d("MainActivity", "markAttendance - Marking attendance for: " + name + " on " + currentDate);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean alreadyMarked = false;

                for (DataSnapshot data : snapshot.getChildren()) {
                    AttendanceRecord record = data.getValue(AttendanceRecord.class);
                    if (record != null &&
                            record.getName().equalsIgnoreCase(name) &&
                            record.getDate().equals(currentDate)) {
                        alreadyMarked = true;
                        Log.d("MainActivity", "markAttendance - Attendance already marked for " + name + " today");
                        break;
                    }
                }

                if (alreadyMarked) {
                    Toast.makeText(MainActivity.this, "Attendance already marked for " + name + " today", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "markAttendance - Toast: Attendance already marked for " + name + " today");
                } else {
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
                    String entryId = databaseReference.push().getKey();
                    AttendanceRecord record = new AttendanceRecord(name, currentDate, currentTime);
                    Log.d("MainActivity", "markAttendance - Marking new entry for " + name + " at " + currentTime + " with ID: " + entryId);

                    if (entryId != null) {
                        databaseReference.child(entryId).setValue(record)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(MainActivity.this, "Attendance marked for " + name, Toast.LENGTH_SHORT).show();
                                    Log.d("MainActivity", "markAttendance - Toast: Attendance marked for " + name + " successfully");
                                    fetchAttendance();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(MainActivity.this, "Failed to mark attendance: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e("MainActivity", "markAttendance - Failed to mark attendance: " + e.getMessage());
                                });
                    } else {
                        Toast.makeText(MainActivity.this, "Could not generate ID", Toast.LENGTH_LONG).show();
                        Log.e("MainActivity", "markAttendance - Could not generate Firebase ID");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Firebase error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("MainActivity", "markAttendance - Firebase error: " + error.getMessage());
            }
        });
    }

    private void updateAttendanceStatusTextView() {
        if (attendanceList != null && !attendanceList.isEmpty()) {
            AttendanceRecord last = attendanceList.get(attendanceList.size() - 1);
            tvAttendanceStatus.setText("Total Records: " + attendanceList.size() +
                    "\nLast Entry: " + last.getName() + " on " + last.getDate());
            Log.d("MainActivity", "updateAttendanceStatusTextView - Updated status: " + tvAttendanceStatus.getText());
        } else {
            tvAttendanceStatus.setText("No attendance records yet.");
            Log.d("MainActivity", "updateAttendanceStatusTextView - No attendance records yet");
        }
    }
}