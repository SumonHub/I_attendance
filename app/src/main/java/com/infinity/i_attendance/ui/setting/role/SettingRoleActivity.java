package com.infinity.i_attendance.ui.setting.role;

import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class SettingRoleActivity extends AppCompatActivity implements OnDataUpdateListener {

    private static final String TAG = "SetupRoleActivity";
    private RecyclerView rvRoleHolder;
    private AdapterRole adapterRole;
    private List<Role> roleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_role);
        // back button
        FloatingActionButton fabBack = findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //
        Utils.showDialogWaiting(this);
        // recyclearview

        rvRoleHolder = findViewById(R.id.rvRoleHolder);

        adapterRole = new AdapterRole(this);
        adapterRole.setOnDataUpdateListener(this);

        rvRoleHolder.setHasFixedSize(true);
        rvRoleHolder.setLayoutManager(new LinearLayoutManager(this));
        rvRoleHolder.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvRoleHolder.setAdapter(adapterRole);

        //
        _fetchData();
        // fab
        FloatingActionButton fabAddRole = findViewById(R.id.fabAddRole);
        fabAddRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRoleDialog addRoleDialog = new AddRoleDialog();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                addRoleDialog.show(ft, AddRoleDialog.TAG);
                addRoleDialog.setOnDataUpdateListener(SettingRoleActivity.this);
            }
        });

    }

    public void _fetchData() {
        Log.d(TAG, "_fetchData: ");
        // shared pref

        User user = SharedPrefsHelper.getSuperUser(this);
        // viewmodel
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getRoleLiveData(user.getApi_key()).observe(this, new Observer<List<Role>>() {
            @Override
            public void onChanged(List<Role> roles) {

                Log.d(TAG, "onChanged: " + roles);
                roleList = roles;
                adapterRole.setRoleList(roleList);
                adapterRole.notifyDataSetChanged();
                rvRoleHolder.smoothScrollToPosition(adapterRole.getItemCount() - 1);
            }
        });
    }

    @Override
    public void onSuccessfulDataUpdated() {
        Log.d(TAG, "onSuccessfulDataUpdated: ");

        _fetchData();
    }
}