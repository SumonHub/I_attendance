package com.infinity.i_attendance.ui.individual_user.ui.profile.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.api.ApiManager;
import com.infinity.i_attendance.api.http.ApiResponse;
import com.infinity.i_attendance.ui.individual_user.model.UserInfo;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.utils.ConstantKey;
import com.infinity.i_attendance.viewmodel.DataViewModel;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;

import render.animations.Attention;
import render.animations.Render;

public class ContactDetailsFragment extends Fragment {

    private static final String TAG = "ContactDetailsFragment";
    private static final int FILE_REQUEST_CODE = 110;
    private static final String ARG_PARAM1 = "param1";
    private final ArrayList<MediaFile> mediaFiles = new ArrayList<>();
    private TextInputLayout lPresentAddress, lPermanentAddress, lEmail, lMobile;
    private TextInputEditText inputCurrentAddress, inputPermanentAddress;
    // private TextInputEditText inputEmail, inputMobile;
    private String valueCurrentAddress, valuePermanentAddress, valueEmail, valueMobile;
    private User selectedUser;
    private UserInfo selectedUserInfo = new UserInfo();
    private MODE mode = MODE.DISABLE;
    private String mParam1;

    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    public static ContactDetailsFragment newInstance(String stringSelectedUser) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKey.SELECTED_USER, stringSelectedUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String stringSelectedUser = getArguments().getString(ConstantKey.SELECTED_USER);
            selectedUser = new Gson().fromJson(stringSelectedUser, User.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lPresentAddress = view.findViewById(R.id.lPresentAddress);
        lPermanentAddress = view.findViewById(R.id.lPermanentAddress);
        //lEmail = view.findViewById(R.id.lEmail);
        //lMobile = view.findViewById(R.id.lMobile);
        //
        inputCurrentAddress = view.findViewById(R.id.inputPresentAddress);
        inputPermanentAddress = view.findViewById(R.id.inputPermanentAddress);
        //inputEmail = view.findViewById(R.id.inputEmail);
        //inputMobile = view.findViewById(R.id.inputMobile);

        updateUI(false);
        bindData();

        ExtendedFloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mode == MODE.DISABLE) {
                    // enable view
                    updateUI(true);
                    mode = MODE.ENABLE;
                    fab.setIcon(getContext().getDrawable(R.drawable.icon_double_tick_32));
                    fab.setBackgroundColor(Color.RED);
                    fab.setText("Done");

                } else if (mode == MODE.ENABLE) {
                    // disable view
                    updateUI(false);
                    mode = MODE.DISABLE;
                    fab.setIcon(getContext().getDrawable(R.drawable.icon_edit_64));
                    fab.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
                    fab.setText("Edit");

                    _buildSelectedUserInfo();
                    ApiManager.addUserInfo(selectedUser.getApi_key(), selectedUserInfo, new ApiManager.OnApiResponse() {
                        @Override
                        public void onSucceed(ApiResponse data) {
                            Toast.makeText(getContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(String errorMsg) {
                            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }

    private void bindData() {

        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getUserInfoByKeyLiveData(selectedUser.getApi_key()).observe(getViewLifecycleOwner(),
                new Observer<UserInfo>() {
                    @Override
                    public void onChanged(UserInfo userInfo) {
                        if (userInfo == null) {
                            return;
                        }

                        Log.d(TAG, "onChanged: " + userInfo);
                        selectedUserInfo = userInfo;
                        inputCurrentAddress.setText(userInfo.getC_address());
                        inputPermanentAddress.setText(userInfo.getP_address());

                    }
                });

    }

    private void _buildSelectedUserInfo() {

        valueCurrentAddress = inputCurrentAddress.getText().toString();
        valuePermanentAddress = inputPermanentAddress.getText().toString();
        //valueEmail = inputEmail.getText().toString();
        //valueMobile = inputMobile.getText().toString();

        if (!valueCurrentAddress.isEmpty()) {
            selectedUserInfo.setC_address(valueCurrentAddress);
        }

        if (!valuePermanentAddress.isEmpty()) {
            selectedUserInfo.setP_address(valueCurrentAddress);
        }

    }

    private void updateUI(boolean isEnable) {

        inputCurrentAddress.setEnabled(isEnable);
        inputPermanentAddress.setEnabled(isEnable);
        //inputEmail.setEnabled(isEnable);
        //inputMobile.setEnabled(isEnable);

    }

    private void animateView(View view) {
        Render render = new Render(getContext());
        render.setAnimation(Attention.Wobble(view));
        render.start();
    }

    private enum MODE {
        ENABLE,
        DISABLE
    }
}