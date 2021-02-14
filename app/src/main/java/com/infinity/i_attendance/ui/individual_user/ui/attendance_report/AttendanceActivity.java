package com.infinity.i_attendance.ui.individual_user.ui.attendance_report;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.utils.ConstantKey;
import com.infinity.i_attendance.utils.Utils;
import com.infinity.i_attendance.viewmodel.DataViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity implements AttendanceFilterDialog.OnDateRangeSelected {

    private static final String TAG = "AttendanceActivity";
    private AttendanceAdapter attendanceAdapter;
    private User selectedUser;
    private TextView emptyMsg, dateRange;
    private String startDate;
    private String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        //
        String stringSelectedUser = getIntent().getStringExtra(ConstantKey.SELECTED_USER);
        selectedUser = new Gson().fromJson(stringSelectedUser, User.class);
        //
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);

        startDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        endDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        //
        Utils.showDialogWaiting(this);
        //
        emptyMsg = findViewById(R.id.emptyMsg);
        dateRange = findViewById(R.id.dateRange);
        RecyclerView recyclerView = findViewById(R.id.rvAttendanceHolder);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        attendanceAdapter = new AttendanceAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(attendanceAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        //
        bindRv();

        FloatingActionButton fabBack = findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FloatingActionButton fabFilter = findViewById(R.id.fabFilter);
        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendanceFilterDialog dialog = new AttendanceFilterDialog();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialog.show(ft, AttendanceFilterDialog.TAG);
                dialog.setOnDateRangeSelected(AttendanceActivity.this);
            }
        });

    }

    private void bindRv() {

        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.getAttendanceReportByDateRange(selectedUser.getApi_key(), startDate, endDate).observe(this, new Observer<List<Attendance>>() {
            @Override
            public void onChanged(List<Attendance> attendances) {
                Log.d(TAG, "getAttendanceLiveData: " + attendances);
                if (attendances == null || attendances.size() == 0) {
                    emptyMsg.setVisibility(View.VISIBLE);
                    return;
                } else emptyMsg.setVisibility(View.GONE);

                attendanceAdapter.addList(attendances);
                attendanceAdapter.notifyDataSetChanged();
            }
        });

        dateRange.setText(startDate + " to " + endDate);
    }

    @Override
    public void onDateRangeSelected(String startDate, String endDate) {
        Utils.showDialogWaiting(this);
        //
        this.startDate = startDate;
        this.endDate = endDate;
        bindRv();

    }
}