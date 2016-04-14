package com.example.chinmayee.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Bundle bundle;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, Bundle bundle) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.bundle  = bundle;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AllOppFragment all = new AllOppFragment();
                all.setArguments(bundle);
                return all;
            case 1:
                ThisWeekOppFragment thisWeek = new ThisWeekOppFragment();
                thisWeek.setArguments(bundle);
                return thisWeek;
            case 2:
                RecomOppFragment recc = new RecomOppFragment();
                recc.setArguments(bundle);
                return recc;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}