package com.infinity.attendance.data.model;

public class Designation {
    private int id;
    private int dpt_id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDpt_id() {
        return dpt_id;
    }

    public void setDpt_id(int dpt_id) {
        this.dpt_id = dpt_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Designation{" +
                "id=" + id +
                ", dpt_id=" + dpt_id +
                ", name='" + name + '\'' +
                '}';
    }
}
