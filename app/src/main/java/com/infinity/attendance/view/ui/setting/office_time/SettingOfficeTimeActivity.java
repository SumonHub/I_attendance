package com.infinity.attendance.view.ui.setting.office_time;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.OfficeTime;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.Utils;
import com.infinity.attendance.view.adapter.AdapterOfficeTime;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.util.List;

public class SettingOfficeTimeActivity extends AppCompatActivity {
    private AdapterOfficeTime adapterOfficeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_office_time);

        Utils.showDialogWaiting(this);

        FloatingActionButton fabBack = findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        adapterOfficeTime = new AdapterOfficeTime(this);
        adapterOfficeTime.setOnDataUpdateListener(new OnDataUpdateListener<List<OfficeTime>>() {
            @Override
            public void onSuccessfulDataUpdated(List<OfficeTime> object) {
                adapterOfficeTime.setOfficeTimeList(object);
            }
        });

        RecyclerView rvOfficeTimeHolder = findViewById(R.id.rvOfficeTimeHolder);
        rvOfficeTimeHolder.setHasFixedSize(true);
        rvOfficeTimeHolder.setLayoutManager(new LinearLayoutManager(this));
        rvOfficeTimeHolder.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvOfficeTimeHolder.setAdapter(adapterOfficeTime);

        _getData();

    }

    private void _getData() {
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getOfficeTimeLiveData().observe(this, new Observer<ApiResponse<OfficeTime>>() {
            @Override
            public void onChanged(ApiResponse<OfficeTime> officeTimeApiResponse) {
                if (officeTimeApiResponse != null && !officeTimeApiResponse.isError()) {
                    adapterOfficeTime.setOfficeTimeList(officeTimeApiResponse.getData());
                } else {
                    Toast.makeText(SettingOfficeTimeActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}