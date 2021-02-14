package com.infinity.i_attendance.api;

import android.util.Log;

import com.infinity.i_attendance.api.http.ApiResponse;
import com.infinity.i_attendance.api.http.RetrofitClient;
import com.infinity.i_attendance.api.http.RetrofitInterface;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiManager {

    private static final String TAG = "ApiManager";

    public static void requestLogin(String uid, String password, final ApiManager.OnApiResponse<User> onApiResponse) {
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .requestLogin(uid, password)
                .enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "requestLogin: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else onApiResponse.onFailed(response.body().getMsg());
                        } else onApiResponse.onFailed(response.message());

                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void createOrUpdateUserRole(String api_key, Role role, final OnApiResponse onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .createOrUpdateUserRole(api_key, role)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void getAllUserRole(String api_key, final OnApiResponse<Role> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAllUserRole(api_key)
                .enqueue(new Callback<ApiResponse<Role>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Role>> call, Response<ApiResponse<Role>> response) {
                        if (response.isSuccessful() && !response.body().isError()) {
                            onApiResponse.onSucceed(response.body());
                        } else onApiResponse.onFailed(response.message());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Role>> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void getUserByKey(String api_key, final OnApiResponse<User> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getUserByKey(api_key)
                .enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "getUserByKey: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void getAllUser(String api_key, final OnApiResponse<User> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAllUser(api_key)
                .enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        if (response.isSuccessful() && !response.body().isError()) {
                            Log.d(TAG, "getAllUser: " + response.body().getResults());
                            onApiResponse.onSucceed(response.body());
                        } else onApiResponse.onFailed(response.message());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void createUser(String api_key, User user, final OnApiResponse<User> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .createUser(api_key, user)
                .enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "createUser: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else onApiResponse.onFailed(response.message());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void updateUser(String api_key, User updatedUser, final OnApiResponse onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateUser(api_key, updatedUser)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        Log.d(TAG, "updateUser: " + response.body());
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else onApiResponse.onFailed(response.message());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void addUserInfo(String api_key, UserInfo updatedUser, final OnApiResponse onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addUserInfo(api_key, updatedUser)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        Log.d(TAG, "updateUser: " + response.body());
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else onApiResponse.onFailed(response.message());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void getUserInfoByKey(String api_key, final OnApiResponse<UserInfo> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getUserInfoByKey(api_key)
                .enqueue(new Callback<ApiResponse<UserInfo>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<UserInfo>> call, Response<ApiResponse<UserInfo>> response) {
                        Log.d(TAG, "updateUser: " + response.body());
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else onApiResponse.onFailed(response.message());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<UserInfo>> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void resetPassword(String apiKey, String newPassword, final OnApiResponse onApiResponse) {
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .resetPassword(apiKey, newPassword)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            onApiResponse.onSucceed(response.body());
                        } else {
                            onApiResponse.onFailed(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void getLeaveType(String api_key, final OnApiResponse<LeaveType> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getLeaveType(api_key)
                .enqueue(new Callback<ApiResponse<LeaveType>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<LeaveType>> call, Response<ApiResponse<LeaveType>> response) {
                        Log.d(TAG, "getLeaveType: " + response);
                        if (response.body().isError()) {
                            onApiResponse.onFailed(response.body().getMsg());
                        } else {
                            onApiResponse.onSucceed(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<LeaveType>> call, Throwable t) {
                        Log.d(TAG, "getLeaveType: " + t.getMessage());
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    //--------------- Leave ---------------

    public static void actionLeaveType(String api_key, LeaveType leaveType, int actionCode, final OnApiResponse onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .actionLeaveType(api_key, leaveType, actionCode)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "createLeaveType: " + response);
                            if (response.body().isError()) {
                                onApiResponse.onFailed(response.body().getMsg());
                            } else {
                                onApiResponse.onSucceed(response.body());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }

                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "createLeaveType: " + t.getMessage());
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void applyLeave(String api_key, Leave leave, final OnApiResponse onApiResponse) {
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .applyLeave(api_key, leave)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void getUserLeaveHistory(String api_key, final OnApiResponse<Leave> onApiResponse) {
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getUserLeaveHistory(api_key)
                .enqueue(new Callback<ApiResponse<Leave>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Leave>> call, Response<ApiResponse<Leave>> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "getLeaveHistory: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Leave>> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void getAllLeaveHistory(String api_key, final OnApiResponse<Leave> onApiResponse) {
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAllLeaveHistory(api_key)
                .enqueue(new Callback<ApiResponse<Leave>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Leave>> call, Response<ApiResponse<Leave>> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "getAllLeaveHistory: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Leave>> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void changeLeaveStatus(String api_key, int id, int status, final OnApiResponse<Leave> onApiResponse) {
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .changeLeaveStatus(api_key, id, status)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "changeLeaveStatus: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void getAllDepartment(String api_key, final OnApiResponse<Department> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAllDepartment(api_key)
                .enqueue(new Callback<ApiResponse<Department>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Department>> call, Response<ApiResponse<Department>> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "getAllDepartment: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Department>> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    //----------------Department----------------

    public static void createDepartment(String api_key, String department_name, final OnApiResponse onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .createDepartment(api_key, department_name)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "createDepartment: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void updateDepartment(String apiKey, int id, String name, final OnApiResponse onApiResponse) {
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateDepartment(apiKey, id, name)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "updateDepartment: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void deleteDepartment(String apiKey, int id, final OnApiResponse onApiResponse) {
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .deleteDepartment(apiKey, id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "deleteDepartment: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void getDesignation(String api_key, int dId, OnApiResponse<Designation> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAllDesignation(api_key, dId)
                .enqueue(new Callback<ApiResponse<Designation>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Designation>> call, Response<ApiResponse<Designation>> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "getAllDesignation: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Designation>> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }


    //----------------------Designation-------------------

    public static void createDesignation(String api_key, int dpt_id, String dg_name, OnApiResponse onApiResponse) {
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .createDesignation(api_key, dpt_id, dg_name)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "createDesignation: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void updateDesignation(String apiKey, int id, String name, final OnApiResponse onApiResponse) {
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateDesignation(apiKey, id, name)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "updateDesignation: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void deleteDesignation(String apiKey, int id, final OnApiResponse onApiResponse) {
        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .deleteDesignation(apiKey, id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "deleteDepartment: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    public static void addAttendance(String apiKey, String uid, String date, String time, String location,
                                     int statusCode, final OnApiResponse<Attendance> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addAttendance(apiKey, uid, date, time, location, statusCode)
                .enqueue(new Callback<ApiResponse<Attendance>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Attendance>> call, Response<ApiResponse<Attendance>> response) {

                        if (response.isSuccessful()) {

                            Log.d(TAG, "addAttendance: " + response.body());

                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Attendance>> call, Throwable t) {
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }


    //-------------- attendance---------------

    public static void getAttendanceReportByDateRange(String apiKey, String fromDate, String toDate, final OnApiResponse<Attendance> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getAttendanceReportByDateRange(apiKey, fromDate, toDate)
                .enqueue(new Callback<ApiResponse<Attendance>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Attendance>> call, Response<ApiResponse<Attendance>> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "getAttendanceReportByDate: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Attendance>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        onApiResponse.onFailed(t.getMessage());
                    }
                });
    }

    // --------------holiday-------------------
    public static void getHolidayList(String api_key, OnApiResponse<Holiday> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getHolidayList(api_key)
                .enqueue(new Callback<ApiResponse<Holiday>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Holiday>> call, Response<ApiResponse<Holiday>> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "getHolidayList: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Holiday>> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                        onApiResponse.onFailed(t.getMessage());
                    }
                });

    }

    public static void addHoliday(String api_key, String name, String date, int status, OnApiResponse<Holiday> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .addHoliday(api_key, name, date, status)
                .enqueue(new Callback<ApiResponse<Holiday>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Holiday>> call, Response<ApiResponse<Holiday>> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "addOrUpdateOrDeleteHoliday: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Holiday>> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                        onApiResponse.onFailed(t.getMessage());
                    }
                });

    }

    public static void updateHoliday(String api_key, int id, String name, String date, int status, OnApiResponse<Holiday> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateHoliday(api_key, id, name, date, status)
                .enqueue(new Callback<ApiResponse<Holiday>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Holiday>> call, Response<ApiResponse<Holiday>> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "addOrUpdateOrDeleteHoliday: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Holiday>> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                        onApiResponse.onFailed(t.getMessage());
                    }
                });

    }

    public static void deleteHoliday(String api_key, int id, int status, OnApiResponse<Holiday> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .deleteHoliday(api_key, id, status)
                .enqueue(new Callback<ApiResponse<Holiday>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Holiday>> call, Response<ApiResponse<Holiday>> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "addOrUpdateOrDeleteHoliday: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Holiday>> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                        onApiResponse.onFailed(t.getMessage());
                    }
                });

    }

    public static void checkIsAnyOffday(String api_key, OnApiResponse onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .checkIsAnyOffday(api_key)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "checkIsAnyOffday: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                        onApiResponse.onFailed(t.getMessage());
                    }
                });

    }

    public static void getOfficeTime(String api_key, OnApiResponse<OfficeTime> onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .getOfficeTime(api_key)
                .enqueue(new Callback<ApiResponse<OfficeTime>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<OfficeTime>> call, Response<ApiResponse<OfficeTime>> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "getOfficeTime: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<OfficeTime>> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                        onApiResponse.onFailed(t.getMessage());
                    }
                });

    }


    //----------- OfficeTime ------------

    public static void updateOfficeTime(String api_key, OfficeTime officeTime, OnApiResponse onApiResponse) {

        RetrofitClient.getRetrofitInstance().create(RetrofitInterface.class)
                .updateOfficeTime(api_key, officeTime)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "getOfficeTime: " + response.body());
                            if (!response.body().isError()) {
                                onApiResponse.onSucceed(response.body());
                            } else {
                                onApiResponse.onFailed(response.body().getMsg());
                            }
                        } else {
                            onApiResponse.onFailed(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                        onApiResponse.onFailed(t.getMessage());
                    }
                });

    }

    public interface OnApiResponse<T> {
        void onSucceed(ApiResponse<T> data);

        void onFailed(String errorMsg);
    }

}
