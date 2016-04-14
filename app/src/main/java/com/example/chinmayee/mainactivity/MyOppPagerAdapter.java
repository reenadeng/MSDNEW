package com.example.chinmayee.mainactivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class MyOppPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Bundle bundle;

    public MyOppPagerAdapter(FragmentManager fm, int NumOfTabs, Bundle bundle) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                InProgressFragment inProgress = new InProgressFragment();
                inProgress.setArguments(bundle);
                return inProgress;
            case 1:
                UpcomingOppFragment upcoming = new UpcomingOppFragment();
                upcoming.setArguments(bundle);
                return upcoming;
            case 2:
                CompletedFragment completed = new CompletedFragment();
                completed.setArguments(bundle);
                return completed;
            case 3:
                SavedOppFragment saved = new SavedOppFragment();
                saved.setArguments(bundle);
                return saved;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}