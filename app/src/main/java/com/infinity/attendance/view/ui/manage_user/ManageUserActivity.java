package com.infinity.attendance.view.ui.manage_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.ConstantKey;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.SharedPrefsHelper;
import com.infinity.attendance.utils.Utils;
import com.infinity.attendance.view.adapter.AdapterUser;
import com.infinity.attendance.view.ui.individual_user.SingleUserActivity;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.util.List;

public class ManageUserActivity extends AppCompatActivity {

    private static final String TAG = "ManageUserActivity";
    private RecyclerView rvUserHolder;
    private AdapterUser adapterUser;
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
        adapterUser = new AdapterUser(this);

        rvUserHolder.setHasFixedSize(true);
        rvUserHolder.setLayoutManager(new LinearLayoutManager(this));
        rvUserHolder.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvUserHolder.setAdapter(adapterUser);
        adapterUser.setOnItemClick(new AdapterUser.OnItemClickListener() {
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
                dialog.setOnDataUpdateListener(new OnDataUpdateListener<Boolean>() {
                    @Override
                    public void onSuccessfulDataUpdated(Boolean object) {
                        Toast.makeText(ManageUserActivity.this, "updated", Toast.LENGTH_SHORT).show();
                    }
                });

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
        dataViewModel.getUser("").observe(this, new Observer<ApiResponse<User>>() {
            @Override
            public void onChanged(ApiResponse<User> userApiResponse) {
                if (userApiResponse != null) {
                    userList = userApiResponse.getData();
                    adapterUser.updateList(userList);
                    adapterUser.notifyDataSetChanged();
                    tvUserCount.setText("" + userList.size());
                }
            }
        });
    }
}