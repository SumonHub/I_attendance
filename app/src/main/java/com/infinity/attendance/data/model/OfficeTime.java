package com.infinity.attendance.data.model;

public class OfficeTime {
    public static final int ON_DAY = 1;
    public static final int OFF_DAY = 2;
    private int id;
    private String day_name, starting_time, ending_time;
    private int status;

    public OfficeTime(int id, String day_name, String starting_time, String endingtime, int status) {
        this.id = id;
        this.day_name = day_name;
        this.starting_time = starting_time;
        this.ending_time = endingtime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay_name() {
        return day_name;
    }

    public void setDay_name(String day_name) {
        this.day_name = day_name;
    }

    public String getStarting_time() {
        return starting_time;
    }

    public void setStarting_time(String starting_time) {
        this.starting_time = starting_time;
    }

    public String getEnding_time() {
        return ending_time;
    }

    public void setEnding_time(String ending_time) {
        this.ending_time = ending_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OfficeTime{" +
                "id=" + id +
                ", dayName='" + day_name + '\'' +
                ", startingTime='" + starting_time + '\'' +
                ", EndingTime='" + ending_time + '\'' +
                ", dayStatus=" + status +
                '}';
    }
}
