package com.infinity.i_attendance.ui.setting.department_setting;

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
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.api.ApiManager;
import com.infinity.i_attendance.api.http.ApiResponse;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.utils.OnDataUpdateListener;
import com.infinity.i_attendance.utils.SharedPrefsHelper;
import com.infinity.i_attendance.utils.Utils;
import com.infinity.i_attendance.viewmodel.DataViewModel;

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

                                ApiManager.createDepartment(superUser.getApi_key(), inputText.getText().toString(), new ApiManager.OnApiResponse() {
                                    @Override
                                    public void onSucceed(ApiResponse data) {
                                        Toast.makeText(SettingDepartmentActivity.this, "Department is created.", Toast.LENGTH_SHORT).show();
                                        bindRv();
                                    }

                                    @Override
                                    public void onFailed(String errorMsg) {
                                        Toast.makeText(SettingDepartmentActivity.this, errorMsg, Toast.LENGTH_SHORT).show();

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
        dataViewModel.getAllDepartment(superUser.getApi_key()).observe(this, new Observer<List<Department>>() {
            @Override
            public void onChanged(List<Department> departments) {

                if (departments == null || departments.size() == 0) {
                    emptyMsg.setVisibility(View.VISIBLE);
                    return;
                } else {
                    emptyMsg.setVisibility(View.GONE);
                }

                departmentList = departments;
                adapterDepartment.setDepartmentList(departmentList);

            }
        });
    }

    @Override
    public void onSuccessfulDataUpdated() {
        bindRv();
    }
}