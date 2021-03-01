package com.infinity.attendance.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.OfficeTime;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.view.ui.setting.office_time.EditOfficeTimeDialog;

import java.util.ArrayList;
import java.util.List;

public class AdapterOfficeTime extends RecyclerView.Adapter<AdapterOfficeTime.OfficeTimeViewHolder> {
    private final Context context;
    private List<OfficeTime> officeTimeList = new ArrayList<>();
    private OnDataUpdateListener<List<OfficeTime>> onDataUpdateListener;

    public AdapterOfficeTime(Context context) {
        this.context = context;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener<List<OfficeTime>> onDataUpdateListener) {
        this.onDataUpdateListener = onDataUpdateListener;
    }

    public void setOfficeTimeList(List<OfficeTime> officeTimeList) {
        this.officeTimeList = officeTimeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OfficeTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_office_time, parent, false);
        return new OfficeTimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficeTimeViewHolder holder, int position) {
        final OfficeTime officeTime = officeTimeList.get(position);

        holder.dayName.setText(officeTime.getDay_name());
        holder.inputStartDate.setText(officeTime.getStarting_time());
        holder.inputEndDate.setText(officeTime.getEnding_time());

        if (officeTime.getStatus() == OfficeTime.OFF_DAY) {
            holder.textInputLayout8.setBoxBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
            holder.inputDayStatus.setText("OFF DAY");
        } else {
            holder.textInputLayout8.setBoxBackgroundColor(context.getResources().getColor(android.R.color.white));
            holder.inputDayStatus.setText("ON DAY");
        }

        // holder.inputDayStatus.setEnabled(false);

        holder.fabEditOfficeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(EditOfficeTimeDialog.SELECTED_DAY, new Gson().toJson(officeTime));
                EditOfficeTimeDialog editOfficeTimeDialog = EditOfficeTimeDialog.newInstance(bundle);
                FragmentTransaction ft2 = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                editOfficeTimeDialog.show(ft2, EditOfficeTimeDialog.TAG);
                editOfficeTimeDialog.setOnDataUpdateListener(new OnDataUpdateListener<List<OfficeTime>>() {
                    @Override
                    public void onSuccessfulDataUpdated(List<OfficeTime> object) {
                        if (onDataUpdateListener != null) {
                            onDataUpdateListener.onSuccessfulDataUpdated(object);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return officeTimeList.size();
    }

    public class OfficeTimeViewHolder extends RecyclerView.ViewHolder {
        private final TextView dayName;
        private final TextInputEditText inputStartDate;
        private final TextInputEditText inputEndDate;
        private final TextInputEditText inputDayStatus;
        private final TextInputLayout textInputLayout8;
        private final FloatingActionButton fabEditOfficeTime;

        public OfficeTimeViewHolder(@NonNull View itemView) {
            super(itemView);

            dayName = itemView.findViewById(R.id.dayName);
            inputStartDate = itemView.findViewById(R.id.inputStartTime);
            inputEndDate = itemView.findViewById(R.id.inputEndTime);
            inputDayStatus = itemView.findViewById(R.id.inputDayStatus);
            fabEditOfficeTime = itemView.findViewById(R.id.fabEditOfficeTime);
            textInputLayout8 = itemView.findViewById(R.id.textInputLayout8);

        }
    }
}
