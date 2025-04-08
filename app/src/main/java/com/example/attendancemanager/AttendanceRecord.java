package com.example.attendancemanager;

import java.util.Date;

public class AttendanceRecord {
    private Long timestamp; // Store as Long instead of Date for Firebase compatibility
    private boolean present;

    // Empty constructor for Firebase
    public AttendanceRecord() {}

    public AttendanceRecord(Date date, boolean present) {
        this.timestamp = date != null ? date.getTime() : null;
        this.present = present;
    }

    public Date getDate() {
        return timestamp != null ? new Date(timestamp) : null;
    }

    public void setDate(Date date) {
        this.timestamp = date != null ? date.getTime() : null;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    // Add these for Firebase
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}