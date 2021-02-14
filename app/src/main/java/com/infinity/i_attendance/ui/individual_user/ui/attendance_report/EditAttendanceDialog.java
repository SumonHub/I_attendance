package com.infinity.i_attendance.ui.individual_user.ui.attendance_report;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.infinity.i_attendance.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditAttendanceDialog extends DialogFragment {
    public static final String TAG = "EditAttendanceDialog";
    public static final String SELECTED_ATTENDANCE = "SELECTED_ATTENDANCE";

    private Bundle bundle;
    private Attendance selectedAttendance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        if (getArguments() != null) {
            bundle = getArguments();
            String stringAttendance = bundle.getString(SELECTED_ATTENDANCE);
            selectedAttendance = new Gson().fromJson(stringAttendance, Attendance.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.dialog_edit_attendance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvDate = view.findViewById(R.id.tvDate);
        TextInputEditText inTime = view.findViewById(R.id.inTime);
        TextInputEditText outTime = view.findViewById(R.id.outTime);
        Button btnOk = view.findViewById(R.id.btnOk);

        tvDate.setText(selectedAttendance.getDate());
        inTime.setText(selectedAttendance.getIn_time());
        outTime.setText(selectedAttendance.getOut_time());

        inTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the current time as the default values for the picker
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        String s = new SimpleDateFormat("h:mm:ss").format(calendar.getTime());
                        inTime.setText(s);
                    }
                };

                TimePickerDialog timePickerDialog = buildD(hour, minute, onTimeSetListener);
                timePickerDialog.show();

            }
        });

        outTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the current time as the default values for the picker
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        String s = new SimpleDateFormat("H:mm:ss").format(calendar.getTime());
                        outTime.setText(s);
                    }
                };

                TimePickerDialog timePickerDialog = buildD(hour, minute, onTimeSetListener);
                timePickerDialog.show();

            }
        });

        FloatingActionButton fabBack = view.findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditAttendanceDialog.this.dismiss();
            }
        });


    }

    private TimePickerDialog buildD(int hour, int minute, TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        return new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, false);
    }
}
