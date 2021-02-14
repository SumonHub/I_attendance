package com.infinity.i_attendance.ui.manage_leave.fragment;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.ui.individual_user.ui.leave_report.AdapterLeaveHistory;
import com.infinity.i_attendance.ui.individual_user.ui.leave_report.Leave;
import com.infinity.i_attendance.ui.manage_leave.LeaveStatusChangeDialog;
import com.infinity.i_attendance.ui.manage_user.fragment.CreateOrUpdateUserDialog;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.utils.OnDataUpdateListener;
import com.infinity.i_attendance.utils.OnItemClickListener;
import com.infinity.i_attendance.utils.SharedPrefsHelper;
import com.infinity.i_attendance.viewmodel.DataViewModel;

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
        dataViewModel.getAllLeaveHistory(superUser.getApi_key()).observe(getViewLifecycleOwner(), new Observer<List<Leave>>() {
            @Override
            public void onChanged(List<Leave> leaves) {
                Log.d(TAG, "getAllLeaveHistory: " + leaves);

                if (leaves.size() == 0) {
                    emptyMsg.setVisibility(View.VISIBLE);
                } else {
                    emptyMsg.setVisibility(View.GONE);
                }

                List<Leave> leaveList = new ArrayList<>();


                for (Leave leave :
                        leaves) {
                    if (leave.getStatus() == 0) {
                        leaveList.add(leave);
                    }
                }

                if (leaveList.size() == 0) {
                    emptyMsg.setVisibility(View.VISIBLE);
                } else {
                    emptyMsg.setVisibility(View.GONE);
                }

                adapter.updateLeaveList(leaveList);

                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {

                        Bundle bundle = new Bundle();
                        bundle.putString(LeaveStatusChangeDialog.SELECTED_LEAVE, new Gson().toJson(leaveList.get(position)));

                        LeaveStatusChangeDialog dialog = LeaveStatusChangeDialog.newInstance(bundle);
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        dialog.show(ft, CreateOrUpdateUserDialog.TAG);
                        dialog.setOnDataUpdateListener(PendingLeaveFragment.this);
                    }
                });
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