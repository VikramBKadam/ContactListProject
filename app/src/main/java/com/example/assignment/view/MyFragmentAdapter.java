package com.example.assignment.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {
    public MyFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1: return new Tab2();
            default: return new Tab1();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0: return "View";
            case 1: return "Create Entry";

        }
        return null;

    }
}
