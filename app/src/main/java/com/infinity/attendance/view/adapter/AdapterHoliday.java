package com.infinity.attendance.view.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.Holiday;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

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
    private List<Holiday> holidayList = new ArrayList<>();
    private OnDataUpdateListener<List<Holiday>> onDataUpdateListener;

    public AdapterHoliday(Context context) {
        this.context = context;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener<List<Holiday>> onDataUpdateListener) {
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
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                _deleteHoliday(holiday);
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

    private void _deleteHoliday(Holiday holiday) {
        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.deleteHoliday(holiday.getId()).observe((LifecycleOwner) context, new Observer<ApiResponse<Holiday>>() {
            @Override
            public void onChanged(ApiResponse<Holiday> holidayApiResponse) {
                if (holidayApiResponse != null && !holidayApiResponse.isError()) {
                    if (holidayApiResponse != null) {
                        onDataUpdateListener.onSuccessfulDataUpdated(holidayApiResponse.getData());
                    }

                } else {
                    Toast.makeText(context, context.getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateHoliday(Holiday holiday) {

        // set the custom layout
        final View customLayout = LayoutInflater.from(context).inflate(R.layout.dialog_add_holiday, null);
        TextInputEditText inputName = customLayout.findViewById(R.id.inputName);
        inputName.setText(holiday.getName());
        AutoCompleteTextView datePicker = customLayout.findViewById(R.id.datePicker);
        datePicker.setText(holiday.getDate());
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
                                String selectedDateStr = new SimpleDateFormat("yyyy-MMMM-dd", Locale.ENGLISH).format(calendar.getTime());
                                datePicker.setText(selectedDateStr);
                                holiday.setDate(selectedDateStr);
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

                        if (TextUtils.isEmpty(inputName.getText()) || TextUtils.isEmpty(datePicker.getText())) {
                            inputName.setError("can't be empty");
                            datePicker.setError("can't be empty");
                            return;
                        }

                        holiday.setName(inputName.getText().toString());

                        DataViewModel dataViewModel = new DataViewModel();
                        dataViewModel.updateHoliday(holiday).observe((LifecycleOwner) context, new Observer<ApiResponse<Holiday>>() {
                            @Override
                            public void onChanged(ApiResponse<Holiday> holidayApiResponse) {
                                if (holidayApiResponse != null && !holidayApiResponse.isError()) {
                                    Toast.makeText(context, "Successfully updated.", Toast.LENGTH_SHORT).show();
                                    if (onDataUpdateListener != null) {
                                        onDataUpdateListener.onSuccessfulDataUpdated(holidayApiResponse.getData());
                                    }
                                } else {
                                    Toast.makeText(context, context.getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
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
