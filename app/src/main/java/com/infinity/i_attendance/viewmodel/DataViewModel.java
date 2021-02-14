package com.infinity.i_attendance.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

public class DataViewModel extends ViewModel {
    private static final String TAG = "DataViewModel";
    private final DataRepository repo;
    MutableLiveData<List<User>> employeeLiveData;
    MutableLiveData<List<Attendance>> attendanceLiveData;
    MutableLiveData<List<OfficeTime>> officeTimeLiveData;
    MutableLiveData<List<Role>> roleLiveData;
    MutableLiveData<List<LeaveType>> leaveType;


    public DataViewModel() {
        repo = DataRepository.getInstance();
    }

    public LiveData<User> getUserByKeyLiveData(String api_key) {
        return repo.getUserByKey(api_key);
    }

    public LiveData<UserInfo> getUserInfoByKeyLiveData(String api_key) {
        return repo.getUserInfoByKey(api_key);
    }

    public LiveData<List<User>> getAllUserLiveData(String api_key) {
        return repo.getAllUser(api_key);
    }

    public LiveData<List<Attendance>> getAttendanceReportByDateRange(String api_key, String fromDate, String toDate) {
        return repo.getAttendanceReportByDateRange(api_key, fromDate, toDate);
    }

    public LiveData<List<OfficeTime>> getOfficeTimeLiveData(String api_key) {
        return repo.getOfficeTimeList(api_key);
    }

    public LiveData<List<Role>> getRoleLiveData(String api_key) {
        return repo.getRoleList(api_key);
    }

    public LiveData<List<LeaveType>> getLeaveType(String api_key) {
        return repo.getLeaveType(api_key);
    }

    public LiveData<List<Leave>> getUserLeaveHistory(String api_key) {
        return repo.getUserLeaveHistory(api_key);
    }

    public LiveData<List<Leave>> getAllLeaveHistory(String api_key) {
        return repo.getAllLeaveHistory(api_key);
    }

    public LiveData<List<Department>> getAllDepartment(String api_key) {
        return repo.getAllDepartment(api_key);
    }

    public LiveData<List<Designation>> getDesignation(String api_key, int dId) {
        return repo.getAllDesignation(api_key, dId);
    }

    public LiveData<List<Holiday>> getHolidayList(String api_key) {
        return repo.getHolidayList(api_key);
    }
}
