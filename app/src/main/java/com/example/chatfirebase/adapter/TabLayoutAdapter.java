package com.example.chatfirebase.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutAdapter extends FragmentPagerAdapter {

    private final List<Fragment> list = new ArrayList<>();
    private final List<String> titleList = new ArrayList<>();

    public TabLayoutAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

    }

    public void addFragment(Fragment fragment, String title) {
        list.add(fragment);
        titleList.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.titleList.get(position);
    }
}
