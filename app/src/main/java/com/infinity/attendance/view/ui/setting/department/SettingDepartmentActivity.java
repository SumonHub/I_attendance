package com.infinity.attendance.view.ui.setting.department;

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
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.Utils;
import com.infinity.attendance.view.adapter.AdapterDepartment;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.util.List;

public class SettingDepartmentActivity extends AppCompatActivity {

    private static final String TAG = "SettingDepartment";
    private AdapterDepartment adapterDepartment;

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
        adapterDepartment = new AdapterDepartment(this);
        adapterDepartment.setOnDataUpdateListener(new OnDataUpdateListener<List<Department>>() {
            @Override
            public void onSuccessfulDataUpdated(List<Department> object) {
                _updateAdapter(object);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rvDepartmentHolder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapterDepartment);

        _getData();

        FloatingActionButton fabAddDepartment = findViewById(R.id.fabAddDepartment);
        fabAddDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _addDepartmentDialog();
            }
        });
    }

    private void _addDepartmentDialog() {
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

                        Department department = new Department();
                        department.setName(inputText.getText().toString());

                        _addDepartment(department);

                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void _addDepartment(Department department) {
        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.addDepartment(department)
                .observe(SettingDepartmentActivity.this, new Observer<ApiResponse<Department>>() {
                    @Override
                    public void onChanged(ApiResponse<Department> departmentApiResponse) {
                        if (departmentApiResponse != null) {
                            Toast.makeText(SettingDepartmentActivity.this, "Department is created.", Toast.LENGTH_SHORT).show();
                            adapterDepartment.setDepartmentList(departmentApiResponse.getData());
                        } else {
                            Toast.makeText(SettingDepartmentActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void _getData() {
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getAllDepartment().observe(this, new Observer<ApiResponse<Department>>() {
            @Override
            public void onChanged(ApiResponse<Department> departmentApiResponse) {
                if (departmentApiResponse != null) {
                    _updateAdapter(departmentApiResponse.getData());
                }
            }
        });
    }

    private void _updateAdapter(List<Department> data) {
        TextView emptyMsg = findViewById(R.id.emptyMsg);
        adapterDepartment.setDepartmentList(data);
        if (adapterDepartment.getItemCount() > 0) {
            emptyMsg.setVisibility(View.GONE);
        } else {
            emptyMsg.setVisibility(View.VISIBLE);
        }
    }

}