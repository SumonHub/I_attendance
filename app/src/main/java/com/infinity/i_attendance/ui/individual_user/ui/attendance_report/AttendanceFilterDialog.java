package com.infinity.i_attendance.ui.individual_user.ui.attendance_report;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.infinity.i_attendance.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AttendanceFilterDialog extends DialogFragment {

    public static String TAG = "AttendanceFilterDialog";

    // Get Current Date
    final Calendar c = Calendar.getInstance();
    int cYear = c.get(Calendar.YEAR);
    int cMonth = c.get(Calendar.MONTH);
    int cDay = c.get(Calendar.DAY_OF_MONTH);
    //
    private Date selectedStartDate, selectedEndDate;
    //
    private OnDateRangeSelected onDateRangeSelected;

    public void setOnDateRangeSelected(OnDateRangeSelected onDateRangeSelected) {
        this.onDateRangeSelected = onDateRangeSelected;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.dialog_attendace_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        FloatingActionButton fabBack = view.findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendanceFilterDialog.this.dismiss();
            }
        });
        //

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        selectedStartDate = c.getTime();
        String currentMonthFirstDate = new SimpleDateFormat("dd MMMM yyyy").format(selectedStartDate);
        selectedEndDate = Calendar.getInstance().getTime();
        String currentDate = new SimpleDateFormat("dd MMMM yyyy").format(selectedEndDate);


        TextInputEditText fromDate = view.findViewById(R.id.fromDate);
        fromDate.setText(currentMonthFirstDate);
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);

                                selectedStartDate = calendar.getTime();

                                fromDate.setText(new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(selectedStartDate));
                            }
                        }, cYear, cMonth, 1).show();
            }
        });

        TextInputEditText toDate = view.findViewById(R.id.toDate);
        toDate.setText(currentDate);
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);

                                selectedEndDate = calendar.getTime();

                                toDate.setText(new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(selectedEndDate));
                            }
                        }, cYear, cMonth, cDay).show();
            }
        });

        Button button = view.findViewById(R.id.btnGetReport);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedStartDate.after(selectedEndDate)) {
                    Toast.makeText(getContext(), "From date must be earlier then to date.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // TODO: 9/27/2020
                String formattedFromDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedStartDate);
                String formattedToDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedEndDate);
                if (onDateRangeSelected != null) {
                    onDateRangeSelected.onDateRangeSelected(formattedFromDate, formattedToDate);
                    AttendanceFilterDialog.this.dismiss();
                }
            }
        });

    }

    interface OnDateRangeSelected {
        void onDateRangeSelected(String startDate, String endDate);
    }
}
