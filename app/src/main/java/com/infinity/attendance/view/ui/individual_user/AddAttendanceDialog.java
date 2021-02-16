package com.infinity.attendance.view.ui.individual_user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.ConstantKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddAttendanceDialog extends DialogFragment {

    private static final String TAG = "AddAttendanceDialog";
    private User selectedUser;

    public AddAttendanceDialog() {
        // Required empty public constructor
    }

    public static AddAttendanceDialog newInstance(Bundle bundle) {
        AddAttendanceDialog fragment = new AddAttendanceDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        if (getArguments() != null) {
            String stringUser = getArguments().getString(ConstantKey.SELECTED_USER, null);
            selectedUser = new Gson().fromJson(stringUser, User.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_add_attendance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView weekday = view.findViewById(R.id.weekday);
        weekday.setText(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date()));
        TextView date = view.findViewById(R.id.date);
        date.setText(new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(new Date()));
        //
        TextView checking = view.findViewById(R.id.checking);
        //
        TextView checkin = view.findViewById(R.id.inTime);
        TextView checkinLoc = view.findViewById(R.id.checkinLoc);
        TextView checkout = view.findViewById(R.id.checkout);
        TextView checkoutLoc = view.findViewById(R.id.checkoutLoc);

        // 103.205.180.73

        /*DbHelper.addAttendance(selectedUser.getApi_key(), selectedUser.getUid(), currentDate, currentTime, "location",
                new DbHelper.OnApiResponse() {
            @Override
            public void onSucceed(ApiResponse data) {

            }

            @Override
            public void onFailed(String errorMsg) {

            }
        });*/


    }

}