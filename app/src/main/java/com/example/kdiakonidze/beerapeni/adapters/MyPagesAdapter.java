package com.example.kdiakonidze.beerapeni.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kdiakonidze.beerapeni.fragments.AmonaweriPageFr;

/**
 * Created by k.diakonidze on 11/15/2017.
 */

public class MyPagesAdapter extends FragmentPagerAdapter {
    private String titles[] = {" დავალიანება "," კასრები "};
    private FragmentManager fragmentManager;
    private int obj_id;
//    private int location=0;

    public MyPagesAdapter(FragmentManager fm, int obj_id) {
        super(fm);
        fragmentManager = fm;
        this.obj_id = obj_id;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("location",position);
        bundle.putInt("id",obj_id);

        Fragment fragment = new AmonaweriPageFr();

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public Fragment getFragmentM(){
        return fragmentManager.getFragments().get(0);
    }

    public Fragment getFragmentK(){
        return fragmentManager.getFragments().get(1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
