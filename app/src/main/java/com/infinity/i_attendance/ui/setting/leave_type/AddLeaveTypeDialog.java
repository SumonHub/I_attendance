package com.infinity.i_attendance.ui.setting.leave_type;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.api.ApiManager;
import com.infinity.i_attendance.api.http.ApiResponse;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.utils.OnDataUpdateListener;
import com.infinity.i_attendance.utils.SharedPrefsHelper;

public class AddLeaveTypeDialog extends DialogFragment {
    public static final String TAG = "AddLeaveTypeDialog";
    public static final String SELECTED_LEAVE_TYPE = "SELECTED_LEAVE_TYPE";
    private TextInputEditText inputLeaveName;
    private TextInputEditText inputLeaveBalance;
    private LeaveType selectedLeaveType;
    private boolean moodUpdate = false;

    private OnDataUpdateListener onDataUpdateListener;

    public static AddLeaveTypeDialog newInstance(@Nullable Bundle bundle) {
        AddLeaveTypeDialog fragment = new AddLeaveTypeDialog();
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
                int actionCode = LeaveType.ADD;

                if (moodUpdate) {
                    newLeaveType.setType_id(selectedLeaveType.getType_id());
                    newLeaveType.setName(name);
                    newLeaveType.setBalance(balance);

                    actionCode = LeaveType.UPDATE;
                } else {
                    newLeaveType.setName(name);
                    newLeaveType.setBalance(balance);
                }

                Log.d(TAG, "newLeaveType: " + newLeaveType);

                User user = SharedPrefsHelper.getSuperUser(getContext());
                //
                /*DbHelper.createOrUpdateLeaveType(user.getApi_key(), newLeaveType, new DbHelper.OnApiResponse() {
                    @Override
                    public void onSucceed(ApiResponse data) {
                        Toast.makeText(getContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
                        onDataUpdateListener.onSuccessfulDataUpdated();
                        AddLeaveTypeDialog.this.dismiss();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });*/

                ApiManager.actionLeaveType(user.getApi_key(), newLeaveType, actionCode, new ApiManager.OnApiResponse() {
                    @Override
                    public void onSucceed(ApiResponse data) {
                        Toast.makeText(getContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
                        onDataUpdateListener.onSuccessfulDataUpdated();
                        AddLeaveTypeDialog.this.dismiss();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        };
    }
}
