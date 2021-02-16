package com.infinity.attendance.view.ui.setting.office_time;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.OfficeTime;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.SharedPrefsHelper;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditOfficeTimeDialog extends DialogFragment {
    public static final String TAG = "EditOfficeTimeDialog";
    public static final String SELECTED_DAY = "SELECTED_DAY";
    //
    // Get Current Date
    final Calendar c = Calendar.getInstance();
    int cYear = c.get(Calendar.YEAR);
    int cMonth = c.get(Calendar.MONTH);
    int cDay = c.get(Calendar.DAY_OF_MONTH);
    private OfficeTime selectedOfficeTime;
    private OnDataUpdateListener onDataUpdateListener;
    private Date startTime, endTime;

    public static EditOfficeTimeDialog newInstance(Bundle bundle) {
        EditOfficeTimeDialog fragment = new EditOfficeTimeDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener onDataUpdateListener) {
        this.onDataUpdateListener = onDataUpdateListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        if (getArguments() != null) {
            selectedOfficeTime = new Gson().fromJson(getArguments().getString(SELECTED_DAY), OfficeTime.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.dialog_setup_office_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fabBack = view.findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditOfficeTimeDialog.this.dismiss();
            }
        });

        TextView dayName = view.findViewById(R.id.dayName);
        dayName.setText(selectedOfficeTime.getDay_name());
        TextInputEditText inputStartTime = view.findViewById(R.id.inputStartTime);
        inputStartTime.setText(selectedOfficeTime.getStarting_time());
        TextInputEditText inputEndTime = view.findViewById(R.id.inputEndTime);
        inputEndTime.setText(selectedOfficeTime.getEnding_time());
        //
        String[] list = {"ON DAY", "OFF DAY"};
        AutoCompleteTextView dayStatusMenu = view.findViewById(R.id.dayStatusMenu);
        dayStatusMenu.setText(list[selectedOfficeTime.getStatus() - 1]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_list_item_1, list);
        dayStatusMenu.setAdapter(adapter);
        dayStatusMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedOfficeTime.setStatus(i + 1);
            }
        });
        //
        inputStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                String s = new SimpleDateFormat("H:mm:00").format(calendar.getTime());

                                selectedOfficeTime.setStarting_time(s);
                                inputStartTime.setText(s);
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false)
                        .show();
            }
        });
        //
        inputEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                String s = new SimpleDateFormat("H:mm:00").format(calendar.getTime());

                                selectedOfficeTime.setEnding_time(s);
                                inputEndTime.setText(s);
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false)
                        .show();
            }
        });
        //
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "selectedOfficeTime: " + selectedOfficeTime);

                try {
                    startTime = new SimpleDateFormat("H:mm:ss").parse(selectedOfficeTime.getStarting_time());
                    endTime = new SimpleDateFormat("H:mm:ss").parse(selectedOfficeTime.getEnding_time());
                } catch (ParseException e) {
                    Log.d(TAG, "onClick: " + e.getMessage());
                }

                if (startTime.before(endTime)) {
                    updateOfficeTime();
                } else {
                    Toast.makeText(getContext(), "Ending time must be after then Starting time. ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateOfficeTime() {

        Log.d(TAG, "updateOfficeTime: " + selectedOfficeTime);

        User superuser = SharedPrefsHelper.getSuperUser(getContext());

        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.updateOfficeTime(superuser.getApi_key(), selectedOfficeTime)
                .observe(getViewLifecycleOwner(), new Observer<ApiResponse>() {
                    @Override
                    public void onChanged(ApiResponse apiResponse) {
                        if (apiResponse != null && !apiResponse.isError()) {
                            Toast.makeText(getContext(), apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            onDataUpdateListener.onSuccessfulDataUpdated();
                            EditOfficeTimeDialog.this.dismiss();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
