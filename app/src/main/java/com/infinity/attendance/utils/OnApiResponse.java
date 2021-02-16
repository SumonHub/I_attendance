package com.infinity.attendance.utils;

public interface OnApiResponse<T> {
    void onSucceed(T data);

    void onFailed(String errorMsg);
}
