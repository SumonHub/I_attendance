package com.infinity.attendance.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.infinity.attendance.R;
import com.infinity.attendance.data.model.Role;
import com.infinity.attendance.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.AdminViewHolder> {

    private final Context context;
    private List<User> userList = new ArrayList<>();
    private OnItemClickListener onItemClick;
    private AlertDialog alert;


    public AdapterUser(Context context) {
        this.context = context;
    }

    public void setOnItemClick(OnItemClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void updateList(List<User> users) {
        userList = users;
    }

    @NonNull
    @Override
    public AdapterUser.AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUser.AdminViewHolder holder, int position) {
        final User selectedUser = userList.get(position);

        holder.uid.setText("ID: " + selectedUser.getUid());
        holder.adminName.setText(selectedUser.getName());
        holder.designation.setText(selectedUser.getDg_name() + " (" + selectedUser.getDpt_name() + ")");
        holder.user_roll.setText(selectedUser.getRole_name());

        if (selectedUser.getAccess_code() != Role.Setting.GENERAL.getAccess_code()) {
            holder.user_roll.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_admin));
        } else {
            holder.user_roll.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_general));
        }

        if (selectedUser.getDpt_id() == 0 || selectedUser.getDg_id() == 0 || selectedUser.getRole_id() == 0) {
            holder.info.setVisibility(View.VISIBLE);
        } else {
            holder.info.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {

        TextView uid, adminName, designation, user_roll, info;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            uid = itemView.findViewById(R.id.uid);
            adminName = itemView.findViewById(R.id.adminName);
            designation = itemView.findViewById(R.id.tvDesignation);
            user_roll = itemView.findViewById(R.id.user_roll);
            info = itemView.findViewById(R.id.info);
        }
    }
}
