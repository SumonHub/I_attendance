package com.infinity.attendance.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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
import com.infinity.attendance.viewmodel.repo.DataRepository;

public class DataViewModel extends ViewModel {
    private static final String TAG = "DataViewModel";
    private final DataRepository repo;

    public DataViewModel() {
        repo = DataRepository.getInstance();
    }

    // ============= user =============

    public LiveData<ApiResponse<User>> requestLogin(String uid, String password) {
        return repo.requestLogin(uid, password);
    }

    public LiveData<ApiResponse> createOrUpdateUserRole(String apiKey, Role role) {
        return repo.createOrUpdateUserRole(apiKey, role);
    }

    public LiveData<ApiResponse<Role>> getAllUserRole(String apiKey) {
        return repo.getAllUserRole(apiKey);
    }

    public LiveData<ApiResponse<User>> createUser(String api_key, User user) {
        return repo.createUser(api_key, user);
    }

    public LiveData<ApiResponse> updateUser(String api_key, User updatedUser) {
        return repo.updateUser(api_key, updatedUser);
    }

    public LiveData<ApiResponse> addUserInfo(String api_key, UserInfo updatedUser) {
        return repo.addUserInfo(api_key, updatedUser);
    }

    public LiveData<ApiResponse> resetPassword(String api_key, String newPassword) {
        return repo.resetPassword(api_key, newPassword);
    }

    public LiveData<ApiResponse<User>> getUserByKeyLiveData(String api_key) {
        return repo.getUserByKey(api_key);
    }

    public LiveData<ApiResponse<UserInfo>> getUserInfoByKeyLiveData(String api_key) {
        return repo.getUserInfoByKey(api_key);
    }

    public LiveData<ApiResponse<User>> getAllUserLiveData(String api_key) {
        return repo.getAllUser(api_key);
    }

    public LiveData<ApiResponse<Attendance>> addAttendance(String api_key, String uid, String date, String time, String location, int statusCode) {
        return repo.addAttendance(api_key, uid, date, time, location, statusCode);
    }

    //

    public LiveData<ApiResponse<Attendance>> getAttendanceReportByDateRange(String api_key, String fromDate, String toDate) {
        return repo.getAttendanceReportByDateRange(api_key, fromDate, toDate);
    }

    public LiveData<ApiResponse<OfficeTime>> getOfficeTimeLiveData(String api_key) {
        return repo.getOfficeTimeList(api_key);
    }

    public LiveData<ApiResponse<Role>> getRoleLiveData(String api_key) {
        return repo.getRoleList(api_key);
    }

    public LiveData<ApiResponse<LeaveType>> getLeaveType(String api_key) {
        return repo.getLeaveType(api_key);
    }

    public LiveData<ApiResponse<Leave>> getUserLeaveHistory(String api_key) {
        return repo.getUserLeaveHistory(api_key);
    }

    public LiveData<ApiResponse<Leave>> getAllLeaveHistory(String api_key) {
        return repo.getAllLeaveHistory(api_key);
    }

    public LiveData<ApiResponse<Department>> getAllDepartment(String api_key) {
        return repo.getAllDepartment(api_key);
    }

    public LiveData<ApiResponse<Designation>> getDesignation(String api_key, int dId) {
        return repo.getAllDesignation(api_key, dId);
    }

    public LiveData<ApiResponse<Holiday>> getHolidayList(String api_key) {
        return repo.getHolidayList(api_key);
    }

    //============= Leave ===============

    public LiveData<ApiResponse> actionLeaveType(String api_key, LeaveType leaveType, int actionCode) {
        return repo.actionLeaveType(api_key, leaveType, actionCode);
    }

    public LiveData<ApiResponse> applyLeave(String api_key, Leave leave) {
        return repo.applyLeave(api_key, leave);
    }

    public LiveData<ApiResponse<Leave>> changeLeaveStatus(String api_key, int id, int status) {
        return repo.changeLeaveStatus(api_key, id, status);
    }


    //----------------Department----------------

    public LiveData<ApiResponse> createDepartment(String api_key, String department_name) {
        return repo.createDepartment(api_key, department_name);
    }

    public LiveData<ApiResponse> updateDepartment(String api_key, int id, String name) {
        return repo.updateDepartment(api_key, id, name);
    }

    public LiveData<ApiResponse> deleteDepartment(String api_key, int id) {
        return repo.deleteDepartment(api_key, id);
    }

    //============ Designation ===========

    public LiveData<ApiResponse> createDesignation(String api_key, int dpt_id, String dg_name) {
        return repo.createDesignation(api_key, dpt_id, dg_name);
    }

    public LiveData<ApiResponse> updateDesignation(String api_key, int id, String name) {
        return repo.updateDesignation(api_key, id, name);
    }

    public LiveData<ApiResponse> deleteDesignation(String api_key, int id) {
        return repo.deleteDesignation(api_key, id);
    }

    // --------------holiday-------------------

    public LiveData<ApiResponse<Holiday>> addHoliday(String api_key, String name, String date, int status) {
        return repo.addHoliday(api_key, name, date, status);
    }

    public LiveData<ApiResponse<Holiday>> updateHoliday(String api_key, int id, String name, String date, int status) {
        return repo.updateHoliday(api_key, id, name, date, status);
    }

    public LiveData<ApiResponse<Holiday>> deleteHoliday(String api_key, int id, int status) {
        return repo.deleteHoliday(api_key, id, status);
    }

    public LiveData<ApiResponse> checkIsAnyOffday(String api_key) {
        return repo.checkIsAnyOffday(api_key);
    }

    public LiveData<ApiResponse> updateOfficeTime(String api_key, OfficeTime officeTime) {
        return repo.updateOfficeTime(api_key, officeTime);
    }


}
