package com.infinity.attendance.view.ui.setting.leave_type;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.LeaveType;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.util.List;

public class AddLeaveTypeDialog extends DialogFragment {
    public static final String TAG = "AddLeaveTypeDialog";
    public static final String SELECTED_LEAVE_TYPE = "SELECTED_LEAVE";
    private TextInputEditText inputLeaveName;
    private TextInputEditText inputLeaveBalance;
    private LeaveType selectedLeaveType;
    private boolean moodUpdate = false;

    private OnDataUpdateListener<List<LeaveType>> onDataUpdateListener;

    public static AddLeaveTypeDialog newInstance(@Nullable Bundle bundle) {
        AddLeaveTypeDialog fragment = new AddLeaveTypeDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener<List<LeaveType>> onDataUpdateListener) {
        this.onDataUpdateListener = onDataUpdateListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            selectedLeaveType = new Gson().fromJson(bundle.getString(SELECTED_LEAVE_TYPE), LeaveType.class);
            moodUpdate = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.dialog_add_leave_type, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_add_leave_type, null);

        inputLeaveName = view.findViewById(R.id.inputLeaveName);
        inputLeaveBalance = view.findViewById(R.id.inputLeaveBalance);

        if (moodUpdate) {
            inputLeaveName.setText(selectedLeaveType.getName());
            inputLeaveBalance.setText(selectedLeaveType.getBalance());
        }


        final AlertDialog alertDialog = new MaterialAlertDialogBuilder(getActivity())
                .setView(view)
                .setPositiveButton("confirm", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setOnClickListener(onAddOrUpdateLeaveType());
            }
        });

        return alertDialog;
    }

    private View.OnClickListener onAddOrUpdateLeaveType() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(inputLeaveName.getText()) || TextUtils.isEmpty(inputLeaveBalance.getText())) {
                    inputLeaveName.setError("can't be empty");
                    inputLeaveBalance.setError("can't be empty");
                    return;
                }

                String name = inputLeaveName.getText().toString();
                String balance = inputLeaveBalance.getText().toString();
                //
                LeaveType newLeaveType = new LeaveType();

                if (moodUpdate) {
                    newLeaveType.setId(selectedLeaveType.getId());
                    newLeaveType.setName(name);
                    newLeaveType.setBalance(balance);
                    //
                    _updateLeaveType(newLeaveType);
                } else {
                    newLeaveType.setName(name);
                    newLeaveType.setBalance(balance);
                    //
                    _addLeaveType(newLeaveType);
                }

            }
        };
    }

    private void _updateLeaveType(LeaveType newLeaveType) {
        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.updateLeaveType(newLeaveType)
                .observe(getViewLifecycleOwner(), new Observer<ApiResponse<LeaveType>>() {
                    @Override
                    public void onChanged(ApiResponse<LeaveType> leaveTypeApiResponse) {
                        if (leaveTypeApiResponse != null && !leaveTypeApiResponse.isError()) {
                            if (onDataUpdateListener != null) {
                                Toast.makeText(getContext(), leaveTypeApiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                onDataUpdateListener.onSuccessfulDataUpdated(leaveTypeApiResponse.getData());
                                AddLeaveTypeDialog.this.dismiss();
                            }
                        } else {
                            Toast.makeText(getContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void _addLeaveType(LeaveType newLeaveType) {
        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.addLeaveType(newLeaveType)
                .observe(getViewLifecycleOwner(), new Observer<ApiResponse<LeaveType>>() {
                    @Override
                    public void onChanged(ApiResponse<LeaveType> leaveTypeApiResponse) {
                        if (leaveTypeApiResponse != null && !leaveTypeApiResponse.isError()) {
                            if (onDataUpdateListener != null) {
                                Toast.makeText(getContext(), leaveTypeApiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                onDataUpdateListener.onSuccessfulDataUpdated(leaveTypeApiResponse.getData());
                                AddLeaveTypeDialog.this.dismiss();
                            }
                        } else {
                            Toast.makeText(getContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
