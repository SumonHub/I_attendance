package com.infinity.attendance.view.ui.setting.department;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.infinity.attendance.data.model.Designation;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.Utils;
import com.infinity.attendance.view.adapter.AdapterDesignation;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.util.List;

public class SettingDesignationActivity extends AppCompatActivity {

    private static final String TAG = "SettingDesignation";
    private AdapterDesignation adapterDesignation;
    private int dpt_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_designation);
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
        dpt_id = getIntent().getIntExtra("DPT_ID", 0);

        adapterDesignation = new AdapterDesignation(this);
        adapterDesignation.setOnDataUpdateListener(new OnDataUpdateListener<List<Designation>>() {
            @Override
            public void onSuccessfulDataUpdated(List<Designation> object) {
                _updateAdapter(object);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rvDesignationHolder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapterDesignation);

        _getData();

        FloatingActionButton fabAddDesignation = findViewById(R.id.fabAddDesignation);
        fabAddDesignation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _addDesignationDialog();
            }
        });

    }

    private void _addDesignationDialog() {
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_add_dp_dg, null);
        TextView headline = customLayout.findViewById(R.id.headline);
        headline.setText("Add designation");
        TextInputEditText inputText = customLayout.findViewById(R.id.inputText);
        inputText.setHint("Designation name");
        //
        final AlertDialog alertDialog = new MaterialAlertDialogBuilder(SettingDesignationActivity.this)
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

                        Designation designation = new Designation();
                        designation.setDpt_id(dpt_id);
                        designation.setName(inputText.getText().toString());

                        _addDesignation(designation);
                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void _addDesignation(Designation designation) {
        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.addDesignation(designation)
                .observe(SettingDesignationActivity.this, new Observer<ApiResponse<Designation>>() {
                    @Override
                    public void onChanged(ApiResponse<Designation> designationApiResponse) {
                        if (designationApiResponse != null && !designationApiResponse.isError()) {
                            Toast.makeText(SettingDesignationActivity.this, "Designation is created.", Toast.LENGTH_SHORT).show();
                            _updateAdapter(designationApiResponse.getData());
                        } else {
                            Toast.makeText(SettingDesignationActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void _getData() {
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getDesignationByDpt(dpt_id).observe(this, new Observer<ApiResponse<Designation>>() {
            @Override
            public void onChanged(ApiResponse<Designation> designationApiResponse) {
                if (designationApiResponse != null) {
                    _updateAdapter(designationApiResponse.getData());
                }
            }
        });
    }

    private void _updateAdapter(List<Designation> designationList) {
        TextView emptyMsg = findViewById(R.id.emptyMsg);
        adapterDesignation.setDesignationList(designationList);
        if (adapterDesignation.getItemCount() > 0) {
            emptyMsg.setVisibility(View.GONE);
        } else {
            emptyMsg.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "_updateAdapter: " + designationList.size());
    }
}