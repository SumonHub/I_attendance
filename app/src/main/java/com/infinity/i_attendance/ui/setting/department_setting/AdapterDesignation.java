package com.infinity.i_attendance.ui.setting.department_setting;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.api.ApiManager;
import com.infinity.i_attendance.api.http.ApiResponse;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.utils.OnDataUpdateListener;
import com.infinity.i_attendance.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class AdapterDesignation extends RecyclerView.Adapter<AdapterDesignation.DepartmentViewHolder> {
    private final Context context;
    private List<Designation> designationList = new ArrayList<>();
    private OnDataUpdateListener onDataUpdateListener;

    public AdapterDesignation(Context context) {
        this.context = context;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener onDataUpdateListener) {
        this.onDataUpdateListener = onDataUpdateListener;
    }

    public void setDesignationList(List<Designation> designationList) {
        this.designationList = designationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_dpt_dg, parent, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {
        Designation designation = designationList.get(position);
        holder.textView.setText(designation.getName());

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDesignation(designation);
            }
        });

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
                                deleteDesignation(designation.getId());
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

    private void deleteDesignation(int id) {
        User superUser = SharedPrefsHelper.getSuperUser(context);
        // set the custom layout

        ApiManager.deleteDesignation(superUser.getApi_key(), id, new ApiManager.OnApiResponse() {
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

    private void updateDesignation(Designation designation) {
        User superUser = SharedPrefsHelper.getSuperUser(context);
        // set the custom layout
        final View customLayout = LayoutInflater.from(context).inflate(R.layout.dialog_add_dp_dg, null);
        TextView headline = customLayout.findViewById(R.id.headline);
        headline.setText("Update designation");
        TextInputEditText inputText = customLayout.findViewById(R.id.inputText);
        inputText.setText(designation.getName());
        //
        final AlertDialog alertDialog = new MaterialAlertDialogBuilder(context)
                .setView(customLayout)
                .setPositiveButton("confirm", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (TextUtils.isEmpty(inputText.getText())) {
                            inputText.setError("can't be empty");
                            return;
                        }

                        ApiManager.updateDesignation(superUser.getApi_key(), designation.getId(), inputText.getText().toString(), new ApiManager.OnApiResponse() {
                            @Override
                            public void onSucceed(ApiResponse data) {
                                Toast.makeText(context, "Designation is created.", Toast.LENGTH_SHORT).show();
                                onDataUpdateListener.onSuccessfulDataUpdated();
                            }

                            @Override
                            public void onFailed(String errorMsg) {
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        });

                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return designationList.size();
    }

    public class DepartmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView btnUpdate;
        private final ImageView btnDelete;

        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
