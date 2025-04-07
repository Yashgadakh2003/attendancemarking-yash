package com.example.attendancemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAttendanceActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAttendance;
    private AttendanceAdapter adapter;
    private TextView textViewNoAttendance;
    private DatabaseReference databaseReference;

    private String studentRollNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);


        textViewNoAttendance = findViewById(R.id.textViewNoAttendance);

        recyclerViewAttendance.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AttendanceAdapter(this, new ArrayList<>());
        recyclerViewAttendance.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("attendance");

        studentRollNumber = getIntent().getStringExtra("ROLL_NUMBER");
        if (studentRollNumber == null || studentRollNumber.isEmpty()) {
            finish();
            return;
        }

        loadAttendanceForStudent(studentRollNumber);
    }

    private void loadAttendanceForStudent(String rollNumber) {
        databaseReference.child(rollNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<AttendanceRecord> attendanceRecords = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AttendanceRecord record = snapshot.getValue(AttendanceRecord.class);
                    if (record != null) {
                        attendanceRecords.add(record);
                    }
                }

                if (!attendanceRecords.isEmpty()) {
                    adapter.setAttendanceList(attendanceRecords);
                    textViewNoAttendance.setVisibility(View.GONE);
                    recyclerViewAttendance.setVisibility(View.VISIBLE);
                } else {
                    recyclerViewAttendance.setVisibility(View.GONE);
                    textViewNoAttendance.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                textViewNoAttendance.setVisibility(View.VISIBLE);
                recyclerViewAttendance.setVisibility(View.GONE);
                // Consider logging the error: Log.e("FirebaseError", "Database operation cancelled", databaseError.toException());
            }
        });
    }
}