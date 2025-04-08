package com.example.attendancemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class AttendanceActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextRollNumber;
    private EditText editTextClass;
    private EditText editTextDivision;
    private Button markAttendanceButton;
    private TextView attendanceConfirmationTextView;
    private boolean attendanceMarked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance); // Ensure this layout file exists

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextRollNumber = findViewById(R.id.editTextRollNumber);
        editTextClass = findViewById(R.id.editTextClass);
        editTextDivision = findViewById(R.id.editTextDivision);
        markAttendanceButton = findViewById(R.id.markAttendanceButton);
        attendanceConfirmationTextView = findViewById(R.id.attendanceConfirmationTextView);

        // Set the button click listener
        markAttendanceButton.setOnClickListener(this::onMarkAttendanceButtonClick);
    }

    public void onMarkAttendanceButtonClick(View v) {
        String name = editTextName.getText().toString().trim();
        String rollNumber = editTextRollNumber.getText().toString().trim();
        String className = editTextClass.getText().toString().trim();
        String division = editTextDivision.getText().toString().trim();

        if (name.isEmpty() || rollNumber.isEmpty() || className.isEmpty() || division.isEmpty()) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!attendanceMarked) {
            // Here, instead of just showing a toast, you would:
            // 1. Create an AttendanceRecord object with the collected data.
            // 2. Save this object to your chosen storage (e.g., SQLite database using Room).

            String message = "Attendance Marked for: " + name + " (Roll: " + rollNumber + ", Class: " + className + "-" + division + ")";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            attendanceConfirmationTextView.setText("Your attendance is marked");
            attendanceConfirmationTextView.setVisibility(View.VISIBLE);

            attendanceMarked = true;
            markAttendanceButton.setEnabled(false);
            markAttendanceButton.setText("Attendance Marked");

            AttendanceDatabaseHelper dbHelper = new AttendanceDatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(AttendanceDatabaseHelper.COLUMN_ROLL, rollNumber);
            values.put(AttendanceDatabaseHelper.COLUMN_DATE, System.currentTimeMillis());
            values.put(AttendanceDatabaseHelper.COLUMN_PRESENT, 1); // assuming present is always true here

            db.insert(AttendanceDatabaseHelper.TABLE_ATTENDANCE, null, values);
            db.close();
        } else {
            Toast.makeText(this, "Attendance already marked", Toast.LENGTH_SHORT).show();
        }
    }
}