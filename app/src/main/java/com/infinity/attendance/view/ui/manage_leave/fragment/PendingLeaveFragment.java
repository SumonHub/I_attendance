package com.infinity.attendance.view.ui.manage_leave.fragment;

import android.os.Bundle;
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
import com.infinity.attendance.view.adapter.AdapterLeaveHistory;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

import java.util.ArrayList;
import java.util.List;


public class PendingLeaveFragment extends Fragment {

    private static final String TAG = "LeaveFragment";

    private AdapterLeaveHistory adapter;
    private TextView emptyMsg;
    //
    private static final String LEAVE_TYPE = "LEAVE_TYPE";
    private int leaveType;

    public PendingLeaveFragment() {
        // Required empty public constructor
    }

    public static PendingLeaveFragment newInstance(int leaveType) {
        Bundle bundle = new Bundle();
        bundle.putInt(LEAVE_TYPE, leaveType);
        PendingLeaveFragment fragment = new PendingLeaveFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            leaveType = getArguments().getInt(LEAVE_TYPE, 0);
        }
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

        emptyMsg = view.findViewById(R.id.emptyMsg);
        RecyclerView recyclerView = view.findViewById(R.id.rvAllLeaveHistory);
        adapter = new AdapterLeaveHistory(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

    }

    public void _getData() {
        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getLeaveHistory("").observe(getViewLifecycleOwner(), new Observer<ApiResponse<Leave>>() {
            @Override
            public void onChanged(ApiResponse<Leave> leaveApiResponse) {
                if (leaveApiResponse != null && !leaveApiResponse.isError()) {
                    List<Leave> leaveList = new ArrayList<>();
                    for (Leave leave :
                            leaveApiResponse.getData()) {
                        if (leave.getStatus() == leaveType) {
                            leaveList.add(leave);
                        }
                    }
                    _updateAdapter(leaveList);
                } else {
                    Toast.makeText(getContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void _updateAdapter(List<Leave> leaveList) {
        adapter.updateLeaveList(leaveList);
        if (adapter.getItemCount() > 0) {
            emptyMsg.setVisibility(View.GONE);
        } else {
            emptyMsg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        _getData();
    }
}