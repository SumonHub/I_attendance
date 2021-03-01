package com.infinity.attendance.view.ui.individual_user.attendance_report;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.Attendance;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.ConstantKey;
import com.infinity.attendance.utils.Utils;
import com.infinity.attendance.view.adapter.AttendanceAdapter;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        dataViewModel.getAttendanceReportByDateRange(selectedUser.getUid(), startDate, endDate).observe(this, new Observer<ApiResponse<Attendance>>() {
            @Override
            public void onChanged(ApiResponse<Attendance> attendanceApiResponse) {
                if (attendanceApiResponse != null) {
                    dateRange.setText(startDate + " to " + endDate);
                    attendanceAdapter.addList(attendanceApiResponse.getData());
                    if (attendanceAdapter.getItemCount() > 0) {
                        emptyMsg.setVisibility(View.GONE);
                    } else {
                        emptyMsg.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(AttendanceActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
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