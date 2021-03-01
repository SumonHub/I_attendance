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
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {

    //-------------- user ----------------

    @GET("login")
    Call<ApiResponse<User>> requestLogin(
            @Query("uid") String uid,
            @Query("password") String password
    );

    @GET("createUser")
    Call<ApiResponse<User>> addUser(
            @Body User user
    );

    @GET("updateUser")
    Call<ApiResponse<User>> updateUser(
            @Body User updatedUser
    );

    @GET("addUserInfo/{uid}")
    Call<ApiResponse<UserInfo>> addUserInfo(
            @Path("uid") String uid,
            @Body UserInfo userInfo
    );

    @GET("getUserInfo/{uid}")
    Call<ApiResponse<UserInfo>> getUserInfo(
            @Path("uid") String uid
    );

    @GET("getUser/{id}")
    Call<ApiResponse<User>> getUser(
            @Path("id") String uid
    );

    @GET("resetPassword/{uid}")
    Call<ApiResponse<User>> resetPassword(
            @Path("uid") String uid,
            @Query("new_password") String new_password
    );

    //---------------- role ---------------

    @GET("getRole")
    Call<ApiResponse<Role>> getRole();

    @POST("addRole")
    Call<ApiResponse<Role>> addRole(
            @Body Role role
    );

    @POST("updateRole")
    Call<ApiResponse<Role>> updateRole(
            @Body Role role
    );

    @GET("deleteRole/{id}")
    Call<ApiResponse<Role>> deleteRole(
            @Path("id") String id
    );

    @POST("changeUserRole/{uid}")
    Call<ApiResponse<Role>> changeUserRole(
            @Path("uid") String uid,
            @Body Role role
    );

    //-------------- Leave --------------

    @GET("getLeaveType")
    Call<ApiResponse<LeaveType>> getLeaveType();

    @POST("addLeaveType")
    Call<ApiResponse<LeaveType>> addLeaveType(
            @Body LeaveType leaveType
    );

    @POST("updateLeaveType")
    Call<ApiResponse<LeaveType>> updateLeaveType(
            @Body LeaveType leaveType
    );

    @DELETE("deleteLeaveType/{id}")
    Call<ApiResponse<LeaveType>> deleteLeaveType(
            @Path("id") int id
    );

    @GET("getLeaveHistory/{uid}")
    Call<ApiResponse<Leave>> getLeaveHistory(
            @Path("uid") String uid
    );

    @POST("applyLeave")
    Call<ApiResponse<Leave>> applyLeave(
            @Body Leave leave
    );

    @POST("changeLeaveStatus/{uid}")
    Call<ApiResponse<Leave>> changeLeaveStatus(
            @Path("id") String uid,
            @Body Leave leave
    );

    //---------------Department------------------

    @GET("getAllDepartment")
    Call<ApiResponse<Department>> getAllDepartment();

    @POST("addDepartment")
    Call<ApiResponse<Department>> addDepartment(
            @Body Department department
    );

    @POST("updateDepartment")
    Call<ApiResponse<Department>> updateDepartment(
            @Body Department department
    );

    @DELETE("deleteDepartment/{dpt_id}")
    Call<ApiResponse<Department>> deleteDepartment(
            @Path("dpt_id") int dpt_id
    );

    //---------------Designation------------------

    @GET("getDesignation/{dpt_id}")
    Call<ApiResponse<Designation>> getAllDesignation(
            @Path("dpt_id") int dpt_id);

    @POST("addDesignation")
    Call<ApiResponse<Designation>> addDesignation(
            @Body Designation designation
    );

    @POST("updateDesignation")
    Call<ApiResponse<Designation>> updateDesignation(
            @Body Designation designation
    );

    @DELETE("deleteDesignation/{dg_id}/{dpt_id}")
    Call<ApiResponse<Designation>> deleteDesignation(
            @Path("dg_id") int dg_id,
            @Path("dpt_id") int dpt_id
    );

    //----------------Attendance----------------

    @GET("addAttendance")
    Call<ApiResponse<Attendance>> addAttendance(
            @Query("uid") String uid,
            @Query("date") String date,
            @Query("time") String time,
            @Query("location") String location,
            @Query("status") int statusCode
    );

    @GET("getAttendanceReportByDateRange/{uid}")
    Call<ApiResponse<Attendance>> getAttendanceReportByDateRange(
            @Path("uid") String uid,
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate
    );

    //----------------holiday----------------

    @GET("getAllHoliday")
    Call<ApiResponse<Holiday>> getHolidayList();

    @POST("addHoliday")
    Call<ApiResponse<Holiday>> addHoliday(
            @Body Holiday holiday
    );

    @POST("updateHoliday")
    Call<ApiResponse<Holiday>> updateHoliday(
            @Body Holiday holiday
    );

    @DELETE("deleteHoliday/{id}")
    Call<ApiResponse<Holiday>> deleteHoliday(
            @Path("id") int id
    );

    @GET("checkIsAnyOffday")
    Call<ApiResponse> checkIsAnyOffday();

    //------------- OfficeTime -------------

    @GET("getOfficeTime")
    Call<ApiResponse<OfficeTime>> getOfficeTime();

    @POST("updateOfficeTime")
    Call<ApiResponse<OfficeTime>> updateOfficeTime(
            @Body OfficeTime officeTime
    );

}