package com.infinity.i_attendance.ui.individual_user.ui.leave_report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.i_attendance.R;
import com.infinity.i_attendance.ui.setting.leave_type.LeaveType;

import java.util.ArrayList;
import java.util.List;

public class AdapterDialogLeaveTypeList extends ArrayAdapter<LeaveType> {
    private final Context mContext;
    private List<LeaveType> leaveTypeList = new ArrayList<>();

    public AdapterDialogLeaveTypeList(Context context, List<LeaveType> list) {
        super(context, 0, list);
        mContext = context;
        leaveTypeList = list;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.row_role_select_dialog, parent, false);

        LeaveType leaveType = leaveTypeList.get(position);

        TextView name = listItem.findViewById(R.id.textView);
        name.setText(leaveType.getName());

        return listItem;
    }
}
