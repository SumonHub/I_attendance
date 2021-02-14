package com.infinity.i_attendance.ui.setting.leave_type;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.api.ApiManager;
import com.infinity.i_attendance.api.http.ApiResponse;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.utils.OnDataUpdateListener;
import com.infinity.i_attendance.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class AdapterLeaveType extends RecyclerView.Adapter<AdapterLeaveType.LeaveViewHolder> {
    private static final String TAG = "AdapterLeaveType";
    private final Context context;
    private List<LeaveType> leaveTypeList = new ArrayList<>();
    private OnDataUpdateListener onDataUpdateListener;

    public AdapterLeaveType(Context context) {
        this.context = context;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener onDataUpdateListener) {
        this.onDataUpdateListener = onDataUpdateListener;
    }

    public void setLeaveTypeList(List<LeaveType> leaveTypeList) {
        this.leaveTypeList = leaveTypeList;
    }

    @NonNull
    @Override
    public LeaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_leave_type, parent, false);
        return new LeaveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LeaveViewHolder holder, final int position) {
        final LeaveType leaveType = leaveTypeList.get(position);
        holder.leaveName.setText(leaveType.getName());
        holder.balance.setText(leaveType.getBalance());

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLeaveType(leaveType);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialAlertDialogBuilder(context)
                        .setIcon(R.drawable.ic_stop_hand_64)
                        .setTitle("Warning")
                        .setMessage("Are your sure to delete? It may causes for data corruption. ")
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteLeaveType(leaveType);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

            }
        });

    }

    private void deleteLeaveType(LeaveType leaveType) {
        Log.d(TAG, "deleteLeaveType: " + leaveType);
        User superuser = SharedPrefsHelper.getSuperUser(context);

        ApiManager.actionLeaveType(superuser.getApi_key(), leaveType, LeaveType.DELETE, new ApiManager.OnApiResponse() {
            @Override
            public void onSucceed(ApiResponse data) {
                Toast.makeText(context, "Successfully deleted.", Toast.LENGTH_SHORT).show();
                onDataUpdateListener.onSuccessfulDataUpdated();
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateLeaveType(LeaveType leaveType) {
        Bundle bundle = new Bundle();
        bundle.putString(AddLeaveTypeDialog.SELECTED_LEAVE_TYPE, new Gson().toJson(leaveType));
        AddLeaveTypeDialog dialog = AddLeaveTypeDialog.newInstance(bundle);
        FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        dialog.show(ft, AddLeaveTypeDialog.TAG);
        dialog.setOnDataUpdateListener(new OnDataUpdateListener() {
            @Override
            public void onSuccessfulDataUpdated() {
                onDataUpdateListener.onSuccessfulDataUpdated();
            }
        });

    }

    @Override
    public int getItemCount() {
        return leaveTypeList.size();
    }

    public class LeaveViewHolder extends RecyclerView.ViewHolder {
        private final TextView leaveName;
        private final TextView balance;
        private final ImageView update;
        private final ImageView delete;

        public LeaveViewHolder(@NonNull View itemView) {
            super(itemView);
            leaveName = itemView.findViewById(R.id.inputLeaveName);
            balance = itemView.findViewById(R.id.inputLeaveBalance);
            update = itemView.findViewById(R.id.btnUpdate);
            delete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
