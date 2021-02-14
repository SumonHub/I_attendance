package com.infinity.i_attendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.infinity.i_attendance.ui.individual_user.SingleUserActivity;
import com.infinity.i_attendance.ui.manage_leave.ManageLeaveActivity;
import com.infinity.i_attendance.ui.manage_user.ManageUserActivity;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.ui.setting.SettingActivity;
import com.infinity.i_attendance.ui.setting.role.Role;
import com.infinity.i_attendance.utils.ConstantKey;
import com.infinity.i_attendance.utils.SharedPrefsHelper;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DashboardActivity";
    private User superuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //
        TextView name = findViewById(R.id.name);
        TextView role = findViewById(R.id.role);
        superuser = SharedPrefsHelper.getSuperUser(this);
        name.setText(superuser.getName());
        role.setText(superuser.getRole_name());
        //
        View pThumb = findViewById(R.id.pThumb);
        pThumb.setOnClickListener(this);
        View appConfigured = findViewById(R.id.appConfigured);
        appConfigured.setOnClickListener(this);
        View manageUser = findViewById(R.id.manageUser);
        manageUser.setOnClickListener(this);
        View manageLeave = findViewById(R.id.manageLeave);
        manageLeave.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()) {
            case R.id.pThumb:
                intent = new Intent(getApplicationContext(), SingleUserActivity.class);
                intent.putExtra(ConstantKey.SELECTED_USER, new Gson().toJson(superuser));
                startActivity(intent);
                break;
            case R.id.appConfigured:
                if (superuser.getAccess_code() != Role.Setting.SUPER_ADMIN.getAccess_code() &&
                        !String.valueOf(superuser.getAccess_code()).contains(String.valueOf(Role.Setting.APP_SETUP.getAccess_code()))) {
                    Toast.makeText(this, "You are not allowed here.", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.manageUser:
                if (superuser.getAccess_code() != Role.Setting.SUPER_ADMIN.getAccess_code() &&
                        !String.valueOf(superuser.getAccess_code()).contains(String.valueOf(Role.Setting.MANAGE_USER.getAccess_code()))) {
                    Toast.makeText(this, "You are not allowed here.", Toast.LENGTH_SHORT).show();
                    return;
                }

                intent = new Intent(getApplicationContext(), ManageUserActivity.class);
                startActivity(intent);
                break;

            case R.id.manageLeave:
                if (superuser.getAccess_code() != Role.Setting.SUPER_ADMIN.getAccess_code() &&
                        !String.valueOf(superuser.getAccess_code()).contains(String.valueOf(Role.Setting.MANAGE_ATTENDANCE.getAccess_code()))) {
                    Toast.makeText(this, "You are not allowed here.", Toast.LENGTH_SHORT).show();
                    return;
                }

                intent = new Intent(getApplicationContext(), ManageLeaveActivity.class);
                startActivity(intent);
                break;
        }
    }
}