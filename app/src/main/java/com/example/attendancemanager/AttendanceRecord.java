package com.example.attendancemanager;

import java.util.Date;

public class AttendanceRecord {
    private Long timestamp; // Store as Long instead of Date for Firebase compatibility
    private boolean present;
    private String rollNumber;

    public AttendanceRecord(Date date, boolean present,String rollNumber) {
        this.timestamp = date != null ? date.getTime() : null;
        this.present = present;
        this.rollNumber = rollNumber;
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

    public String getRollNumber() {
        return rollNumber;
    }
    // Add these for Firebase
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}