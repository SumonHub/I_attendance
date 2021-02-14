package com.infinity.i_attendance.ui.manage_user;

import android.content.Intent;
import android.os.Bundle;
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
import com.infinity.i_attendance.ui.individual_user.SingleUserActivity;
import com.infinity.i_attendance.ui.manage_user.adapter.UserAdapter;
import com.infinity.i_attendance.ui.manage_user.fragment.CreateOrUpdateUserDialog;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.utils.ConstantKey;
import com.infinity.i_attendance.utils.OnDataUpdateListener;
import com.infinity.i_attendance.utils.SharedPrefsHelper;
import com.infinity.i_attendance.utils.Utils;
import com.infinity.i_attendance.viewmodel.DataViewModel;

import java.util.List;

public class ManageUserActivity extends AppCompatActivity implements OnDataUpdateListener {

    private static final String TAG = "ManageUserActivity";
    private RecyclerView rvUserHolder;
    private UserAdapter userAdapter;
    private FloatingActionButton fabAdd;
    private TextView tvUserCount;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

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
        tvUserCount = findViewById(R.id.tvUserCount);
        rvUserHolder = findViewById(R.id.rvUserHolder);
        userAdapter = new UserAdapter(this);

        rvUserHolder.setHasFixedSize(true);
        rvUserHolder.setLayoutManager(new LinearLayoutManager(this));
        rvUserHolder.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvUserHolder.setAdapter(userAdapter);
        userAdapter.setOnItemClick(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Gson gson = new Gson();
                String stringUser = gson.toJson(userList.get(position));

                Intent intent = new Intent(ManageUserActivity.this, SingleUserActivity.class);
                intent.putExtra(ConstantKey.SELECTED_USER, stringUser);
                startActivity(intent);
            }
        });


        //
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: 8/20/2020 pass user list
                CreateOrUpdateUserDialog dialog = CreateOrUpdateUserDialog.newInstance(null);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialog.show(ft, CreateOrUpdateUserDialog.TAG);
                dialog.setOnDataUpdateListener(ManageUserActivity.this);

            }
        });

        Utils.onScrollFabBehaviour(rvUserHolder, fabAdd);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindRv();

    }

    private void bindRv() {
        // shared pref

        final User superUser = SharedPrefsHelper.getSuperUser(ManageUserActivity.this);
        //
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getAllUserLiveData(superUser.getApi_key()).observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                // TODO: 8/20/2020
                userList = users;
                userAdapter.updateList(userList);
                userAdapter.notifyDataSetChanged();
                tvUserCount.setText("" + userList.size());
            }
        });
    }

    @Override
    public void onSuccessfulDataUpdated() {
        bindRv();
    }
}