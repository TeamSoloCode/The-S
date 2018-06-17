package com.example.bruce.myapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BRUCE on 8/31/2017.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mListFragment = new ArrayList<>();
    private final List<String> mListTitleFragment = new ArrayList<>();
    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title){
        mListFragment.add(fragment);
        mListTitleFragment.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position) {

        return mListTitleFragment.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }
}
