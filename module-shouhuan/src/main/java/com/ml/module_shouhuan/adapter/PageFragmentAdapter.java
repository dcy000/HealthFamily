package com.ml.module_shouhuan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PageFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list;
    private String[] title;

    public PageFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }

    public PageFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list, String[] titleArray) {
        this(fm, list);
        this.title = titleArray;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (title != null){
            return title[position];
        }
        return "title";
    }
}
