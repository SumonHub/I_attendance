package com.infinity.attendance.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infinity.attendance.R;
import com.infinity.attendance.data.model.Leave;
import com.infinity.attendance.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterLeaveHistory extends RecyclerView.Adapter<AdapterLeaveHistory.RoleViewHolder> {
    private static final String TAG = "AdapterLeaveHistory";
    private final Context context;
    private List<Leave> leaveList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public AdapterLeaveHistory(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void updateLeaveList(List<Leave> leaveList) {
        this.leaveList = leaveList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_leave_history, parent, false);
        return new RoleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoleViewHolder holder, int position) {
        final Leave leave = leaveList.get(position);

        holder.uname.setText(leave.getUname());
        holder.leaveType.setText(leave.getName());
        holder.dateRange.setText("From " + leave.getFrom_date() + " To " + leave.getTo_date());
        holder.leavePurpose.setText(leave.getPurpose());

        if (leave.getStatus() == Leave.PENDING) {
            holder.leaveStatus.setText("Pending");
        } else if (leave.getStatus() == Leave.APPROVED) {
            holder.leaveStatus.setText("Approved");
            holder.leaveStatus.setBackground(context.getDrawable(R.drawable.bg_general));
        } else if (leave.getStatus() == Leave.DECLINED) {
            holder.leaveStatus.setText("Decline");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leave.getStatus() == Leave.PENDING) {

                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }

    public class RoleViewHolder extends RecyclerView.ViewHolder {
        private final TextView uname;
        private final TextView leaveType;
        private final TextView dateRange;
        private final TextView leavePurpose;
        private final TextView leaveStatus;

        public RoleViewHolder(@NonNull View itemView) {
            super(itemView);
            uname = itemView.findViewById(R.id.uname);
            leaveType = itemView.findViewById(R.id.leaveType);
            dateRange = itemView.findViewById(R.id.dateRange);
            leavePurpose = itemView.findViewById(R.id.leavePurpose);
            leaveStatus = itemView.findViewById(R.id.leaveStatus);
        }
    }
}
