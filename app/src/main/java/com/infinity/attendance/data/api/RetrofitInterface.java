package com.infinity.attendance.data.api;


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
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @POST("login")
    Call<ApiResponse<User>> requestLogin(
            @Query("uid") String uid,
            @Query("password") String password
    );


    @POST("createOrUpdateUserRole")
    Call<ApiResponse> createOrUpdateUserRole(
            @Header("api_key") String api_key,
            @Body Role role
    );

    @POST("getAllUserRole")
    Call<ApiResponse<Role>> getAllUserRole(
            @Header("api_key") String api_key
    );

    //-------------- user ----------------

    @POST("getUserByKey")
    Call<ApiResponse<User>> getUserByKey(
            @Header("api_key") String api_key
    );

    @POST("getAllUser")
    Call<ApiResponse<User>> getAllUser(
            @Header("api_key") String api_key
    );

    @POST("createUser")
    Call<ApiResponse<User>> createUser(
            @Header("api_key") String api_key,
            @Body User user
    );

    @POST("updateUser")
    Call<ApiResponse> updateUser(
            @Header("api_key") String api_key,
            @Body User updatedUser
    );

    @POST("addUserInfo")
    Call<ApiResponse> addUserInfo(
            @Header("api_key") String api_key,
            @Body UserInfo userInfo
    );

    @POST("getUserInfoByKey")
    Call<ApiResponse<UserInfo>> getUserInfoByKey(
            @Header("api_key") String api_key
    );

    @POST("resetPassword")
    Call<ApiResponse> resetPassword(
            @Header("api_key") String api_key,
            @Query("new_password") String new_password
    );

    //----------------------------

    @POST("getLeaveType")
    Call<ApiResponse<LeaveType>> getLeaveType(
            @Header("api_key") String api_key
    );

    @POST("actionLeaveType")
    Call<ApiResponse> actionLeaveType(
            @Header("api_key") String api_key,
            @Body LeaveType leaveType,
            @Query("action_code") int action_code
    );

    @POST("getUserLeaveHistory")
    Call<ApiResponse<Leave>> getUserLeaveHistory(
            @Header("api_key") String api_key
    );

    @POST("getAllLeaveHistory")
    Call<ApiResponse<Leave>> getAllLeaveHistory(
            @Header("api_key") String api_key
    );

    @POST("applyLeave")
    Call<ApiResponse> applyLeave(
            @Header("api_key") String api_key,
            @Body Leave leave

    );

    @POST("changeLeaveStatus")
    Call<ApiResponse> changeLeaveStatus(
            @Header("api_key") String api_key,
            @Query("id") int id,
            @Query("status") int status

    );

    //---------------Department------------------

    @POST("getAllDepartment")
    Call<ApiResponse<Department>> getAllDepartment(
            @Header("api_key") String api_key
    );

    @POST("createDepartment")
    Call<ApiResponse> createDepartment(
            @Header("api_key") String api_key,
            @Query("name") String department_name
    );

    @POST("updateDepartment")
    Call<ApiResponse> updateDepartment(
            @Header("api_key") String api_key,
            @Query("id") int id,
            @Query("name") String name
    );

    @POST("deleteDepartment")
    Call<ApiResponse> deleteDepartment(
            @Header("api_key") String api_key,
            @Query("id") int id
    );

    //---------------Designation------------------

    @POST("getDesignation")
    Call<ApiResponse<Designation>> getAllDesignation(
            @Header("api_key") String api_key,
            @Query("dpt_id") int dId);

    @POST("createDesignation")
    Call<ApiResponse> createDesignation(
            @Header("api_key") String api_key,
            @Query("dpt_id") int dpt_id,
            @Query("name") String department_name
    );

    @POST("updateDesignation")
    Call<ApiResponse> updateDesignation(
            @Header("api_key") String api_key,
            @Query("id") int id,
            @Query("name") String name
    );

    @POST("deleteDesignation")
    Call<ApiResponse> deleteDesignation(
            @Header("api_key") String api_key,
            @Query("id") int id
    );

    //----------------Attendance----------------

    @POST("addAttendance")
    Call<ApiResponse<Attendance>> addAttendance(
            @Header("api_key") String api_key,
            @Query("uid") String uid,
            @Query("date") String date,
            @Query("time") String time,
            @Query("location") String location,
            @Query("status") int statusCode
    );

    @POST("getAttendanceReportByDateRange")
    Call<ApiResponse<Attendance>> getAttendanceReportByDateRange(
            @Header("api_key") String api_key,
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate
    );

    //----------------holiday----------------

    @POST("getAllHoliday")
    Call<ApiResponse<Holiday>> getHolidayList(
            @Header("api_key") String api_key
    );

    @POST("addOrUpdateOrDeleteHoliday")
    Call<ApiResponse<Holiday>> addHoliday(
            @Header("api_key") String api_key,
            @Query("name") String name,
            @Query("date") String date,
            @Query("status") int status
    );

    @POST("addOrUpdateOrDeleteHoliday")
    Call<ApiResponse<Holiday>> updateHoliday(
            @Header("api_key") String api_key,
            @Query("id") int id,
            @Query("name") String uid,
            @Query("date") String date,
            @Query("status") int status
    );

    @POST("addOrUpdateOrDeleteHoliday")
    Call<ApiResponse<Holiday>> deleteHoliday(
            @Header("api_key") String api_key,
            @Query("id") int id,
            @Query("status") int status
    );

    @POST("checkIsAnyOffday")
    Call<ApiResponse> checkIsAnyOffday(
            @Header("api_key") String api_key
    );

    //------------- OfficeTime -------------

    @GET("getOfficeTime")
    Call<ApiResponse<OfficeTime>> getOfficeTime(
            @Header("api_key") String api_key
    );

    @POST("updateOfficeTime")
    Call<ApiResponse> updateOfficeTime(
            @Header("api_key") String api_key,
            @Body OfficeTime officeTime
    );
}