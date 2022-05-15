package com.example.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragment.main.NotificationFragment;
import com.example.myapplication.fragment.main.PostlistFragment;

public class DashboardAdapter extends FragmentStateAdapter {
    public DashboardAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new NotificationFragment();
            case 1: return new PostlistFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}