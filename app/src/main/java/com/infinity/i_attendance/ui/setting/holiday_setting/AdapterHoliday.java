package com.infinity.i_attendance.ui.setting.holiday_setting;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.api.ApiManager;
import com.infinity.i_attendance.api.http.ApiResponse;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.utils.OnDataUpdateListener;
import com.infinity.i_attendance.utils.SharedPrefsHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterHoliday extends RecyclerView.Adapter<AdapterHoliday.DepartmentViewHolder> {
    // Get Current Date
    final Calendar c = Calendar.getInstance();
    private final Context context;
    int cYear = c.get(Calendar.YEAR);
    int cMonth = c.get(Calendar.MONTH);
    int cDay = c.get(Calendar.DAY_OF_MONTH);
    //
    User superUser;
    private List<Holiday> holidayList = new ArrayList<>();
    private OnDataUpdateListener onDataUpdateListener;
    private String selectedDateStr;
    //

    public AdapterHoliday(Context context) {
        this.context = context;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener onDataUpdateListener) {
        this.onDataUpdateListener = onDataUpdateListener;
    }

    public void setHolidayList(List<Holiday> holidayList) {
        this.holidayList = holidayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_holiday, parent, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {

        superUser = SharedPrefsHelper.getSuperUser(context);
        Holiday holiday = holidayList.get(position);
        holder.name.setText(holiday.getName());
        holder.date.setText(holiday.getDate());

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateHoliday(holiday);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialAlertDialogBuilder(context)
                        .setIcon(R.drawable.ic_stop_hand_64)
                        .setTitle("Warning")
                        .setMessage("Are your sure to delete? It may causes for data corruption. ")
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteHoliday(holiday);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

            }
        });

    }

    private void deleteHoliday(Holiday holiday) {
        ApiManager.deleteHoliday(superUser.getApi_key(),
                holiday.getId(),
                Holiday.DELETE,
                new ApiManager.OnApiResponse() {
                    @Override
                    public void onSucceed(ApiResponse data) {
                        onDataUpdateListener.onSuccessfulDataUpdated();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateHoliday(Holiday holiday) {

        // set the custom layout
        final View customLayout = LayoutInflater.from(context).inflate(R.layout.dialog_add_holiday, null);
        EditText editText = customLayout.findViewById(R.id.inputText);
        editText.setText(holiday.getName());
        TextInputEditText datePicker = customLayout.findViewById(R.id.datePicker);
        selectedDateStr = holiday.getDate();
        datePicker.setText(selectedDateStr);
        //
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                selectedDateStr = new SimpleDateFormat("yyyy-MMMM-dd", Locale.ENGLISH).format(calendar.getTime());
                                datePicker.setText(selectedDateStr);
                            }
                        }, cYear, cMonth, cDay).show();
            }
        });
        //
        final AlertDialog alertDialog = new MaterialAlertDialogBuilder(context)
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

                        if (TextUtils.isEmpty(editText.getText()) || TextUtils.isEmpty(datePicker.getText())) {
                            editText.setError("can't be empty");
                            datePicker.setError("can't be empty");
                            return;
                        }

                        ApiManager.updateHoliday(superUser.getApi_key(),
                                holiday.getId(),
                                editText.getText().toString(),
                                selectedDateStr,
                                Holiday.UPDATE,
                                new ApiManager.OnApiResponse() {
                                    @Override
                                    public void onSucceed(ApiResponse data) {
                                        Toast.makeText(context, "Updated created.", Toast.LENGTH_SHORT).show();
                                        onDataUpdateListener.onSuccessfulDataUpdated();
                                    }

                                    @Override
                                    public void onFailed(String errorMsg) {
                                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                                    }
                                });

                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();

    }

    @Override
    public int getItemCount() {
        return holidayList.size();
    }

    public class DepartmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView date;
        private final ImageView update;
        private final ImageView delete;

        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            update = itemView.findViewById(R.id.btnUpdate);
            delete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
