package com.infinity.attendance.view.ui.setting.leave_type;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.LeaveType;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.SharedPrefsHelper;
import com.infinity.attendance.utils.Utils;
import com.infinity.attendance.view.adapter.AdapterLeaveType;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;


public class SettingLeaveActivity extends AppCompatActivity implements OnDataUpdateListener {
    private AdapterLeaveType adapterLeaveType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_leave);
        //
        FloatingActionButton fabBack = findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //
        Utils.showDialogWaiting(this);
        //
        RecyclerView recyclerView = findViewById(R.id.rvLeaveType);

        adapterLeaveType = new AdapterLeaveType(this);
        adapterLeaveType.setOnDataUpdateListener(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapterLeaveType);
        bindRv();

        FloatingActionButton fab = findViewById(R.id.fabAddLeaveType);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddLeaveTypeDialog dialog = AddLeaveTypeDialog.newInstance(null);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialog.show(ft, AddLeaveTypeDialog.TAG);
                dialog.setOnDataUpdateListener(SettingLeaveActivity.this);
            }
        });
    }

    private void bindRv() {
        // shared pref
        User user = SharedPrefsHelper.getSuperUser(SettingLeaveActivity.this);
        //
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getLeaveType(user.getApi_key()).observe(this, new Observer<ApiResponse<LeaveType>>() {
            @Override
            public void onChanged(ApiResponse<LeaveType> leaveTypeApiResponse) {
                if (leaveTypeApiResponse != null && !leaveTypeApiResponse.isError()) {
                    adapterLeaveType.setLeaveTypeList(leaveTypeApiResponse.getResults());
                } else {
                    Toast.makeText(SettingLeaveActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onSuccessfulDataUpdated() {
        bindRv();
    }
}