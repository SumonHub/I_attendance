package com.infinity.i_attendance.ui.manage_leave;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.ui.manage_leave.fragment.ApprovedLeaveFragment;
import com.infinity.i_attendance.ui.manage_leave.fragment.DeclinedLeaveFragment;
import com.infinity.i_attendance.ui.manage_leave.fragment.PendingLeaveFragment;
import com.infinity.i_attendance.ui.manage_user.adapter.UserAdapter;
import com.infinity.i_attendance.ui.manage_user.model.User;

import java.util.List;

public class ManageLeaveActivity extends AppCompatActivity {

    // tab titles
    public String[] titles = new String[]{"Pending", "Approved", "Declined"};
    private List<User> userList;
    private RecyclerView rvUserHolder;
    private UserAdapter userAdapter;
    private TextView emptyMsg;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_leave);

        //
        FloatingActionButton fabBack = findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //
        /*viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/

        ViewPager2 viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new ViewPagerFragmentAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tabs);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        }).attach();

    }

    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new PendingLeaveFragment();
                case 1:
                    return new ApprovedLeaveFragment();
                case 2:
                    return new DeclinedLeaveFragment();
            }
            return new PendingLeaveFragment();
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }

    /*private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PendingLeaveFragment(), "Pending");
        adapter.addFragment(new ApprovedLeaveFragment(), "Approved");
        adapter.addFragment(new DeclinedLeaveFragment(), "Declined");
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    Utils.showDialogWaiting(ManageLeaveActivity.this);
                    PendingLeaveFragment pendingLeaveFragment = (PendingLeaveFragment) adapter.getItem(position);
                    pendingLeaveFragment.bindRv();
                }

                if (position == 1) {
                    Utils.showDialogWaiting(ManageLeaveActivity.this);
                    ApprovedLeaveFragment approvedLeaveFragment = (ApprovedLeaveFragment) adapter.getItem(position);
                    approvedLeaveFragment.bindRv();
                }

                if (position == 2) {
                    Utils.showDialogWaiting(ManageLeaveActivity.this);
                    DeclinedLeaveFragment declinedLeaveFragment = (DeclinedLeaveFragment) adapter.getItem(position);
                    declinedLeaveFragment.bindRv();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/

}