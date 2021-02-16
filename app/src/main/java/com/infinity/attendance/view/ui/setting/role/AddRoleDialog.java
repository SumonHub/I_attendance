package com.infinity.attendance.view.ui.setting.role;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.Role;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.SharedPrefsHelper;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

public class AddRoleDialog extends DialogFragment {
    public static final String TAG = "AddRoleDialog";
    public static final String SELECTED_ROLE = "SELECTED_ROLE";
    private boolean moodUpdate = false;
    private Role selectedRole;

    private EditText inputRoleTitle;
    private CheckBox checkbox_app_setup;
    private CheckBox checkbox_manage_user;
    private CheckBox checkbox_manage_attendance;
    private CheckBox checkbox_manage_leave;

    private String rollTitle;
    private StringBuilder accessCode;

    private OnDataUpdateListener onDataUpdateListener;

    public AddRoleDialog() {
    }

    // TODO: Rename and change types and number of parameters
    public static AddRoleDialog newInstance(Bundle bundle) {
        AddRoleDialog fragment = new AddRoleDialog();
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
            selectedRole = new Gson().fromJson(bundle.getString(SELECTED_ROLE), Role.class);
            moodUpdate = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.dialog_add_role, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fabBack = view.findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRoleDialog.this.dismiss();
            }
        });

        inputRoleTitle = view.findViewById(R.id.inputRoleTitle);
        //
        checkbox_app_setup = view.findViewById(R.id.checkbox_app_setup);
        checkbox_app_setup.setText(Role.Setting.APP_SETUP.getDisplayName());
        //
        checkbox_manage_user = view.findViewById(R.id.checkbox_manage_user);
        checkbox_manage_user.setText(Role.Setting.MANAGE_USER.getDisplayName());
        //
        checkbox_manage_attendance = view.findViewById(R.id.checkbox_manage_attendance);
        checkbox_manage_attendance.setText(Role.Setting.MANAGE_ATTENDANCE.getDisplayName());
        //
        checkbox_manage_leave = view.findViewById(R.id.checkbox_manage_leave);
        checkbox_manage_leave.setText(Role.Setting.MANAGE_LEAVE.getDisplayName());

        FloatingActionButton fabSetupRole = view.findViewById(R.id.fabSettingRole);


        if (moodUpdate) {
            inputRoleTitle.setText(selectedRole.getRole_name());
            //
            String parsedAccessCode = String.valueOf(selectedRole.getAccess_code());
            //
            checkbox_app_setup.setChecked(
                    parsedAccessCode.contains(String.valueOf(Role.Setting.APP_SETUP.getAccess_code())));
            checkbox_manage_user.setChecked(
                    parsedAccessCode.contains(String.valueOf(Role.Setting.MANAGE_USER.getAccess_code())));
            checkbox_manage_attendance.setChecked(
                    parsedAccessCode.contains(String.valueOf(Role.Setting.MANAGE_ATTENDANCE.getAccess_code())));
            checkbox_manage_leave.setChecked(
                    parsedAccessCode.contains(String.valueOf(Role.Setting.MANAGE_LEAVE.getAccess_code())));
        }

        fabSetupRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {

                    Role newRole = new Role();

                    if (moodUpdate) {
                        newRole.setId(selectedRole.getId());
                        newRole.setRole_name(rollTitle);
                        newRole.setAccess_code(Integer.parseInt(accessCode.toString()));
                    } else {
                        newRole.setRole_name(rollTitle);
                        newRole.setAccess_code(Integer.parseInt(accessCode.toString()));
                    }

                    Log.d(TAG, "newRole: " + newRole);

                    createOrUpdateUserRole(newRole);
                }
            }
        });
    }

    private void createOrUpdateUserRole(Role newRole) {

        User user = SharedPrefsHelper.getSuperUser(getContext());
        //
        Log.d(TAG, "createOrUpdateUserRole: " + newRole);

        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.createOrUpdateUserRole(user.getApi_key(), newRole)
                .observe(getViewLifecycleOwner(), new Observer<ApiResponse>() {
                    @Override
                    public void onChanged(ApiResponse apiResponse) {
                        if (apiResponse != null && !apiResponse.isError()) {
                            Toast.makeText(getContext(), "Successfully Add user role.", Toast.LENGTH_SHORT).show();
                            onDataUpdateListener.onSuccessfulDataUpdated();
                            AddRoleDialog.this.dismiss();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isValid() {

        rollTitle = inputRoleTitle.getText().toString();
        accessCode = new StringBuilder();

        if (checkbox_app_setup.isChecked()) {
            accessCode.append(Role.Setting.APP_SETUP.getAccess_code());
        }
        if (checkbox_manage_user.isChecked()) {
            accessCode.append(Role.Setting.MANAGE_USER.getAccess_code());
        }
        if (checkbox_manage_attendance.isChecked()) {
            accessCode.append(Role.Setting.MANAGE_ATTENDANCE.getAccess_code());
        }
        if (checkbox_manage_leave.isChecked()) {
            accessCode.append(Role.Setting.MANAGE_LEAVE.getAccess_code());
        }

        if (TextUtils.isEmpty(rollTitle) || TextUtils.isEmpty(accessCode)) {
            Toast.makeText(getContext(), "Name or access option can't be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        Log.d(TAG, "isValid: \n" + "RollTitle =" + accessCode.toString() + "\n" +
                "Access_code = " + accessCode);

        return true;
    }
}
