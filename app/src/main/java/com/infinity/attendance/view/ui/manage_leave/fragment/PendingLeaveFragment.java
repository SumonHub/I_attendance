package com.infinity.attendance.view.ui.manage_leave.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infinity.attendance.R;
import com.infinity.attendance.data.model.Leave;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.SharedPrefsHelper;
import com.infinity.attendance.view.adapter.AdapterLeaveHistory;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.util.ArrayList;
import java.util.List;


public class PendingLeaveFragment extends Fragment implements OnDataUpdateListener {

    private static final String TAG = "LeaveFragment";

    private AdapterLeaveHistory adapter;
    private TextView emptyMsg;

    public PendingLeaveFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leave, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: LeaveFragment");

        emptyMsg = view.findViewById(R.id.emptyMsg);
        RecyclerView recyclerView = view.findViewById(R.id.rvAllLeaveHistory);
        adapter = new AdapterLeaveHistory(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);


    }

    public void bindRv() {
        //
        final User superUser = SharedPrefsHelper.getSuperUser(getContext());
        //
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getAllLeaveHistory(superUser.getApi_key()).observe(getViewLifecycleOwner(), new Observer<ApiResponse<Leave>>() {
            @Override
            public void onChanged(ApiResponse<Leave> leaveApiResponse) {
                if (leaveApiResponse != null && !leaveApiResponse.isError()) {
                    List<Leave> leaveList = new ArrayList<>();

                    for (Leave leave :
                            leaveApiResponse.getResults()) {
                        if (leave.getStatus() == 0) {
                            leaveList.add(leave);
                        }
                    }

                    adapter.updateLeaveList(leaveList);
                    if (adapter.getItemCount() > 0) {
                        emptyMsg.setVisibility(View.GONE);
                    } else {
                        emptyMsg.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onSuccessfulDataUpdated() {
        Toast.makeText(getContext(), "Successfully approved.", Toast.LENGTH_SHORT).show();
        bindRv();
    }

    @Override
    public void onResume() {
        super.onResume();
        bindRv();
    }
}