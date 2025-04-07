package com.example.attendancemanager;

import java.util.Date;

public class AttendanceRecord {
    private Date date;
    private boolean present;

    // Default constructor required for calls to DataSnapshot.getValue(AttendanceRecord.class)
    public AttendanceRecord() {}

    public AttendanceRecord(Date date, boolean present) {
        this.date = date;
        this.present = present;
    }

    public Date getDate() {
        return date;
    }

    public boolean isPresent() {
        return present;
    }
}