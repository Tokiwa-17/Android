package com.example.myapplication.adapter;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.fragment.homepageEdit.EditSelfInfoFragment;

public class EditInfoPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public EditInfoPagerAdapter(@NonNull FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new EditSelfInfoFragment();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
