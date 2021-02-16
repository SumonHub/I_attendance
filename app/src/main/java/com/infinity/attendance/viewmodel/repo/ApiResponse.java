package com.infinity.attendance.viewmodel.repo;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse<T> {
    boolean error;
    String msg;
    List<T> results = new ArrayList<>();

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "error=" + error +
                ", msg='" + msg + '\'' +
                ", results=" + results +
                '}';
    }
}
