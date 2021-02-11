package com.example.assignment.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.assignment.view.Tab1;
import com.example.assignment.view.Tab2;
import com.example.assignment.view.Tab3;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {
    public MyFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1: return new Tab2();
            case 2: return new Tab3();

            default: return new Tab1();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0: return "View";
            case 1: return "Create Entry";
            case 2: return "Contacts";

        }
        return null;

    }
}
