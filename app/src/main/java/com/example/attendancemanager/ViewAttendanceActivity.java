package com.example.attendancemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

public class ViewAttendanceActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAttendance;
    private AttendanceAdapter adapter;
    private TextView textViewNoAttendance;

    private String studentRollNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        textViewNoAttendance = findViewById(R.id.textViewNoAttendance);
        recyclerViewAttendance = findViewById(R.id.recyclerViewAttendance);
        recyclerViewAttendance.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AttendanceAdapter(this, new ArrayList<>());
        recyclerViewAttendance.setAdapter(adapter);

        studentRollNumber = getIntent().getStringExtra("ROLL_NUMBER");
        if (studentRollNumber == null || studentRollNumber.isEmpty()) {
            finish();
            return;
        }

        loadAttendanceForStudent(studentRollNumber);
    }

    private void loadAttendanceForStudent(String rollNumber) {
        AttendanceDatabaseHelper dbHelper = new AttendanceDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                AttendanceDatabaseHelper.TABLE_ATTENDANCE,
                null,
                AttendanceDatabaseHelper.COLUMN_ROLL + "=?",
                new String[]{rollNumber},
                null,
                null,
                AttendanceDatabaseHelper.COLUMN_DATE + " DESC"
        );

        List<AttendanceRecord> attendanceRecords = new ArrayList<>();
        while (cursor.moveToNext()) {
            long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(AttendanceDatabaseHelper.COLUMN_DATE));
            boolean present = cursor.getInt(cursor.getColumnIndexOrThrow(AttendanceDatabaseHelper.COLUMN_PRESENT)) == 1;
            String roll = cursor.getString(cursor.getColumnIndexOrThrow(AttendanceDatabaseHelper.COLUMN_ROLL));
            attendanceRecords.add(new AttendanceRecord(new Date(timestamp), present,roll)); // ✅ Add to list
        }

        cursor.close();
        db.close();

        if (!attendanceRecords.isEmpty()) {
            adapter.setAttendanceList(attendanceRecords);
            textViewNoAttendance.setVisibility(View.GONE);
            recyclerViewAttendance.setVisibility(View.VISIBLE);
        } else {
            recyclerViewAttendance.setVisibility(View.GONE);
            textViewNoAttendance.setVisibility(View.VISIBLE);
        }
    }
}
