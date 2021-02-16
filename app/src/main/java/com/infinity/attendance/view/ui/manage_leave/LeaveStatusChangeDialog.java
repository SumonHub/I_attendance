package com.infinity.attendance.view.ui.manage_leave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.Leave;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.SharedPrefsHelper;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;


public class LeaveStatusChangeDialog extends DialogFragment {

    public static final String SELECTED_LEAVE = "SELECTED_LEAVE";
    private static final String TAG = "LeaveStatusChangeDialog";
    private TextInputEditText appyingDate, inputPurpose, tv_leaveType, fromDate, toDate;
    private TextView accept, decline;

    private Leave selectedLeave;

    private OnDataUpdateListener onDataUpdateListener;

    public LeaveStatusChangeDialog() {
        // Required empty public constructor
    }

    public static LeaveStatusChangeDialog newInstance(Bundle bundle) {
        LeaveStatusChangeDialog fragment = new LeaveStatusChangeDialog();
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
            String string = getArguments().getString(SELECTED_LEAVE);
            selectedLeave = new Gson().fromJson(string, Leave.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_leave_status_change, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fabBack = view.findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeaveStatusChangeDialog.this.dismiss();
            }
        });
        //

        appyingDate = view.findViewById(R.id.currentDate);
        appyingDate.setText(selectedLeave.getApply_date());
        inputPurpose = view.findViewById(R.id.purpose);
        inputPurpose.setText(selectedLeave.getPurpose());
        fromDate = view.findViewById(R.id.fromDate);
        fromDate.setText(selectedLeave.getFrom_date());
        toDate = view.findViewById(R.id.toDate);
        toDate.setText(selectedLeave.getTo_date());
        tv_leaveType = view.findViewById(R.id.leaveType);
        tv_leaveType.setText(selectedLeave.getName());

        accept = view.findViewById(R.id.approve);
        decline = view.findViewById(R.id.declined);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 9/14/2020 api call
                changeLeaveStatus(1);
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLeaveStatus(2);
            }
        });


    }

    private void changeLeaveStatus(int status) {
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.changeLeaveStatus(SharedPrefsHelper.getSuperUser(getContext()).getApi_key(),
                selectedLeave.getId(),
                status).observe(getViewLifecycleOwner(), new Observer<ApiResponse<Leave>>() {
            @Override
            public void onChanged(ApiResponse<Leave> leaveApiResponse) {
                if (leaveApiResponse != null && !leaveApiResponse.isError()) {
                    onDataUpdateListener.onSuccessfulDataUpdated();
                    LeaveStatusChangeDialog.this.dismiss();
                } else {
                    Toast.makeText(getContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}