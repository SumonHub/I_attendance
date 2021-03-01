package com.infinity.attendance.data.model;

public class User {

    private String uid, name, password, email, phone, dg_name, dpt_name,
            joining_date, blood_group, role_name;
    private String encoded_cv;

    private int dg_id, dpt_id, role_id;
    private int access_code;
    private int status;

    public String getEncoded_cv() {
        return encoded_cv;
    }

    public void setEncoded_cv(String encoded_cv) {
        this.encoded_cv = encoded_cv;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDg_name() {
        return dg_name;
    }

    public void setDg_name(String dg_name) {
        this.dg_name = dg_name;
    }

    public String getDpt_name() {
        return dpt_name;
    }

    public void setDpt_name(String dpt_name) {
        this.dpt_name = dpt_name;
    }

    public int getDg_id() {
        return dg_id;
    }

    public void setDg_id(int dg_id) {
        this.dg_id = dg_id;
    }

    public int getDpt_id() {
        return dpt_id;
    }

    public void setDpt_id(int dpt_id) {
        this.dpt_id = dpt_id;
    }

    public String getJoining_date() {
        return joining_date;
    }

    public void setJoining_date(String joining_date) {
        this.joining_date = joining_date;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public int getAccess_code() {
        return access_code;
    }

    public void setAccess_code(int access_code) {
        this.access_code = access_code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dg_name='" + dg_name + '\'' +
                ", dpt_name='" + dpt_name + '\'' +
                ", joining_date='" + joining_date + '\'' +
                ", blood_group='" + blood_group + '\'' +
                ", role_name='" + role_name + '\'' +
                ", encoded_cv='" + encoded_cv + '\'' +
                ", dg_id=" + dg_id +
                ", dpt_id=" + dpt_id +
                ", role_id=" + role_id +
                ", access_code=" + access_code +
                ", status=" + status +
                '}';
    }
}
