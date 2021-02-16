package com.infinity.attendance.view.ui.setting.role;

import android.os.Bundle;
import android.util.Log;
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
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.SharedPrefsHelper;
import com.infinity.attendance.utils.Utils;
import com.infinity.attendance.view.adapter.AdapterRole;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

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
        dataViewModel.getRoleLiveData(user.getApi_key()).observe(this, new Observer<ApiResponse<Role>>() {
            @Override
            public void onChanged(ApiResponse<Role> roleApiResponse) {
                if (roleApiResponse != null && !roleApiResponse.isError()) {
                    roleList = roleApiResponse.getResults();
                    adapterRole.setRoleList(roleList);
                    adapterRole.notifyDataSetChanged();
                    rvRoleHolder.smoothScrollToPosition(adapterRole.getItemCount() - 1);
                } else {
                    Toast.makeText(SettingRoleActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onSuccessfulDataUpdated() {
        Log.d(TAG, "onSuccessfulDataUpdated: ");

        _fetchData();
    }
}