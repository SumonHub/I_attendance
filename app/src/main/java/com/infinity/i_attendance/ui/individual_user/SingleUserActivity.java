package com.infinity.i_attendance.ui.individual_user;

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
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.api.ApiManager;
import com.infinity.i_attendance.api.http.ApiResponse;
import com.infinity.i_attendance.ui.individual_user.ui.attendance_report.Attendance;
import com.infinity.i_attendance.ui.individual_user.ui.attendance_report.AttendanceActivity;
import com.infinity.i_attendance.ui.individual_user.ui.leave_report.LeaveHistoryActivity;
import com.infinity.i_attendance.ui.individual_user.ui.profile.ProfileInfoActivity;
import com.infinity.i_attendance.ui.manage_user.fragment.CreateOrUpdateUserDialog;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.ui.setting.role.Role;
import com.infinity.i_attendance.utils.ConstantKey;
import com.infinity.i_attendance.utils.OnDataUpdateListener;
import com.infinity.i_attendance.utils.SharedPrefsHelper;
import com.infinity.i_attendance.utils.SingleShotLocationProvider;
import com.infinity.i_attendance.utils.Utils;
import com.infinity.i_attendance.viewmodel.DataViewModel;
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

public class SingleUserActivity extends AppCompatActivity implements View.OnClickListener, OnDataUpdateListener {

    private static final String TAG = "SelectedUserActivity";

    private TextView uRole, uName, uDesignation, uPhone, uEmail, uJoiningDate, uBloodGroup;
    private TextView info;
    private LinearLayout secCheckIn, secCheckOut;
    private TextView fabCheckout, inTime, inAddress, outTime, outAddress;
    private User superUser, selectedUser;
    private String stringSelectedUser;
    private ExtendedFloatingActionButton fabEditInfo;
    private ExtendedFloatingActionButton fabAttendanceReport;
    private ExtendedFloatingActionButton fabLeaveReport;
    private ExtendedFloatingActionButton fabResetPass;
    private ExtendedFloatingActionButton fabProfileInfo;
    //private Bundle bundle;
    //
    private String ipAddress;
    private boolean isValidLocation = false;
    private String address;
    //

    private TextView btnBottomSheet;
    private LinearLayout layoutBottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private SpinKitView spin_kit;
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
        Log.d(TAG, "onCreate: selectedUser = " + selectedUser);
        Log.d(TAG, "onCreate: superUser = " + superUser);

        if (selectedUser.getUid().equals(superUser.getUid())) {
            selfProfile = true;
        }
        //
        uRole = findViewById(R.id.uRole);
        uName = findViewById(R.id.uName);
        uDesignation = findViewById(R.id.uDesignation);
        uPhone = findViewById(R.id.uPhone);
        uEmail = findViewById(R.id.uEmail);
        uJoiningDate = findViewById(R.id.uJoiningDate);
        uBloodGroup = findViewById(R.id.uBloodGroup);
        //
        bindData();
        //
        fabEditInfo = findViewById(R.id.fabEditInfo);
        fabEditInfo.setOnClickListener(this);
        //
        fabAttendanceReport = findViewById(R.id.fabAttendanceReport);
        fabAttendanceReport.setOnClickListener(this);
        //
        fabLeaveReport = findViewById(R.id.fabLeaveReport);
        fabLeaveReport.setOnClickListener(this);
        //
        fabResetPass = findViewById(R.id.fabResetPass);
        fabResetPass.setOnClickListener(this);
        //
        fabProfileInfo = findViewById(R.id.fabProfileInfo);
        fabProfileInfo.setOnClickListener(this);
        //
        // applyAccessControl();
        //
        info = findViewById(R.id.info);
        spin_kit = findViewById(R.id.spin_kit);
        // bottom sheet
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        btnBottomSheet = findViewById(R.id.btnBottomSheet);
        btnBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBottomSheet();
            }
        });
        //
        secCheckIn = findViewById(R.id.secCheckIn);
        secCheckOut = findViewById(R.id.secCheckOut);
        fabCheckout = findViewById(R.id.fabCheckout);
        fabCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showDialogWaiting(SingleUserActivity.this);
                checkOut();
            }
        });

        inTime = findViewById(R.id.inTime);
        inAddress = findViewById(R.id.inAddress);
        outTime = findViewById(R.id.outTime);
        outAddress = findViewById(R.id.outAddress);
        //
        if (selfProfile) {

            Log.d(TAG, "onCreate: isHoliday = " + isHoliday());
            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkIn();
                }
            }, 1500);

        } else {
            // others profile
            layoutBottomSheet.setVisibility(View.GONE);
            findViewById(R.id.secInfo).setVisibility(View.GONE);
        }
    }

    private boolean isHoliday() {
        final boolean[] feedback = {false};

        ApiManager.checkIsAnyOffday(selectedUser.getApi_key(), new ApiManager.OnApiResponse() {
            @Override
            public void onSucceed(ApiResponse data) {
                // holiday
                feedback[0] = true;

            }

            @Override
            public void onFailed(String errorMsg) {
                // on day
                feedback[0] = false;

            }
        });

        return feedback[0];
    }

    private void checkIn() {
        Log.d(TAG, "checkIn: ");
        //
        ipAddress = getIpAddress();
        if (ipAddress != null && ipAddress.equals("103.205.180.73")) {
            addAttendance("Jobbar tower", 1);
        } else {
            info.setText("You are not using office wifi. Trying to get your location...");
            checkLocationValidity(1);
        }
    }

    private void checkOut() {
        ipAddress = getIpAddress();
        //
        if (ipAddress != null && ipAddress.equals("103.205.180.73")) {
            addAttendance("Jobbar tower", 2);
        } else {
            info.setText("You are not using office wifi. Trying to get your location...");
            checkLocationValidity(2);
        }
    }

    private void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //  btnBottomSheet.setText("Close sheet");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            //  btnBottomSheet.setText("Expand sheet");
        }
    }

    public String getIpAddress() {

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

    private void applyAccessControl() {

        int accessCode = selectedUser.getAccess_code();

        if (accessCode == Role.Setting.SUPER_ADMIN.getAccess_code()) {
            return;
        }

        if (accessCode != Role.Setting.MANAGE_USER.getAccess_code()) {
            fabEditInfo.setVisibility(View.GONE);
        }

    }

    private void bindData() {
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getUserByKeyLiveData(selectedUser.getApi_key()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d(TAG, "single_user: " + user);
                selectedUser = user;
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
        });
    }

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
                createOrUpdateUserDialog.setOnDataUpdateListener(SingleUserActivity.this);
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
                resetPassword();
                break;
            case R.id.fabProfileInfo:
                intent = new Intent(SingleUserActivity.this, ProfileInfoActivity.class);
                intent.putExtra(ConstantKey.SELECTED_USER, stringSelectedUser);
                startActivity(intent);
                break;
        }
    }

    private void resetPassword() {
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

                        if (!isValid(inputNewPass, inputCnfPass)) {
                            return;
                        }

                        ApiManager.resetPassword(superUser.getApi_key(), inputCnfPass.getText().toString(),
                                new ApiManager.OnApiResponse() {
                                    @Override
                                    public void onSucceed(ApiResponse data) {
                                        Toast.makeText(SingleUserActivity.this, "Successfully update password!", Toast.LENGTH_SHORT).show();

                                        // TODO: 10/7/2020 login false
                                    }

                                    @Override
                                    public void onFailed(String errorMsg) {
                                        Log.d(TAG, "onFailed: " + errorMsg);
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

    private boolean isValid(TextInputEditText inputNewPass, TextInputEditText inputCnfPass) {
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

    @Override
    public void onSuccessfulDataUpdated() {
        Toast.makeText(this, "onSuccessfulDataUpdated", Toast.LENGTH_SHORT).show();
        // bindData();
    }

    private void checkLocationValidity(int attendanceType) {

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

                                            if (location == null) {
                                                // TODO: 9/24/2020 location not found try again.
                                                isValidLocation = false;
                                                info.setText("Can't get location. please try later.");
                                                return;
                                            }

                                            Log.d(TAG, "location: " + location);
                                            Log.d(TAG, "address: " + address);

                                            Location homeLocation = new Location("home");
                                            homeLocation.setLatitude(23.779755);
                                            homeLocation.setLongitude(90.417110);

                                            double distance = homeLocation.distanceTo(location);
                                            Log.d(TAG, "distance: " + distance);

                                            // TODO: 9/24/2020 not in office and not use office net
                                            isValidLocation = distance < 100.0;

                                            if (isValidLocation) {
                                                addAttendance(address, attendanceType);
                                            } else {
                                                info.setText("Invalid location.");
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

    private void addAttendance(String address, int statusCode) {

        info.setText(address);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
        String currentTime = new SimpleDateFormat("H:mm:ss", Locale.ENGLISH).format(new Date());

        ApiManager.addAttendance(selectedUser.getApi_key(),
                selectedUser.getUid(),
                currentDate,
                currentTime,
                address,
                statusCode,
                new ApiManager.OnApiResponse<Attendance>() {
                    @Override
                    public void onSucceed(ApiResponse<Attendance> data) {
                        Log.d(TAG, "onSucceed: ");
                        updateTodayAttendanceUI(data.getResults().get(0));
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Log.d(TAG, "onFailed: " + errorMsg);
                        Toast.makeText(SingleUserActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        spin_kit.setVisibility(View.GONE);
                        layoutBottomSheet.setVisibility(View.GONE);

                    }
                }
        );
    }

    private void updateTodayAttendanceUI(Attendance todayAttendance) {
        Log.d(TAG, "updateTodayAttendanceUI: " + todayAttendance);

        inTime.setText("Checked in at - " + todayAttendance.getIn_time());
        inAddress.setText("Checked in from - " + todayAttendance.getIn_loc());
        outTime.setText("Checked out at - " + todayAttendance.getOut_time());
        outAddress.setText("Checked out from - " + todayAttendance.getOut_loc());

        if (todayAttendance.getStatus() == 1) {
            secCheckOut.setVisibility(View.GONE);
        } else {
            secCheckOut.setVisibility(View.VISIBLE);
        }

        info.setText("Hello! Today's attendance successfully recorded. For more info see Today's attendance.");
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