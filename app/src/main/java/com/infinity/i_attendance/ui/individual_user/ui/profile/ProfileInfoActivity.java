package com.infinity.i_attendance.ui.individual_user.ui.profile;

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
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.ui.individual_user.ui.profile.fragment.ContactDetailsFragment;
import com.infinity.i_attendance.ui.individual_user.ui.profile.fragment.OtherDetailsFragment;
import com.infinity.i_attendance.ui.individual_user.ui.profile.fragment.PersonalDetailsFragment;
import com.infinity.i_attendance.utils.ConstantKey;

public class ProfileInfoActivity extends AppCompatActivity {

    // tab titles
    public String[] titles = new String[]{"Personal Details", "Contact Details", "Others"};
    private String stringSelectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        //
        FloatingActionButton fabBack = findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //
        stringSelectedUser = getIntent().getStringExtra(ConstantKey.SELECTED_USER);
        //

        ViewPager2 viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new ViewPagerFragmentAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tab_layout);

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
                    return PersonalDetailsFragment.newInstance(stringSelectedUser);
                case 1:
                    return ContactDetailsFragment.newInstance(stringSelectedUser);
                case 2:
                    return new OtherDetailsFragment();
            }
            return new PersonalDetailsFragment();
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }
}