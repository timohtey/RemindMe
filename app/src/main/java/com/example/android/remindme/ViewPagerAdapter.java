package com.example.android.remindme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by timohtey on 13/10/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private ArrayList<String> tabTitles;

    public ViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
        fragments = new ArrayList<>();
        tabTitles = new ArrayList<>();
    }

    public Fragment getItem(int position){
        return fragments.get(position);
    }

    public int getCount(){
        return fragments.size();
    }

    public void addFragments(Fragment fragment, String titles){
        fragments.add(fragment);
        tabTitles.add(titles);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
