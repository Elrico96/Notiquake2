package com.example.notiquake.app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private  int noTabs;

    public PageAdapter(@NonNull FragmentManager fm, int numtabs) {
        super(fm);
        this.noTabs = numtabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new EarthqaukeFragment();
            case 1:
                return  new EarhquakeNewsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noTabs;
    }
}
