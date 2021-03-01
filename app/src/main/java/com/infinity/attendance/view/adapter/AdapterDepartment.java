package com.infinity.attendance.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.Department;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.view.ui.setting.department.SettingDesignationActivity;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.util.ArrayList;
import java.util.List;

public class AdapterDepartment extends RecyclerView.Adapter<AdapterDepartment.DepartmentViewHolder> {
    private final Context context;
    private List<Department> departmentList = new ArrayList<>();
    private OnDataUpdateListener<List<Department>> onDataUpdateListener;

    public AdapterDepartment(Context context) {
        this.context = context;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener<List<Department>> onDataUpdateListener) {
        this.onDataUpdateListener = onDataUpdateListener;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
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
        Department department = departmentList.get(position);
        holder.textView.setText(department.getName());
        // update
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _updateDepartmentDialog(department);
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
                                _deleteDepartment(department);
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingDesignationActivity.class);
                intent.putExtra("DPT_ID", department.getId());
                context.startActivity(intent);
            }
        });
    }

    private void _updateDepartmentDialog(Department department) {
        final View customLayout = LayoutInflater.from(context).inflate(R.layout.dialog_add_dp_dg, null);
        TextView headline = customLayout.findViewById(R.id.headline);
        headline.setText("Update department");
        TextInputEditText inputText = customLayout.findViewById(R.id.inputText);
        inputText.setText(department.getName());
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
                        department.setName(inputText.getText().toString());
                        _updateDepartment(department);
                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void _updateDepartment(Department department) {
        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.updateDepartment(department)
                .observe((LifecycleOwner) context, new Observer<ApiResponse<Department>>() {
                    @Override
                    public void onChanged(ApiResponse<Department> departmentApiResponse) {
                        if (departmentApiResponse != null) {
                            Toast.makeText(context, "Successfully updated.", Toast.LENGTH_SHORT).show();
                            if (onDataUpdateListener != null) {
                                onDataUpdateListener.onSuccessfulDataUpdated(departmentApiResponse.getData());
                            }
                        } else {
                            Toast.makeText(context, context.getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void _deleteDepartment(Department department) {
        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.deleteDepartment(department.getId())
                .observe((LifecycleOwner) context, new Observer<ApiResponse<Department>>() {
                    @Override
                    public void onChanged(ApiResponse<Department> departmentApiResponse) {
                        if (departmentApiResponse != null) {
                            Toast.makeText(context, "Successfully deleted.", Toast.LENGTH_SHORT).show();
                            if (onDataUpdateListener != null) {
                                onDataUpdateListener.onSuccessfulDataUpdated(departmentApiResponse.getData());
                            }
                        } else {
                            Toast.makeText(context, context.getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return departmentList.size();
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
