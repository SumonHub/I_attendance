package com.infinity.attendance.viewmodel.repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.infinity.attendance.data.api.RetrofitClient;
import com.infinity.attendance.data.api.RetrofitInterface;
import com.infinity.attendance.data.model.Attendance;
import com.infinity.attendance.data.model.Department;
import com.infinity.attendance.data.model.Designation;
import com.infinity.attendance.data.model.Holiday;
import com.infinity.attendance.data.model.Leave;
import com.infinity.attendance.data.model.LeaveType;
import com.infinity.attendance.data.model.OfficeTime;
import com.infinity.attendance.data.model.Role;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.data.model.UserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {
    private static final String TAG = "DataRepository";
    public static DataRepository instance;

    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    public LiveData<ApiResponse<Attendance>> getAttendanceReportByDateRange(String api_key, String fromDate, String toDate) {
        MutableLiveData<ApiResponse<Attendance>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAttendanceReportByDateRange(api_key, fromDate, toDate)
                .enqueue(new Callback<ApiResponse<Attendance>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Attendance>> call, Response<ApiResponse<Attendance>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Attendance>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;

    }

    //========== user ================

    public LiveData<ApiResponse<User>> requestLogin(String uid, String password) {
        final MutableLiveData<ApiResponse<User>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .requestLogin(uid, password)
                .enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        Log.d(TAG, "onResponse: " + response.body());
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse> createOrUpdateUserRole(String api_key, Role role) {
        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .createOrUpdateUserRole(api_key, role)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<Role>> getAllUserRole(String api_key) {
        final MutableLiveData<ApiResponse<Role>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAllUserRole(api_key)
                .enqueue(new Callback<ApiResponse<Role>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Role>> call, Response<ApiResponse<Role>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Role>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<User>> createUser(String api_key, User user) {
        final MutableLiveData<ApiResponse<User>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .createUser(api_key, user)
                .enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse> updateUser(String api_key, User updatedUser) {
        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateUser(api_key, updatedUser)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse> addUserInfo(String api_key, UserInfo updatedUser) {
        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addUserInfo(api_key, updatedUser)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse> resetPassword(String apiKey, String newPassword) {
        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .resetPassword(apiKey, newPassword)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<User>> getUserByKey(String api_key) {
        final MutableLiveData<ApiResponse<User>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getUserByKey(api_key)
                .enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<UserInfo>> getUserInfoByKey(String api_key) {
        final MutableLiveData<ApiResponse<UserInfo>> mutableLiveData = new MutableLiveData<>();

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getUserInfoByKey(api_key)
                .enqueue(new Callback<ApiResponse<UserInfo>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<UserInfo>> call, Response<ApiResponse<UserInfo>> response) {
                        mutableLiveData.setValue(response.body());

                    }

                    @Override
                    public void onFailure(Call<ApiResponse<UserInfo>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<User>> getAllUser(String api_key) {
        final MutableLiveData<ApiResponse<User>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAllUser(api_key)
                .enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<Leave>> getUserLeaveHistory(String api_key) {
        final MutableLiveData<ApiResponse<Leave>> mutableLiveData = new MutableLiveData<>();

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getUserLeaveHistory(api_key)
                .enqueue(new Callback<ApiResponse<Leave>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Leave>> call, Response<ApiResponse<Leave>> response) {
                        mutableLiveData.setValue(response.body());

                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Leave>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Attendance>> addAttendance(String apiKey, String uid, String date, String time, String location, int statusCode) {
        final MutableLiveData<ApiResponse<Attendance>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addAttendance(apiKey, uid, date, time, location, statusCode)
                .enqueue(new Callback<ApiResponse<Attendance>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Attendance>> call, Response<ApiResponse<Attendance>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Attendance>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());

                    }
                });
        return mutableLiveData;
    }

    //===== setting ==============

    public LiveData<ApiResponse<OfficeTime>> getOfficeTimeList(String api_key) {
        MutableLiveData<ApiResponse<OfficeTime>> mutableLiveData = new MutableLiveData<>();

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getOfficeTime(api_key)
                .enqueue(new Callback<ApiResponse<OfficeTime>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<OfficeTime>> call, Response<ApiResponse<OfficeTime>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<OfficeTime>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Role>> getRoleList(String api_key) {
        final MutableLiveData<ApiResponse<Role>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAllUserRole(api_key)
                .enqueue(new Callback<ApiResponse<Role>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Role>> call, Response<ApiResponse<Role>> response) {

                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Role>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<LeaveType>> getLeaveType(String api_key) {

        final MutableLiveData<ApiResponse<LeaveType>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getLeaveType(api_key)
                .enqueue(new Callback<ApiResponse<LeaveType>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<LeaveType>> call, Response<ApiResponse<LeaveType>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<LeaveType>> call, Throwable t) {
                        Log.d(TAG, "getLeaveType: " + t.getMessage());
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Leave>> getAllLeaveHistory(String api_key) {
        final MutableLiveData<ApiResponse<Leave>> mutableLiveData = new MutableLiveData<>();

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAllLeaveHistory(api_key)
                .enqueue(new Callback<ApiResponse<Leave>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Leave>> call, Response<ApiResponse<Leave>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Leave>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Department>> getAllDepartment(String api_key) {
        final MutableLiveData<ApiResponse<Department>> mutableLiveData = new MutableLiveData<>();

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAllDepartment(api_key)
                .enqueue(new Callback<ApiResponse<Department>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Department>> call, Response<ApiResponse<Department>> response) {

                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Department>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Designation>> getAllDesignation(String api_key, int dId) {
        final MutableLiveData<ApiResponse<Designation>> mutableLiveData = new MutableLiveData<>();

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAllDesignation(api_key, dId)
                .enqueue(new Callback<ApiResponse<Designation>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Designation>> call, Response<ApiResponse<Designation>> response) {

                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Designation>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Holiday>> getHolidayList(String api_key) {
        final MutableLiveData<ApiResponse<Holiday>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getHolidayList(api_key)
                .enqueue(new Callback<ApiResponse<Holiday>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Holiday>> call, Response<ApiResponse<Holiday>> response) {

                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Holiday>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;

    }

    //--------------- Leave ---------------

    public LiveData<ApiResponse> actionLeaveType(String api_key, LeaveType leaveType, int actionCode) {
        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .actionLeaveType(api_key, leaveType, actionCode)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "createLeaveType: " + t.getMessage());
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse> applyLeave(String api_key, Leave leave) {
        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .applyLeave(api_key, leave)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<Leave>> changeLeaveStatus(String api_key, int id, int status) {
        final MutableLiveData<ApiResponse<Leave>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .changeLeaveStatus(api_key, id, status)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    //============================

    public LiveData<ApiResponse> createDepartment(String api_key, String department_name) {
        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .createDepartment(api_key, department_name)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse> updateDepartment(String api_key, int id, String name) {
        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateDepartment(api_key, id, name)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse> deleteDepartment(String api_key, int id) {
        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .deleteDepartment(api_key, id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;
    }

    // ===========================

    public LiveData<ApiResponse> createDesignation(String api_key, int dpt_id, String dg_name) {
        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .createDesignation(api_key, dpt_id, dg_name)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse> updateDesignation(String apiKey, int id, String name) {
        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateDesignation(apiKey, id, name)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse> deleteDesignation(String apiKey, int id) {
        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .deleteDesignation(apiKey, id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    // --------------holiday-------------------

    public LiveData<ApiResponse<Holiday>> addHoliday(String api_key, String name, String date, int status) {
        final MutableLiveData<ApiResponse<Holiday>> liveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addHoliday(api_key, name, date, status)
                .enqueue(new Callback<ApiResponse<Holiday>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Holiday>> call, Response<ApiResponse<Holiday>> response) {
                        liveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Holiday>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return liveData;

    }

    public LiveData<ApiResponse<Holiday>> updateHoliday(String api_key, int id, String name, String date, int status) {
        final MutableLiveData<ApiResponse<Holiday>> liveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateHoliday(api_key, id, name, date, status)
                .enqueue(new Callback<ApiResponse<Holiday>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Holiday>> call, Response<ApiResponse<Holiday>> response) {
                        liveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Holiday>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return liveData;
    }

    public LiveData<ApiResponse<Holiday>> deleteHoliday(String api_key, int id, int status) {
        final MutableLiveData<ApiResponse<Holiday>> liveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .deleteHoliday(api_key, id, status)
                .enqueue(new Callback<ApiResponse<Holiday>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Holiday>> call, Response<ApiResponse<Holiday>> response) {
                        liveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Holiday>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return liveData;

    }

    public LiveData<ApiResponse> checkIsAnyOffday(String api_key) {

        final MutableLiveData<ApiResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .checkIsAnyOffday(api_key)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        liveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return liveData;

    }

    //----------- OfficeTime ------------

    public LiveData<ApiResponse> updateOfficeTime(String api_key, OfficeTime officeTime) {
        final MutableLiveData<ApiResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateOfficeTime(api_key, officeTime)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        liveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return liveData;
    }

}
