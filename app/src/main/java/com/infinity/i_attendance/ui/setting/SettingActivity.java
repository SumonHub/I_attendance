package com.infinity.i_attendance.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.ui.setting.department_setting.SettingDepartmentActivity;
import com.infinity.i_attendance.ui.setting.holiday_setting.SettingHolidayActivity;
import com.infinity.i_attendance.ui.setting.leave_type.SettingLeaveActivity;
import com.infinity.i_attendance.ui.setting.office_time.SettingOfficeTimeActivity;
import com.infinity.i_attendance.ui.setting.role.SettingRoleActivity;
import com.infinity.i_attendance.utils.Utils;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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
        ExtendedFloatingActionButton fabSetupDepartment = findViewById(R.id.fabSettingDepartment);
        fabSetupDepartment.setOnClickListener(this);
        ExtendedFloatingActionButton fabSetupRole = findViewById(R.id.fabSettingRole);
        fabSetupRole.setOnClickListener(this);
        ExtendedFloatingActionButton fabSetupOfficeTime = findViewById(R.id.fabSettingWorkingTime);
        fabSetupOfficeTime.setOnClickListener(this);
        ExtendedFloatingActionButton fabSetupHoliday = findViewById(R.id.fabSettingHoliday);
        fabSetupHoliday.setOnClickListener(this);
        ExtendedFloatingActionButton fabSetupLeave = findViewById(R.id.fabSettingLeave);
        fabSetupLeave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.fabSettingDepartment:
                intent = new Intent(this, SettingDepartmentActivity.class);
                startActivity(intent);

                break;
            case R.id.fabSettingRole:
                intent = new Intent(this, SettingRoleActivity.class);
                startActivity(intent);
                break;
            case R.id.fabSettingWorkingTime:
                intent = new Intent(this, SettingOfficeTimeActivity.class);
                startActivity(intent);
                break;
            case R.id.fabSettingHoliday:
                intent = new Intent(this, SettingHolidayActivity.class);
                startActivity(intent);
                break;
            case R.id.fabSettingLeave:
                intent = new Intent(this, SettingLeaveActivity.class);
                startActivity(intent);
                break;
        }
    }
}