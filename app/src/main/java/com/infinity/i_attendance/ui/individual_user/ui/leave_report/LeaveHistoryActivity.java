package com.infinity.i_attendance.ui.individual_user.ui.leave_report;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.ui.manage_user.fragment.CreateOrUpdateUserDialog;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.utils.ConstantKey;
import com.infinity.i_attendance.utils.OnDataUpdateListener;
import com.infinity.i_attendance.utils.Utils;
import com.infinity.i_attendance.viewmodel.DataViewModel;

import java.util.List;

public class LeaveHistoryActivity extends AppCompatActivity implements OnDataUpdateListener {

    private static final String TAG = "LeaveHistoryActivity";
    private AdapterLeaveHistory adapter;
    private String stringSelectedUser;
    private User selectedUser;
    private TextView emptyMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_history);


        stringSelectedUser = getIntent().getStringExtra(ConstantKey.SELECTED_USER);
        selectedUser = new Gson().fromJson(stringSelectedUser, User.class);

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

        emptyMsg = findViewById(R.id.emptyMsg);
        RecyclerView rvLeaveHistory = findViewById(R.id.rvLeaveHistory);
        adapter = new AdapterLeaveHistory(this);

        rvLeaveHistory.setHasFixedSize(true);
        rvLeaveHistory.setLayoutManager(new LinearLayoutManager(this));
        rvLeaveHistory.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvLeaveHistory.setAdapter(adapter);

        bindRv();

        //
        FloatingActionButton fabApplyLeave = findViewById(R.id.fabApplyLeave);
        /*if (SharedPrefsHelper.getSuperUser(this).getAccess_code() != User.ROLL_GENERAL) {
            fabApplyLeave.hide();
        }*/
        fabApplyLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ApplyLeaveDialog applyLeaveDialog = new ApplyLeaveDialog();
                //
                Bundle bundle = new Bundle();
                bundle.putString(ConstantKey.SELECTED_USER, stringSelectedUser);
                applyLeaveDialog.setArguments(bundle);
                //
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                applyLeaveDialog.show(ft, CreateOrUpdateUserDialog.TAG);
                applyLeaveDialog.setOnDataUpdateListener(LeaveHistoryActivity.this);
            }
        });
    }

    private void bindRv() {

        // TODO: 9/2/2020 pass apiKey for which user you want to get history

        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getUserLeaveHistory(selectedUser.getApi_key()).observe(this, new Observer<List<Leave>>() {
            @Override
            public void onChanged(List<Leave> leaves) {
                Log.d(TAG, "getLeaveHistory: " + leaves);
                if (leaves == null || leaves.size() == 0) {
                    emptyMsg.setVisibility(View.VISIBLE);
                    return;
                } else {
                    emptyMsg.setVisibility(View.GONE);
                }

                adapter.updateLeaveList(leaves);
            }
        });
    }

    @Override
    public void onSuccessfulDataUpdated() {
        bindRv();
    }
}