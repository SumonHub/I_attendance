package com.infinity.attendance.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.attendance.R;
import com.infinity.attendance.data.model.LeaveType;

import java.util.ArrayList;
import java.util.List;

public class AdapterDialogLeaveType extends ArrayAdapter<LeaveType> {
    private final Context mContext;
    private List<LeaveType> leaveTypeList = new ArrayList<>();

    public AdapterDialogLeaveType(Context context, List<LeaveType> list) {
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
