package com.example.attendancemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private List<AttendanceRecord> attendanceList;
    private Context context;

    public AttendanceAdapter(Context context, List<AttendanceRecord> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceRecord record = attendanceList.get(position);
        holder.textViewDate.setText(record.getDate().toString()); // Format date as needed
        holder.textViewStatus.setText(record.isPresent() ? "Present" : "Absent");
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    // Method to update the attendance list
    public void setAttendanceList(List<AttendanceRecord> attendanceList) {
        this.attendanceList = attendanceList;
        notifyDataSetChanged(); // Notify the adapter to refresh the view
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        TextView textViewStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate); // Ensure this ID matches your layout
            textViewStatus = itemView.findViewById(R.id.textViewStatus); // Ensure this ID matches your layout
        }
    }
}