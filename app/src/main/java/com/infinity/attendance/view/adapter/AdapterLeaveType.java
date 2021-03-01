package com.infinity.attendance.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.LeaveType;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.view.ui.setting.leave_type.AddLeaveTypeDialog;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.util.ArrayList;
import java.util.List;

public class AdapterLeaveType extends RecyclerView.Adapter<AdapterLeaveType.LeaveViewHolder> {
    private static final String TAG = "AdapterLeaveType";
    private final Context context;
    private List<LeaveType> leaveTypeList = new ArrayList<>();
    private OnDataUpdateListener<List<LeaveType>> onDataUpdateListener;

    public AdapterLeaveType(Context context) {
        this.context = context;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener<List<LeaveType>> onDataUpdateListener) {
        this.onDataUpdateListener = onDataUpdateListener;
    }

    public void setLeaveTypeList(List<LeaveType> leaveTypeList) {
        this.leaveTypeList = leaveTypeList;
        notifyDataSetChanged();
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
                _updateLeaveTypeDialog(leaveType);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialAlertDialogBuilder(context)
                        .setIcon(R.drawable.ic_stop_hand_64)
                        .setTitle("Warning")
                        .setMessage("Are your sure to delete? It may causes for data corruption. ")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                _deleteLeaveType(leaveType);
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

    private void _deleteLeaveType(LeaveType leaveType) {
        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.deleteLeave(leaveType.getId()).observe((LifecycleOwner) context, new Observer<ApiResponse<LeaveType>>() {
            @Override
            public void onChanged(ApiResponse<LeaveType> listApiResponse) {
                if (listApiResponse != null && !listApiResponse.isError()) {
                    Toast.makeText(context, "Successfully deleted.", Toast.LENGTH_SHORT).show();
                    if (onDataUpdateListener != null) {
                        onDataUpdateListener.onSuccessfulDataUpdated(listApiResponse.getData());
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void _updateLeaveTypeDialog(LeaveType leaveType) {
        Bundle bundle = new Bundle();
        bundle.putString(AddLeaveTypeDialog.SELECTED_LEAVE_TYPE, new Gson().toJson(leaveType));
        AddLeaveTypeDialog dialog = AddLeaveTypeDialog.newInstance(bundle);
        FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        dialog.show(ft, AddLeaveTypeDialog.TAG);
        dialog.setOnDataUpdateListener(new OnDataUpdateListener<List<LeaveType>>() {
            @Override
            public void onSuccessfulDataUpdated(List<LeaveType> object) {
                if (onDataUpdateListener != null) {
                    onDataUpdateListener.onSuccessfulDataUpdated(object);
                }
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
