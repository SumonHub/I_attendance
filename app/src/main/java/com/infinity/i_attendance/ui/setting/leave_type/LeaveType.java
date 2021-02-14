package com.infinity.i_attendance.ui.setting.leave_type;

public class LeaveType {
    public static final int ADD = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;

    private int type_id;
    private String name, balance;

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "LeaveType{" +
                "id=" + type_id +
                ", name='" + name + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}
