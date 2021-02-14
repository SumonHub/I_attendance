package com.infinity.i_attendance.ui.individual_user.ui.attendance_report;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.ui.setting.role.Role;
import com.infinity.i_attendance.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private static final String TAG = "AttendanceAdapter";
    private final Context context;
    private List<Attendance> attendanceList = new ArrayList<>();

    public AttendanceAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AttendanceAdapter.AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_attendace, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.AttendanceViewHolder holder, int position) {

        Attendance attendance = attendanceList.get(position);

        holder.date.setText(attendance.getDate());
        holder.in_time.setText(attendance.getIn_time());
        holder.inAddress.setText(attendance.getIn_loc());
        holder.inAddress.setSelected(true);

        holder.out_time.setText(attendance.getOut_time());
        holder.outAddress.setText(attendance.getOut_loc());
        holder.outAddress.setSelected(true);

        String s = "";

        if (attendance.getLate_status() == Attendance.DEFAULT) {
            s = "Default";
        } else if (attendance.getLate_status() == Attendance.MISSING_OUT_TIME) {
            s = "Missing out time";
        } else if (attendance.getLate_status() == Attendance.LATE_IN_EARLY_OUT) {
            s = "Late in Early out";
        } else if (attendance.getLate_status() == Attendance.LATE_IN) {
            s = "Late in";
        } else if (attendance.getLate_status() == Attendance.EARLY_OUT) {
            s = "Early out";
        }

        holder.late_status.setText(s);

        User superUser = SharedPrefsHelper.getSuperUser(context);

        if (String.valueOf(superUser.getAccess_code()).contains(String.valueOf(Role.Setting.MANAGE_ATTENDANCE.getAccess_code()))
                || superUser.getAccess_code() != Role.Setting.SUPER_ADMIN.getAccess_code()) {

            holder.fabEdit.setVisibility(View.GONE);
        }

        holder.fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 8/8/2020
                /*
                 *  change in or out time where date = ? and userId = ?
                 *
                 * */

                EditAttendanceDialog editAttendanceDialog = new EditAttendanceDialog();
                Bundle bundle = new Bundle();
                bundle.putString(EditAttendanceDialog.SELECTED_ATTENDANCE, new Gson().toJson(attendance));
                editAttendanceDialog.setArguments(bundle);
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                editAttendanceDialog.show(ft, EditAttendanceDialog.TAG);


            }
        });
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public void addList(List<Attendance> attendances) {
        attendanceList = attendances;
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView date, inAddress, in_time, out_time, outAddress, late_status;
        FloatingActionButton fabEdit;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            in_time = itemView.findViewById(R.id.inTime);
            inAddress = itemView.findViewById(R.id.inAddress);
            outAddress = itemView.findViewById(R.id.outAddress);
            out_time = itemView.findViewById(R.id.outTime);
            late_status = itemView.findViewById(R.id.late_status);
            fabEdit = itemView.findViewById(R.id.fabEditAttendance);
        }
    }
}
