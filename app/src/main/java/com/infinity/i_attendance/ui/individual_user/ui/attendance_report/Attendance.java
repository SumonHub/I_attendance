package com.infinity.i_attendance.ui.individual_user.ui.attendance_report;

public class Attendance {
    public static final int DEFAULT = 0;
    public static final int MISSING_OUT_TIME = 1;
    public static final int LATE_IN_EARLY_OUT = 2;
    public static final int LATE_IN = 3;
    public static final int EARLY_OUT = 4;

    private int id, uid, status;
    private String date, in_time, out_time, in_loc, out_loc;
    private int late_status;


    public String getIn_loc() {
        return in_loc;
    }

    public void setIn_loc(String in_loc) {
        this.in_loc = in_loc;
    }

    public String getOut_loc() {
        return out_loc;
    }

    public void setOut_loc(String out_loc) {
        this.out_loc = out_loc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLate_status() {
        return late_status;
    }

    public void setLate_status(int late_status) {
        this.late_status = late_status;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", uid=" + uid +
                ", status=" + status +
                ", date='" + date + '\'' +
                ", in_time='" + in_time + '\'' +
                ", out_time='" + out_time + '\'' +
                ", in_loc='" + in_loc + '\'' +
                ", out_loc='" + out_loc + '\'' +
                ", late_status=" + late_status +
                '}';
    }
}
