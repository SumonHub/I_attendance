package com.infinity.attendance.data.model;

public class UserInfo {
    String f_name, m_name, dob, religion, gender, marital_status, nationality, nid_no, p_photo;
    String c_address, p_address;

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNid_no() {
        return nid_no;
    }

    public void setNid_no(String nid_no) {
        this.nid_no = nid_no;
    }

    public String getP_photo() {
        return p_photo;
    }

    public void setP_photo(String p_photo) {
        this.p_photo = p_photo;
    }

    public String getC_address() {
        return c_address;
    }

    public void setC_address(String c_address) {
        this.c_address = c_address;
    }

    public String getP_address() {
        return p_address;
    }

    public void setP_address(String p_address) {
        this.p_address = p_address;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "f_name='" + f_name + '\'' +
                ", m_name='" + m_name + '\'' +
                ", dob='" + dob + '\'' +
                ", religion='" + religion + '\'' +
                ", gender='" + gender + '\'' +
                ", marital_status='" + marital_status + '\'' +
                ", nationality='" + nationality + '\'' +
                ", nid_no='" + nid_no + '\'' +
                ", p_photo='" + p_photo + '\'' +
                ", c_address='" + c_address + '\'' +
                ", p_address='" + p_address + '\'' +
                '}';
    }
}
