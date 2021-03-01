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

    public LiveData<ApiResponse<User>> addUser(User user) {
        return repo.addUser(user);
    }

    public LiveData<ApiResponse<User>> updateUser(User updatedUser) {
        return repo.updateUser(updatedUser);
    }

    public LiveData<ApiResponse<UserInfo>> addUserInfo(String uid, UserInfo updatedUser) {
        return repo.addUserInfo(uid, updatedUser);
    }

    public LiveData<ApiResponse<User>> resetPassword(String uid, String newPassword) {
        return repo.resetPassword(uid, newPassword);
    }

    public LiveData<ApiResponse<User>> getUser(String uid) {
        return repo.getUser(uid);
    }

    public LiveData<ApiResponse<Attendance>> addAttendance(String uid, String date, String time, String location, int statusCode) {
        return repo.addAttendance(uid, date, time, location, statusCode);
    }

    public LiveData<ApiResponse<Attendance>> getAttendanceReportByDateRange(String uid, String fromDate, String toDate) {
        return repo.getAttendanceReportByDateRange(uid, fromDate, toDate);
    }
    // ========= role ================

    public LiveData<ApiResponse<Role>> getAllUserRole() {
        return repo.getRole();
    }

    public LiveData<ApiResponse<Role>> addRole(Role role) {
        return repo.addRole(role);
    }

    //============= Leave ===============

    public LiveData<ApiResponse<Leave>> getLeaveHistory(String uid) {
        return repo.getUserLeaveHistory(uid);
    }

    public LiveData<ApiResponse<LeaveType>> getLeaveType() {
        return repo.getLeaveType();
    }

    public LiveData<ApiResponse<Leave>> applyLeave(Leave leave) {
        return repo.applyLeave(leave);
    }

    public LiveData<ApiResponse<Leave>> changeLeaveStatus(String id, Leave leave) {
        return repo.changeLeaveStatus(id, leave);
    }

    //----------------Department----------------
    public LiveData<ApiResponse<Department>> getAllDepartment() {
        return repo.getAllDepartment();
    }

    public LiveData<ApiResponse<Department>> addDepartment(Department department) {
        return repo.addDepartment(department);
    }

    public LiveData<ApiResponse<Department>> updateDepartment(Department department) {
        return repo.updateDepartment(department);
    }

    public LiveData<ApiResponse<Department>> deleteDepartment(int id) {
        return repo.deleteDepartment(id);
    }

    //============ Designation ===========
    public LiveData<ApiResponse<Designation>> getDesignationByDpt(int dpt_id) {
        return repo.getDesignationByDpt(dpt_id);
    }

    public LiveData<ApiResponse<Designation>> addDesignation(Designation designation) {
        return repo.addDesignation(designation);
    }

    public LiveData<ApiResponse<Designation>> updateDesignation(Designation designation) {
        return repo.updateDesignation(designation);
    }

    public LiveData<ApiResponse<Designation>> deleteDesignation(int designationId, int dpt_id) {
        return repo.deleteDesignation(designationId, dpt_id);
    }

    // --------------holiday-------------------
    public LiveData<ApiResponse<Holiday>> getHoliday() {
        return repo.getHolidayList();
    }

    public LiveData<ApiResponse<Holiday>> addHoliday(Holiday holiday) {
        return repo.addHoliday(holiday);
    }

    public LiveData<ApiResponse<Holiday>> updateHoliday(Holiday holiday) {
        return repo.updateHoliday(holiday);
    }

    public LiveData<ApiResponse<Holiday>> deleteHoliday(int id) {
        return repo.deleteHoliday(id);
    }

    public LiveData<ApiResponse> checkIsAnyOffday() {
        return repo.checkIsAnyOffday();
    }

    public LiveData<ApiResponse<OfficeTime>> getOfficeTimeLiveData() {
        return repo.getOfficeTime();
    }

    public LiveData<ApiResponse<OfficeTime>> updateOfficeTime(OfficeTime officeTime) {
        return repo.updateOfficeTime(officeTime);
    }

    public LiveData<ApiResponse<UserInfo>> getUserInfoLiveData(String uid) {
        return repo.getUserInfo(uid);
    }

    public LiveData<ApiResponse<LeaveType>> addLeaveType(LeaveType newLeaveType) {
        return repo.addLeaveType(newLeaveType);
    }

    public LiveData<ApiResponse<LeaveType>> updateLeaveType(LeaveType newLeaveType) {
        return repo.updateLeaveType(newLeaveType);
    }

    public LiveData<ApiResponse<LeaveType>> deleteLeave(int id) {
        return repo.deleteLeave(id);
    }
}
