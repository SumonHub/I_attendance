package com.infinity.attendance.view.ui.setting.role;

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
import com.infinity.attendance.data.model.Role;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.Utils;
import com.infinity.attendance.view.adapter.AdapterRole;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.util.ArrayList;
import java.util.List;

public class SettingRoleActivity extends AppCompatActivity {

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
        adapterRole.setOnDataUpdateListener(new OnDataUpdateListener<List<Role>>() {
            @Override
            public void onSuccessfulDataUpdated(List<Role> object) {
                Toast.makeText(SettingRoleActivity.this, "update", Toast.LENGTH_SHORT).show();
            }
        });

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
                addRoleDialog.setOnDataUpdateListener(new OnDataUpdateListener<List<Role>>() {
                    @Override
                    public void onSuccessfulDataUpdated(List<Role> object) {
                        Toast.makeText(SettingRoleActivity.this, "updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void _fetchData() {
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getAllUserRole().observe(this, new Observer<ApiResponse<Role>>() {
            @Override
            public void onChanged(ApiResponse<Role> roleApiResponse) {
                if (roleApiResponse != null && !roleApiResponse.isError()) {
                    roleList = roleApiResponse.getData();
                    adapterRole.setRoleList(roleList);
                    rvRoleHolder.smoothScrollToPosition(adapterRole.getItemCount() - 1);
                } else {
                    Toast.makeText(SettingRoleActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}