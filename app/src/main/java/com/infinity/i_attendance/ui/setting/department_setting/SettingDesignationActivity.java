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

import java.util.List;

public class SettingDesignationActivity extends AppCompatActivity implements OnDataUpdateListener {

    private TextView emptyMsg;
    private RecyclerView recyclerView;
    private AdapterDesignation adapterDesignation;
    private FloatingActionButton fabAddDesignation;
    private User superUser;
    private int dpt_id;
    private List<Designation> designationList;

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

        emptyMsg = findViewById(R.id.emptyMsg);
        recyclerView = findViewById(R.id.rvDesignationHolder);
        adapterDesignation = new AdapterDesignation(this);
        adapterDesignation.setOnDataUpdateListener(SettingDesignationActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapterDesignation);

        bindRv();

        fabAddDesignation = findViewById(R.id.fabAddDesignation);
        fabAddDesignation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                                ApiManager.createDesignation(superUser.getApi_key(), dpt_id, inputText.getText().toString(), new ApiManager.OnApiResponse() {
                                    @Override
                                    public void onSucceed(ApiResponse data) {
                                        Toast.makeText(SettingDesignationActivity.this, "Designation is created.", Toast.LENGTH_SHORT).show();
                                        bindRv();
                                    }

                                    @Override
                                    public void onFailed(String errorMsg) {
                                        Toast.makeText(SettingDesignationActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
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
        dataViewModel.getDesignation(superUser.getApi_key(), dpt_id).observe(this, new Observer<List<Designation>>() {
            @Override
            public void onChanged(List<Designation> designations) {

                if (designations == null || designations.size() == 0) {
                    emptyMsg.setVisibility(View.VISIBLE);
                    return;
                } else {
                    emptyMsg.setVisibility(View.GONE);
                }

                designationList = designations;
                adapterDesignation.setDesignationList(designations);
            }
        });
    }


    @Override
    public void onSuccessfulDataUpdated() {
        bindRv();
    }
}