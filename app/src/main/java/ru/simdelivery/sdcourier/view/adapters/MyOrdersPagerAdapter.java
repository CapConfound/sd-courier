package ru.simdelivery.sdcourier.view.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ru.simdelivery.sdcourier.view.fragments.details.MyOrderDetailsFragment;

public class MyOrdersPagerAdapter extends FragmentStatePagerAdapter {


    public MyOrdersPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        MyOrderDetailsFragment myOrdersDetailsFragment = new MyOrderDetailsFragment();
        Bundle bundle = new Bundle();
        position += 1;
        bundle.putString("message", "page"+position);
        myOrdersDetailsFragment.setArguments(bundle);


        return myOrdersDetailsFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
