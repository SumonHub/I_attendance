package com.infinity.attendance.view.ui.setting;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.infinity.attendance.data.model.Holiday;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.SharedPrefsHelper;
import com.infinity.attendance.utils.Utils;
import com.infinity.attendance.view.adapter.AdapterHoliday;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SettingHolidayActivity extends AppCompatActivity {

    private static final String TAG = "SettingHolidayActivity";
    // Get Current Date
    final Calendar c = Calendar.getInstance();
    int cYear = c.get(Calendar.YEAR);
    int cMonth = c.get(Calendar.MONTH);
    int cDay = c.get(Calendar.DAY_OF_MONTH);
    private TextView emptyMsg;
    private AdapterHoliday adapter;
    private List<Holiday> holidayList;
    private User superUser;
    private Date selectedDate;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_holiday);
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
        // shared pref
        superUser = SharedPrefsHelper.getSuperUser(this);
        //

        emptyMsg = findViewById(R.id.emptyMsg);
        RecyclerView rvLeaveHistory = findViewById(R.id.rvHoliday);
        adapter = new AdapterHoliday(this);
        adapter.setOnDataUpdateListener(new OnDataUpdateListener() {
            @Override
            public void onSuccessfulDataUpdated() {
                bindRv();
            }
        });

        rvLeaveHistory.setHasFixedSize(true);
        rvLeaveHistory.setLayoutManager(new LinearLayoutManager(this));
        rvLeaveHistory.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvLeaveHistory.setAdapter(adapter);
        //
        bindRv();
        //

        FloatingActionButton fabAddHoliday = findViewById(R.id.fabAddHoliday);
        fabAddHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHoliday();
            }
        });

    }

    private void addHoliday() {

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_add_holiday, null);
        EditText editText = customLayout.findViewById(R.id.inputText);
        TextInputEditText datePicker = customLayout.findViewById(R.id.datePicker);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SettingHolidayActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);

                                selectedDate = calendar.getTime();

                                datePicker.setText(new SimpleDateFormat("yyyy-MMMM-dd", Locale.ENGLISH).format(selectedDate));
                            }
                        }, cYear, cMonth, cDay).show();
            }
        });
        //
        final AlertDialog alertDialog = new MaterialAlertDialogBuilder(this)
                .setView(customLayout)
                .setPositiveButton("ADD", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (TextUtils.isEmpty(editText.getText()) || selectedDate == null) {
                            editText.setError("can't be empty");
                            datePicker.setError("can't be empty");
                            return;
                        }

                        String selectedDateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(selectedDate);

                        DataViewModel dataViewModel = new DataViewModel();
                        dataViewModel.addHoliday(superUser.getApi_key(), editText.getText().toString(), selectedDateStr, Holiday.ADD)
                                .observe(SettingHolidayActivity.this, new Observer<ApiResponse<Holiday>>() {
                                    @Override
                                    public void onChanged(ApiResponse<Holiday> holidayApiResponse) {
                                        if (holidayApiResponse != null && !holidayApiResponse.isError()) {
                                            Toast.makeText(SettingHolidayActivity.this, "Successfully created.", Toast.LENGTH_SHORT).show();
                                            bindRv();
                                        } else {
                                            Toast.makeText(SettingHolidayActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
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

    private void bindRv() {
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getHolidayList(superUser.getApi_key()).observe(this,
                new Observer<ApiResponse<Holiday>>() {
                    @Override
                    public void onChanged(ApiResponse<Holiday> holidayApiResponse) {
                        if (holidayApiResponse != null && !holidayApiResponse.isError()) {
                            holidayList = holidayApiResponse.getResults();
                            adapter.setHolidayList(holidayList);
                            if (adapter.getItemCount() > 0) {
                                emptyMsg.setVisibility(View.GONE);
                            } else {
                                emptyMsg.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }
}