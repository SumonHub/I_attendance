package com.infinity.attendance.view.ui.individual_user;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.Attendance;
import com.infinity.attendance.data.model.Role;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.ConstantKey;
import com.infinity.attendance.utils.OnApiResponse;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.SharedPrefsHelper;
import com.infinity.attendance.utils.SingleShotLocationProvider;
import com.infinity.attendance.utils.Utils;
import com.infinity.attendance.view.ui.individual_user.attendance_report.AttendanceActivity;
import com.infinity.attendance.view.ui.individual_user.leave_report.LeaveHistoryActivity;
import com.infinity.attendance.view.ui.individual_user.profile.ProfileInfoActivity;
import com.infinity.attendance.view.ui.manage_user.CreateOrUpdateUserDialog;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SingleUserActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SingleUserActivity";

    private TextView uRole, uName, uDesignation, uPhone, uEmail, uJoiningDate, uBloodGroup;
    private TextView tvAttendanceInfoBanner;
    private LinearLayout secCheckIn, secCheckOut;
    private TextView inTime;
    private TextView inAddress;
    private TextView outTime;
    private TextView outAddress;
    private ExtendedFloatingActionButton fabEditInfo;
    private BottomSheetBehavior sheetBehavior;
    private SpinKitView spin_kit;
    //
    private User superUser, selectedUser;
    private String stringSelectedUser;
    private String ipAddress;
    private boolean isValidLocation = false;
    private boolean selfProfile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user);
        //
        FloatingActionButton fabBack = findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //
        Utils.showDialogWaiting(this);
        //
        stringSelectedUser = getIntent().getStringExtra(ConstantKey.SELECTED_USER);
        selectedUser = new Gson().fromJson(stringSelectedUser, User.class);
        superUser = SharedPrefsHelper.getSuperUser(this);
        if (selectedUser.getUid().equals(superUser.getUid())) {
            selfProfile = true;
            Log.d(TAG, "onCreate: Self profile");
        }
        //
        _initView();
        //
        _bindData();
        //
        _applyAccessControl();
        //

    }

    private void _initView() {
        uRole = findViewById(R.id.uRole);
        uName = findViewById(R.id.uName);
        uDesignation = findViewById(R.id.uDesignation);
        uPhone = findViewById(R.id.uPhone);
        uEmail = findViewById(R.id.uEmail);
        uJoiningDate = findViewById(R.id.uJoiningDate);
        uBloodGroup = findViewById(R.id.uBloodGroup);
        //
        fabEditInfo = findViewById(R.id.fabEditInfo);
        fabEditInfo.setOnClickListener(this);
        //
        ExtendedFloatingActionButton fabAttendanceReport = findViewById(R.id.fabAttendanceReport);
        fabAttendanceReport.setOnClickListener(this);
        //
        ExtendedFloatingActionButton fabLeaveReport = findViewById(R.id.fabLeaveReport);
        fabLeaveReport.setOnClickListener(this);
        //
        ExtendedFloatingActionButton fabResetPass = findViewById(R.id.fabResetPass);
        fabResetPass.setOnClickListener(this);
        //
        ExtendedFloatingActionButton fabProfileInfo = findViewById(R.id.fabProfileInfo);
        fabProfileInfo.setOnClickListener(this);

        tvAttendanceInfoBanner = findViewById(R.id.tvAttendanceInfoBanner);
        spin_kit = findViewById(R.id.spin_kit);
        // bottom sheet
        LinearLayout layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        TextView btnBottomSheet = findViewById(R.id.btnBottomSheet);
        btnBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _toggleBottomSheet();
            }
        });
        //
        secCheckIn = findViewById(R.id.secCheckIn);
        secCheckOut = findViewById(R.id.secCheckOut);
        TextView fabCheckout = findViewById(R.id.fabCheckout);
        fabCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showDialogWaiting(SingleUserActivity.this);
                _checkOut();
            }
        });

        inTime = findViewById(R.id.inTime);
        inAddress = findViewById(R.id.inAddress);
        outTime = findViewById(R.id.outTime);
        outAddress = findViewById(R.id.outAddress);
        //
        if (selfProfile) {
            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    _checkIn();
                }
            }, 1500);

        } else {
            // others profile
            layoutBottomSheet.setVisibility(View.GONE);
            findViewById(R.id.secInfo).setVisibility(View.GONE);
        }
    }

    private void _checkIn() {
        ipAddress = _getIpAddress();
        if (ipAddress != null && ipAddress.equals("103.205.180.177")) {
            _addAttendance("Jobbar tower (103.205.180.177)", 1);
        } else {
            tvAttendanceInfoBanner.setText("You are not using office wifi. Trying to get your location...");
            _checkLocationValidity(1);
        }
    }

    private void _checkOut() {
        ipAddress = _getIpAddress();
        if (ipAddress != null && ipAddress.equals("103.205.180.177")) {
            _addAttendance("Jobbar tower (103.205.180.177)", 2);
        } else {
            tvAttendanceInfoBanner.setText("You are not using office wifi. Trying to get your location...");
            _checkLocationValidity(2);
        }
    }

    private void _toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //  btnBottomSheet.setText("Close sheet");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            //  btnBottomSheet.setText("Expand sheet");
        }
    }

    public String _getIpAddress() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> result = executor.submit(new Callable<String>() {
            public String call() throws Exception {

                Scanner s = new Scanner(new URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A");
                if (s.hasNext()) {
                    return s.next();
                } else {
                    return null;
                }
            }
        });

        try {
            Log.d(TAG, "getIpAddress: " + result.get());
            return result.get();
        } catch (Exception exception) {
            Log.d(TAG, "getIpAddress exception: " + exception.getMessage());
            return null;
        }
    }

    private void _applyAccessControl() {

        int accessCode = selectedUser.getAccess_code();

        if (accessCode == Role.Setting.SUPER_ADMIN.getAccess_code()) {
            return;
        }

        if (accessCode != Role.Setting.MANAGE_USER.getAccess_code()) {
            fabEditInfo.setVisibility(View.GONE);
        }

    }

    private void _bindData() {

        _getData(new OnApiResponse<User>() {
            @Override
            public void onSucceed(User user) {
                selectedUser = user;
                _updateUI(user);
            }

            @Override
            public void onFailed(String errorMsg) {

            }
        });
    }

    private void _updateUI(User user) {
        uRole.setText(user.getRole_name());
        uName.setText(user.getName());
        uDesignation.setText(user.getDg_name() + " - " + user.getDpt_name());
        uPhone.setText(user.getPhone());
        uEmail.setText(user.getEmail());
        uJoiningDate.setText("Joning date: " + user.getJoining_date());
        uBloodGroup.setText("Blood group: " + user.getBlood_group());

        if (selectedUser.getAccess_code() != Role.Setting.GENERAL.getAccess_code()) {
            uRole.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_admin));
        } else {
            uRole.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_general));
        }
    }

    private void _getData(OnApiResponse<User> onApiResponse) {
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getUser(selectedUser.getUid()).observe(this, new Observer<ApiResponse<User>>() {
            @Override
            public void onChanged(ApiResponse<User> userApiResponse) {
                if (userApiResponse != null && !userApiResponse.isError()) {
                    onApiResponse.onSucceed(userApiResponse.getData().get(0));
                } else {
                    onApiResponse.onFailed(getString(R.string.error_msg));
                }
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Intent intent;
        Bundle bundle = new Bundle();
        bundle.putString(ConstantKey.SELECTED_USER, new Gson().toJson(selectedUser));

        switch (view.getId()) {
            case R.id.fabEditInfo:
                CreateOrUpdateUserDialog createOrUpdateUserDialog = CreateOrUpdateUserDialog.newInstance(bundle);
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                createOrUpdateUserDialog.show(ft2, CreateOrUpdateUserDialog.TAG);
                createOrUpdateUserDialog.setOnDataUpdateListener(new OnDataUpdateListener<Boolean>() {
                    @Override
                    public void onSuccessfulDataUpdated(Boolean object) {
                        Toast.makeText(SingleUserActivity.this, "onSuccessfulDataUpdated", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.fabAttendanceReport:
                intent = new Intent(SingleUserActivity.this, AttendanceActivity.class);
                intent.putExtra(ConstantKey.SELECTED_USER, stringSelectedUser);
                startActivity(intent);
                break;
            case R.id.fabLeaveReport:
                intent = new Intent(SingleUserActivity.this, LeaveHistoryActivity.class);
                intent.putExtra(ConstantKey.SELECTED_USER, stringSelectedUser);
                startActivity(intent);
                break;
            case R.id.fabResetPass:
                _updatePasswordDialog();
                break;
            case R.id.fabProfileInfo:
                intent = new Intent(SingleUserActivity.this, ProfileInfoActivity.class);
                intent.putExtra(ConstantKey.SELECTED_USER, stringSelectedUser);
                startActivity(intent);
                break;
        }
    }

    private void _updatePasswordDialog() {
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);

        TextInputEditText inputNewPass = customLayout.findViewById(R.id.inputNewPass);
        TextInputEditText inputCnfPass = customLayout.findViewById(R.id.inputCnfPass);

        //
        final AlertDialog alertDialog = new MaterialAlertDialogBuilder(this)
                .setView(customLayout)
                .setPositiveButton("RESET", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!_isValid(inputNewPass, inputCnfPass)) {
                            return;
                        }

                        _updatePassword(superUser.getUid(), inputCnfPass.getText().toString(),
                                new OnApiResponse<Boolean>() {
                                    @Override
                                    public void onSucceed(Boolean data) {
                                        Toast.makeText(SingleUserActivity.this, "Successfully update password!", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onFailed(String errorMsg) {
                                        Toast.makeText(SingleUserActivity.this, errorMsg, Toast.LENGTH_SHORT).show();

                                    }
                                });

                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void _updatePassword(String uid, String newPassword, OnApiResponse<Boolean> onApiResponse) {
        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.resetPassword(uid, newPassword).observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {
                if (apiResponse != null) {
                    onApiResponse.onSucceed(true);
                } else {
                    onApiResponse.onFailed(getString(R.string.error_msg));
                }
            }
        });
    }

    private boolean _isValid(TextInputEditText inputNewPass, TextInputEditText inputCnfPass) {
        boolean feedback = true;

        if (inputNewPass.getText().length() < 6) {
            inputNewPass.setError("Password must be 6 digit.");
            feedback = false;
        } else {
            inputNewPass.setError(null);
        }

        if (!TextUtils.equals(inputNewPass.getText(), inputCnfPass.getText())) {
            inputCnfPass.setError("Password mismatch");
            feedback = false;
        } else {
            inputCnfPass.setError(null);
        }

        return feedback;
    }

    private void _checkLocationValidity(int attendanceType) {

        Dexter.withContext(this)
                .withPermission(ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Log.d(TAG, "onPermissionGranted: ");

                        SingleShotLocationProvider locator = new SingleShotLocationProvider(SingleUserActivity.this);
                        if (locator.isCanGetLocation()) {
                            locator.requestSingleUpdate(
                                    new SingleShotLocationProvider.LocationCallback() {
                                        @Override
                                        public void onNewLocationAvailable(Location location, String address) {
                                            Location homeLocation = new Location("home");
                                            homeLocation.setLatitude(23.779755);
                                            homeLocation.setLongitude(90.417110);
                                            double distance = homeLocation.distanceTo(location);
                                            // not in office and not use office net
                                            isValidLocation = distance < 100.0;
                                            if (isValidLocation) {
                                                _addAttendance(address, attendanceType);
                                            } else {
                                                tvAttendanceInfoBanner.setText("Invalid location.");
                                            }
                                        }
                                    });
                        } else {
                            locator.showGpsSettingsAlert();
                        }

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            _showNeedPermissionAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void _addAttendance(String address, int statusCode) {
        tvAttendanceInfoBanner.setText(address);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
        String currentTime = new SimpleDateFormat("H:mm:ss", Locale.ENGLISH).format(new Date());

        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.addAttendance(selectedUser.getUid(),
                currentDate,
                currentTime,
                address,
                statusCode).observe(this, new Observer<ApiResponse<Attendance>>() {
            @Override
            public void onChanged(ApiResponse<Attendance> attendanceApiResponse) {
                if (attendanceApiResponse != null && !attendanceApiResponse.isError()) {
                    _updateTodayAttendanceUI(attendanceApiResponse.getData().get(0));
                } else {
                    Toast.makeText(SingleUserActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                    spin_kit.setVisibility(View.GONE);
                    _toggleBottomSheet();
                }
            }
        });
    }

    private void _updateTodayAttendanceUI(Attendance todayAttendance) {
        inTime.setText("Checked in at - " + todayAttendance.getIn_time());
        inAddress.setText("Checked in from - " + todayAttendance.getIn_loc());
        outTime.setText("Checked out at - " + todayAttendance.getOut_time());
        outAddress.setText("Checked out from - " + todayAttendance.getOut_loc());

        if (todayAttendance.getStatus() == 1) {
            secCheckOut.setVisibility(View.GONE);
        } else {
            secCheckOut.setVisibility(View.VISIBLE);
        }

        tvAttendanceInfoBanner.setText("Hello! Today's attendance successfully recorded. For more info see Today's attendance.");
        spin_kit.setVisibility(View.GONE);

    }

    private void _showNeedPermissionAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs location permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    @Override
    public void onBackPressed() {

        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

}