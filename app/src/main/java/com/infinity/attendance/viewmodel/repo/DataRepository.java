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

    public LiveData<ApiResponse<Attendance>> getAttendanceReportByDateRange(String uid, String fromDate, String toDate) {
        MutableLiveData<ApiResponse<Attendance>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAttendanceReportByDateRange(uid, fromDate, toDate)
                .enqueue(new Callback<ApiResponse<Attendance>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Attendance>> call, Response<ApiResponse<Attendance>> response) {
                        Log.d(TAG, "onResponse: " + response.body());
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Attendance>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;

    }

    public LiveData<ApiResponse<Role>> addRole(Role role) {
        final MutableLiveData<ApiResponse<Role>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addRole(role)
                .enqueue(new Callback<ApiResponse<Role>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Role>> call, Response<ApiResponse<Role>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Role>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<Role>> getRole() {
        final MutableLiveData<ApiResponse<Role>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getRole()
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

    //========== user ================

    public LiveData<ApiResponse<User>> requestLogin(String uid, String password) {
        final MutableLiveData<ApiResponse<User>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .requestLogin(uid, password)
                .enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<User>> addUser(User user) {
        final MutableLiveData<ApiResponse<User>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addUser(user)
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

    public LiveData<ApiResponse<User>> updateUser(User updatedUser) {
        final MutableLiveData<ApiResponse<User>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateUser(updatedUser)
                .enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<UserInfo>> addUserInfo(String uid, UserInfo updatedUser) {
        final MutableLiveData<ApiResponse<UserInfo>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addUserInfo(uid, updatedUser)
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

    public LiveData<ApiResponse<User>> resetPassword(String uid, String newPassword) {
        final MutableLiveData<ApiResponse<User>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .resetPassword(uid, newPassword)
                .enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        mutableLiveData.setValue(null);
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<User>> getUser(String uid) {
        final MutableLiveData<ApiResponse<User>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getUser(uid)
                .enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<UserInfo>> getUserInfo(String uid) {
        final MutableLiveData<ApiResponse<UserInfo>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getUserInfo(uid)
                .enqueue(new Callback<ApiResponse<UserInfo>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<UserInfo>> call, Response<ApiResponse<UserInfo>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<UserInfo>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Leave>> getUserLeaveHistory(String uid) {
        final MutableLiveData<ApiResponse<Leave>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getLeaveHistory(uid)
                .enqueue(new Callback<ApiResponse<Leave>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Leave>> call, Response<ApiResponse<Leave>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.body() != null) {
                            mutableLiveData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Leave>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<Attendance>> addAttendance(String uid, String date, String time, String location, int statusCode) {
        final MutableLiveData<ApiResponse<Attendance>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addAttendance(uid, date, time, location, statusCode)
                .enqueue(new Callback<ApiResponse<Attendance>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Attendance>> call, Response<ApiResponse<Attendance>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.body() != null) {
                            mutableLiveData.setValue(response.body());
                        } else {
                            mutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Attendance>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);

                    }
                });
        return mutableLiveData;
    }

    //--------------- Leave ---------------

    public LiveData<ApiResponse<LeaveType>> getLeaveType() {

        final MutableLiveData<ApiResponse<LeaveType>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getLeaveType()
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

    public LiveData<ApiResponse<LeaveType>> addLeaveType(LeaveType newLeaveType) {
        final MutableLiveData<ApiResponse<LeaveType>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addLeaveType(newLeaveType)
                .enqueue(new Callback<ApiResponse<LeaveType>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<LeaveType>> call, Response<ApiResponse<LeaveType>> response) {
                        if (response.body() != null) {
                            mutableLiveData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<LeaveType>> call, Throwable t) {
                        Log.d(TAG, "getLeaveType: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<LeaveType>> updateLeaveType(LeaveType newLeaveType) {
        final MutableLiveData<ApiResponse<LeaveType>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateLeaveType(newLeaveType)
                .enqueue(new Callback<ApiResponse<LeaveType>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<LeaveType>> call, Response<ApiResponse<LeaveType>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.body() != null) {
                            mutableLiveData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<LeaveType>> call, Throwable t) {
                        Log.d(TAG, "updateLeaveType: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<LeaveType>> deleteLeave(int id) {
        final MutableLiveData<ApiResponse<LeaveType>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .deleteLeaveType(id)
                .enqueue(new Callback<ApiResponse<LeaveType>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<LeaveType>> call, Response<ApiResponse<LeaveType>> response) {
                        if (response.body() != null) {
                            mutableLiveData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<LeaveType>> call, Throwable t) {
                        Log.d(TAG, "getLeaveType: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Leave>> applyLeave(Leave leave) {
        final MutableLiveData<ApiResponse<Leave>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .applyLeave(leave)
                .enqueue(new Callback<ApiResponse<Leave>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Leave>> call, Response<ApiResponse<Leave>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Leave>> call, Throwable t) {
                        mutableLiveData.setValue(null);
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<Leave>> changeLeaveStatus(String uid, Leave leave) {
        final MutableLiveData<ApiResponse<Leave>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .changeLeaveStatus(uid, leave)
                .enqueue(new Callback<ApiResponse<Leave>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Leave>> call, Response<ApiResponse<Leave>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Leave>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });
        return mutableLiveData;
    }

    //=============Department===============

    public LiveData<ApiResponse<Department>> getAllDepartment() {
        final MutableLiveData<ApiResponse<Department>> mutableLiveData = new MutableLiveData<>();

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAllDepartment()
                .enqueue(new Callback<ApiResponse<Department>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Department>> call, Response<ApiResponse<Department>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Department>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Department>> addDepartment(Department department) {
        final MutableLiveData<ApiResponse<Department>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addDepartment(department)
                .enqueue(new Callback<ApiResponse<Department>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Department>> call, Response<ApiResponse<Department>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.body() != null) {
                            Log.d(TAG, "onResponse: " + response.body());
                            mutableLiveData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Department>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Department>> updateDepartment(Department department) {
        final MutableLiveData<ApiResponse<Department>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateDepartment(department)
                .enqueue(new Callback<ApiResponse<Department>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Department>> call, Response<ApiResponse<Department>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Department>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Department>> deleteDepartment(int id) {
        final MutableLiveData<ApiResponse<Department>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .deleteDepartment(id)
                .enqueue(new Callback<ApiResponse<Department>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Department>> call, Response<ApiResponse<Department>> response) {
                        mutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Department>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });

        return mutableLiveData;
    }


    // =============Designation==============

    public LiveData<ApiResponse<Designation>> getDesignationByDpt(int dpt_id) {
        final MutableLiveData<ApiResponse<Designation>> mutableLiveData = new MutableLiveData<>();

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAllDesignation(dpt_id)
                .enqueue(new Callback<ApiResponse<Designation>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Designation>> call, Response<ApiResponse<Designation>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.body() != null) {
                            mutableLiveData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Designation>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Designation>> addDesignation(Designation designation) {
        final MutableLiveData<ApiResponse<Designation>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addDesignation(designation)
                .enqueue(new Callback<ApiResponse<Designation>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Designation>> call, Response<ApiResponse<Designation>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.body() != null) {
                            Log.d(TAG, "onResponse: " + response.body());
                            mutableLiveData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Designation>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<Designation>> updateDesignation(Designation designation) {
        final MutableLiveData<ApiResponse<Designation>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateDesignation(designation)
                .enqueue(new Callback<ApiResponse<Designation>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Designation>> call, Response<ApiResponse<Designation>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.body() != null) {
                            mutableLiveData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Designation>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<Designation>> deleteDesignation(int designationId, int dpt_id) {
        final MutableLiveData<ApiResponse<Designation>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .deleteDesignation(designationId, dpt_id)
                .enqueue(new Callback<ApiResponse<Designation>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Designation>> call, Response<ApiResponse<Designation>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.body() != null) {
                            mutableLiveData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Designation>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });
        return mutableLiveData;
    }

    // --------------holiday-------------------

    public LiveData<ApiResponse<Holiday>> getHolidayList() {
        final MutableLiveData<ApiResponse<Holiday>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getHolidayList()
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

    public LiveData<ApiResponse<Holiday>> addHoliday(Holiday holiday) {
        final MutableLiveData<ApiResponse<Holiday>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addHoliday(holiday)
                .enqueue(new Callback<ApiResponse<Holiday>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Holiday>> call, Response<ApiResponse<Holiday>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.body() != null) {
                            mutableLiveData.setValue(response.body());
                        } else {
                            mutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Holiday>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Holiday>> updateHoliday(Holiday holiday) {
        final MutableLiveData<ApiResponse<Holiday>> liveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateHoliday(holiday)
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

    public LiveData<ApiResponse<Holiday>> deleteHoliday(int id) {
        final MutableLiveData<ApiResponse<Holiday>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .deleteHoliday(id)
                .enqueue(new Callback<ApiResponse<Holiday>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Holiday>> call, Response<ApiResponse<Holiday>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.body() != null) {
                            mutableLiveData.setValue(response.body());
                        } else {
                            mutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Holiday>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
        return mutableLiveData;

    }

    public LiveData<ApiResponse> checkIsAnyOffday() {

        final MutableLiveData<ApiResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .checkIsAnyOffday()
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

    public LiveData<ApiResponse<OfficeTime>> getOfficeTime() {
        MutableLiveData<ApiResponse<OfficeTime>> mutableLiveData = new MutableLiveData<>();

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getOfficeTime()
                .enqueue(new Callback<ApiResponse<OfficeTime>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<OfficeTime>> call, Response<ApiResponse<OfficeTime>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.body() != null) {
                            mutableLiveData.setValue(response.body());
                        } else {
                            mutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<OfficeTime>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<OfficeTime>> updateOfficeTime(OfficeTime officeTime) {
        final MutableLiveData<ApiResponse<OfficeTime>> mutableLiveData = new MutableLiveData<>();
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateOfficeTime(officeTime)
                .enqueue(new Callback<ApiResponse<OfficeTime>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<OfficeTime>> call, Response<ApiResponse<OfficeTime>> response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.body() != null) {
                            mutableLiveData.setValue(response.body());
                        } else {
                            mutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<OfficeTime>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mutableLiveData.setValue(null);
                    }
                });

        return mutableLiveData;
    }

}
