package com.infinity.i_attendance.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.infinity.i_attendance.api.ApiManager;
import com.infinity.i_attendance.api.http.ApiResponse;
import com.infinity.i_attendance.ui.individual_user.model.UserInfo;
import com.infinity.i_attendance.ui.individual_user.ui.attendance_report.Attendance;
import com.infinity.i_attendance.ui.individual_user.ui.leave_report.Leave;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.ui.setting.department_setting.Department;
import com.infinity.i_attendance.ui.setting.department_setting.Designation;
import com.infinity.i_attendance.ui.setting.holiday_setting.Holiday;
import com.infinity.i_attendance.ui.setting.leave_type.LeaveType;
import com.infinity.i_attendance.ui.setting.office_time.OfficeTime;
import com.infinity.i_attendance.ui.setting.role.Role;

import java.util.List;

public class DataRepository {
    private static final String TAG = "DataRepository";
    public static DataRepository instance;

    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Attendance>> getAttendanceReportByDateRange(String api_key, String fromDate, String toDate) {
        // TODO: 8/4/2020 get Attendance from server.
        MutableLiveData<List<Attendance>> mutableLiveData = new MutableLiveData<>();
        ApiManager.getAttendanceReportByDateRange(api_key, fromDate, toDate, new ApiManager.OnApiResponse<Attendance>() {
            @Override
            public void onSucceed(ApiResponse<Attendance> data) {
                mutableLiveData.setValue(data.getResults());
            }

            @Override
            public void onFailed(String errorMsg) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;

    }

    public MutableLiveData<List<OfficeTime>> getOfficeTimeList(String api_key) {
        MutableLiveData<List<OfficeTime>> mutableLiveData = new MutableLiveData<>();

        ApiManager.getOfficeTime(api_key, new ApiManager.OnApiResponse<OfficeTime>() {
            @Override
            public void onSucceed(ApiResponse<OfficeTime> data) {
                mutableLiveData.setValue(data.getResults());
            }

            @Override
            public void onFailed(String errorMsg) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<List<Role>> getRoleList(String api_key) {
        final MutableLiveData<List<Role>> mutableLiveData = new MutableLiveData<>();
        ApiManager.getAllUserRole(api_key, new ApiManager.OnApiResponse<Role>() {
            @Override
            public void onSucceed(ApiResponse<Role> data) {
                mutableLiveData.setValue(data.getResults());
            }

            @Override
            public void onFailed(String errorMsg) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<User> getUserByKey(String api_key) {
        final MutableLiveData<User> mutableLiveData = new MutableLiveData<>();
        ApiManager.getUserByKey(api_key, new ApiManager.OnApiResponse<User>() {
            @Override
            public void onSucceed(ApiResponse<User> data) {
                mutableLiveData.setValue(data.getResults().get(0));
            }

            @Override
            public void onFailed(String errorMsg) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<UserInfo> getUserInfoByKey(String api_key) {
        final MutableLiveData<UserInfo> mutableLiveData = new MutableLiveData<>();

        ApiManager.getUserInfoByKey(api_key, new ApiManager.OnApiResponse<UserInfo>() {
            @Override
            public void onSucceed(ApiResponse<UserInfo> data) {
                mutableLiveData.setValue(data.getResults().get(0));
            }

            @Override
            public void onFailed(String errorMsg) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<List<User>> getAllUser(String api_key) {
        final MutableLiveData<List<User>> mutableLiveData = new MutableLiveData<>();
        ApiManager.getAllUser(api_key, new ApiManager.OnApiResponse<User>() {
            @Override
            public void onSucceed(ApiResponse<User> data) {
                mutableLiveData.setValue(data.getResults());
            }

            @Override
            public void onFailed(String errorMsg) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<List<User>> createUser(String api_key, User user) {
        final MutableLiveData<List<User>> mutableLiveData = new MutableLiveData<>();
        ApiManager.createUser(api_key, user, new ApiManager.OnApiResponse<User>() {
            @Override
            public void onSucceed(ApiResponse<User> data) {
                mutableLiveData.setValue(data.getResults());
            }

            @Override
            public void onFailed(String errorMsg) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<List<LeaveType>> getLeaveType(String api_key) {

        final MutableLiveData<List<LeaveType>> mutableLiveData = new MutableLiveData<>();
        ApiManager.getLeaveType(api_key, new ApiManager.OnApiResponse<LeaveType>() {
            @Override
            public void onSucceed(ApiResponse<LeaveType> data) {
                mutableLiveData.setValue(data.getResults());
            }

            @Override
            public void onFailed(String errorMsg) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<List<Leave>> getUserLeaveHistory(String api_key) {
        final MutableLiveData<List<Leave>> mutableLiveData = new MutableLiveData<>();

        ApiManager.getUserLeaveHistory(api_key, new ApiManager.OnApiResponse<Leave>() {
            @Override
            public void onSucceed(ApiResponse<Leave> data) {
                mutableLiveData.setValue(data.getResults());
            }

            @Override
            public void onFailed(String errorMsg) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<List<Leave>> getAllLeaveHistory(String api_key) {
        final MutableLiveData<List<Leave>> mutableLiveData = new MutableLiveData<>();

        ApiManager.getAllLeaveHistory(api_key, new ApiManager.OnApiResponse<Leave>() {
            @Override
            public void onSucceed(ApiResponse<Leave> data) {
                Log.d(TAG, "onSucceed: " + data.getResults());
                mutableLiveData.setValue(data.getResults());
            }

            @Override
            public void onFailed(String errorMsg) {
                Log.d(TAG, "onFailed: " + errorMsg);
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<List<Department>> getAllDepartment(String api_key) {
        final MutableLiveData<List<Department>> mutableLiveData = new MutableLiveData<>();

        ApiManager.getAllDepartment(api_key, new ApiManager.OnApiResponse<Department>() {
            @Override
            public void onSucceed(ApiResponse<Department> data) {
                mutableLiveData.setValue(data.getResults());
            }

            @Override
            public void onFailed(String errorMsg) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Designation>> getAllDesignation(String api_key, int dId) {
        final MutableLiveData<List<Designation>> mutableLiveData = new MutableLiveData<>();

        ApiManager.getDesignation(api_key, dId, new ApiManager.OnApiResponse<Designation>() {
            @Override
            public void onSucceed(ApiResponse<Designation> data) {
                mutableLiveData.setValue(data.getResults());
            }

            @Override
            public void onFailed(String errorMsg) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Holiday>> getHolidayList(String api_key) {

        final MutableLiveData<List<Holiday>> mutableLiveData = new MutableLiveData<>();

        ApiManager.getHolidayList(api_key, new ApiManager.OnApiResponse<Holiday>() {
            @Override
            public void onSucceed(ApiResponse<Holiday> data) {
                mutableLiveData.setValue(data.getResults());
            }

            @Override
            public void onFailed(String errorMsg) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;

    }

    public LiveData<ApiResponse> checkIsAnyOffday(String api_key) {

        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();

        ApiManager.checkIsAnyOffday(api_key, new ApiManager.OnApiResponse<Holiday>() {
            @Override
            public void onSucceed(ApiResponse data) {
                mutableLiveData.setValue(data);
            }

            @Override
            public void onFailed(String errorMsg) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;

    }
}
