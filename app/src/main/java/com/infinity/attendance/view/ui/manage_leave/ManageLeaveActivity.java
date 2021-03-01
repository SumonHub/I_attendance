package com.infinity.attendance.view.ui.manage_leave;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.infinity.attendance.R;
import com.infinity.attendance.view.ui.manage_leave.fragment.PendingLeaveFragment;


public class ManageLeaveActivity extends AppCompatActivity {

    // tab titles
    public String[] titles = new String[]{"Pending", "Approved", "Declined"};


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
                    return PendingLeaveFragment.newInstance(0);
                case 1:
                    return PendingLeaveFragment.newInstance(1);
                case 2:
                    return PendingLeaveFragment.newInstance(2);
            }
            return new PendingLeaveFragment();
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }
}