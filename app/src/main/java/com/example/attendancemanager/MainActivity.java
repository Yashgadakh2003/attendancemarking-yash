package com.example.attendancemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private Button goToAttendanceButton;
    private Button viewAttendanceButton;
    private EditText editTextMainName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMainName = findViewById(R.id.editTextMainName);
        goToAttendanceButton = findViewById(R.id.goToAttendanceButton);
        viewAttendanceButton = findViewById(R.id.viewAttendanceButton);
        goToAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AttendanceActivity.class);
                startActivity(intent);
            }
        });
        viewAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rollNumber = editTextMainName.getText().toString().trim();
                Intent intent = new Intent(MainActivity.this, ViewAttendanceActivity.class);
                intent.putExtra("ROLL_NUMBER", rollNumber);
                startActivity(intent);
            }
        });
    }
}