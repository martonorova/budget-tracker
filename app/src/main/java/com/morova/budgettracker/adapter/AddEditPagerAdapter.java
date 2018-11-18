package com.morova.budgettracker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.morova.budgettracker.fragments.AddEditCashMovementItemFragment;
import com.morova.budgettracker.fragments.AddEditCategoryFragment;

/**
 * Created by Root on 11/18/2018.
 */

public class AddEditPagerAdapter extends FragmentPagerAdapter {

    public static final int NUM_PAGES = 2;

    public AddEditPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AddEditCashMovementItemFragment();
            case 1:
                return new AddEditCategoryFragment();
            default:
                return new AddEditCashMovementItemFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
