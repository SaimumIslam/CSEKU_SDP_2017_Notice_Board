package com.example.foysal.noticeboardextend;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


public class PagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tabTiles = new ArrayList<>();

    public  void addFragments(Fragment fragment, String tabtile)
    {
        this.fragments.add(fragment);
        this.tabTiles.add(tabtile);

    }
    public void AddFragments(Fragment fragment)
    {
        this.fragments.add(fragment);
    }

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTiles.get(position);
    }
}