package com.infinity.i_attendance.ui.setting.role;

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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.utils.OnDataUpdateListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterRole extends RecyclerView.Adapter<AdapterRole.RoleViewHolder> {
    private static final String TAG = "AdapterRole";
    private final Context context;
    private List<Role> roleList = new ArrayList<>();
    private OnDataUpdateListener onDataUpdateListener;

    public AdapterRole(Context context) {
        this.context = context;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener onDataUpdateListener) {
        this.onDataUpdateListener = onDataUpdateListener;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @NonNull
    @Override
    public RoleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_role, parent, false);
        return new RoleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoleViewHolder holder, int position) {
        final Role role = roleList.get(position);

        holder.roleName.setText(role.getRole_name());
        //  holder.accessInfo.setText(""+role.getAccess_code());

        if (role.getAccess_code() == Role.Setting.SUPER_ADMIN.getAccess_code()) {
            holder.accessInfo.setText("Full Access");
        } else if (role.getAccess_code() == Role.Setting.GENERAL.getAccess_code()) {
            holder.accessInfo.setText("For employee");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Role.Setting setting : Role.Setting.values()) {
                // do what you want

                if (String.valueOf(role.getAccess_code()).contains(String.valueOf(setting.getAccess_code()))) {
                    stringBuilder.append(setting.getDisplayName()).append("\n");
                }
            }

            holder.accessInfo.setText(stringBuilder.toString().trim());
        }

        // update
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (role.getAccess_code() == Role.Setting.GENERAL.getAccess_code()
                        || role.getAccess_code() == Role.Setting.SUPER_ADMIN.getAccess_code()) {
                    Toast.makeText(context, "Can't change SuperAdmin & Employee", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString(AddRoleDialog.SELECTED_ROLE, new Gson().toJson(role));
                AddRoleDialog addRoleDialog = AddRoleDialog.newInstance(bundle);
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                addRoleDialog.show(ft, AddRoleDialog.TAG);
                addRoleDialog.setOnDataUpdateListener(new OnDataUpdateListener() {
                    @Override
                    public void onSuccessfulDataUpdated() {
                        onDataUpdateListener.onSuccessfulDataUpdated();
                    }
                });
            }
        });

        // delete
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(context)
                        .setIcon(R.drawable.ic_stop_hand_64)
                        .setTitle("Warning")
                        .setMessage("Are your sure to delete? It may causes for data corruption. ")
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteRole(role.getId());
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

    private void deleteRole(int roleId) {
        Toast.makeText(context, "Under construction.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return roleList.size();
    }

    public class RoleViewHolder extends RecyclerView.ViewHolder {
        private final TextView roleName;
        private final TextView accessInfo;
        private final ImageView btnUpdate;
        private final ImageView btnDelete;

        public RoleViewHolder(@NonNull View itemView) {
            super(itemView);
            roleName = itemView.findViewById(R.id.roleName);
            accessInfo = itemView.findViewById(R.id.accessInfo);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
