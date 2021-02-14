package com.infinity.i_attendance.ui.setting.holiday_setting;

public class Holiday {
    public static final int ADD = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;
    private int id;
    private String name, date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
