package com.infinity.attendance.view.ui.setting.department_setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.Department;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.SharedPrefsHelper;
import com.infinity.attendance.utils.Utils;
import com.infinity.attendance.view.adapter.AdapterDepartment;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.util.ArrayList;
import java.util.List;

public class SettingDepartmentActivity extends AppCompatActivity implements OnDataUpdateListener {

    private static final String TAG = "SettingDepartment";
    List<Department> departmentList = new ArrayList<>();
    private TextView emptyMsg;
    private RecyclerView recyclerView;
    private AdapterDepartment adapterDepartment;
    private FloatingActionButton fabAddDepartment;
    private User superUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_department);
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
        //
        emptyMsg = findViewById(R.id.emptyMsg);
        recyclerView = findViewById(R.id.rvDepartmentHolder);
        adapterDepartment = new AdapterDepartment(this);
        adapterDepartment.setOnDataUpdateListener(SettingDepartmentActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapterDepartment);

        bindRv();

        fabAddDepartment = findViewById(R.id.fabAddDepartment);
        fabAddDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.dialog_add_dp_dg, null);
                TextView headline = customLayout.findViewById(R.id.headline);
                headline.setText("Add department");
                TextInputEditText inputText = customLayout.findViewById(R.id.inputText);
                inputText.setHint("Department name");
                // create and show the alert dialog
                final AlertDialog alertDialog = new MaterialAlertDialogBuilder(SettingDepartmentActivity.this)
                        .setView(customLayout)
                        .setPositiveButton("confirm", null)
                        .create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        btnPositive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (TextUtils.isEmpty(inputText.getText())) {
                                    inputText.setError("can't be empty");
                                    return;
                                }

                                DataViewModel dataViewModel = new DataViewModel();
                                dataViewModel.createDepartment(superUser.getApi_key(), inputText.getText().toString())
                                        .observe(SettingDepartmentActivity.this, new Observer<ApiResponse>() {
                                            @Override
                                            public void onChanged(ApiResponse apiResponse) {
                                                if (apiResponse != null && !apiResponse.isError()) {
                                                    Toast.makeText(SettingDepartmentActivity.this, "Department is created.", Toast.LENGTH_SHORT).show();
                                                    bindRv();
                                                } else {
                                                    Toast.makeText(SettingDepartmentActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                alertDialog.dismiss();
                            }
                        });
                    }
                });

                alertDialog.show();
            }
        });

    }

    private void bindRv() {
        // shared pref
        superUser = SharedPrefsHelper.getSuperUser(this);
        //
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getAllDepartment(superUser.getApi_key()).observe(this, new Observer<ApiResponse<Department>>() {
            @Override
            public void onChanged(ApiResponse<Department> departmentApiResponse) {
                if (departmentApiResponse != null && !departmentApiResponse.isError()) {
                    departmentList = departmentApiResponse.getResults();
                    adapterDepartment.setDepartmentList(departmentList);

                    if (adapterDepartment.getItemCount() > 0) {
                        emptyMsg.setVisibility(View.GONE);
                    } else {
                        emptyMsg.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onSuccessfulDataUpdated() {
        bindRv();
    }
}