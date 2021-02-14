package com.infinity.i_attendance.ui.setting.leave_type;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.utils.OnDataUpdateListener;
import com.infinity.i_attendance.utils.SharedPrefsHelper;
import com.infinity.i_attendance.utils.Utils;
import com.infinity.i_attendance.viewmodel.DataViewModel;

import java.util.List;


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
        dataViewModel.getLeaveType(user.getApi_key()).observe(this, new Observer<List<LeaveType>>() {
            @Override
            public void onChanged(List<LeaveType> LeaveType) {
                adapterLeaveType.setLeaveTypeList(LeaveType);
                adapterLeaveType.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSuccessfulDataUpdated() {
        bindRv();
    }
}