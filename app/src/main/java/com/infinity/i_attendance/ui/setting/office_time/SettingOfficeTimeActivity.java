package com.infinity.i_attendance.ui.setting.office_time;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.utils.OnDataUpdateListener;
import com.infinity.i_attendance.utils.SharedPrefsHelper;
import com.infinity.i_attendance.utils.Utils;
import com.infinity.i_attendance.viewmodel.DataViewModel;

import java.util.List;

public class SettingOfficeTimeActivity extends AppCompatActivity implements OnDataUpdateListener {
    private AdapterOfficeTime adapterOfficeTime;
    private User superUser;

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
        //
        superUser = SharedPrefsHelper.getSuperUser(this);

        RecyclerView rvOfficeTimeHolder = findViewById(R.id.rvOfficeTimeHolder);

        adapterOfficeTime = new AdapterOfficeTime(this);
        adapterOfficeTime.setOnDataUpdateListener(this);

        rvOfficeTimeHolder.setHasFixedSize(true);
        rvOfficeTimeHolder.setLayoutManager(new LinearLayoutManager(this));
        rvOfficeTimeHolder.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvOfficeTimeHolder.setAdapter(adapterOfficeTime);
        bindRv();

    }

    private void bindRv() {
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getOfficeTimeLiveData(superUser.getApi_key()).observe(this, new Observer<List<OfficeTime>>() {
            @Override
            public void onChanged(List<OfficeTime> officeTimes) {
                adapterOfficeTime.setOfficeTimeList(officeTimes);
                adapterOfficeTime.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSuccessfulDataUpdated() {
        bindRv();
    }
}