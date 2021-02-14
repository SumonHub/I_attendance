package com.infinity.i_attendance.ui.manage_user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.infinity.i_attendance.R;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.ui.setting.role.Role;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.AdminViewHolder> {

    private final Context context;
    private List<User> userList = new ArrayList<>();
    private OnItemClickListener onItemClick;
    private AlertDialog alert;


    public UserAdapter(Context context) {
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
    public UserAdapter.AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.AdminViewHolder holder, int position) {
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

        //
       /* holder.user_roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createD();

                // shared pref
                SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
                String stringUser = prefs.getString("USER", null);
                User user = new Gson().fromJson(stringUser, User.class);

                DbHelper.getAllUserRole(user.getApi_key(), new DbHelper.OnApiResponse<Role>() {
                    @Override
                    public void onSucceed(ApiResponse<Role> data) {


                        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_role, null);
                        ListView listView = view.findViewById(R.id.list);
                        AdapterDialogRoleList adapter = new AdapterDialogRoleList(context, data.getResults());
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                holder.user_roll.setText(data.getResults().get(position).getRole_name());
                                alert.dismiss();
                                onRoleChangeListener.onRoleChange(selectedUser.getApi_key(), data.getResults().get(position).getAccess_code());
                            }
                        });

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setView(view);
                        alert = alertDialog.create();
                        alert.show();
                    }

                    @Override
                    public void onFailed(String errorMsg) {

                    }
                });

            }
        });*/

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
