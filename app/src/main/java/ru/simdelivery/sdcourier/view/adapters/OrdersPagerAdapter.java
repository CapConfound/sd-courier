package ru.simdelivery.sdcourier.view.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ru.simdelivery.sdcourier.view.fragments.details.pages.order.OrderDestinationFragment;
import ru.simdelivery.sdcourier.view.fragments.details.pages.order.OrderPickUpFragment;

public class OrdersPagerAdapter extends FragmentPagerAdapter {


    public OrdersPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                OrderPickUpFragment orderPickUpFragment = new OrderPickUpFragment();
                return orderPickUpFragment;
            case 1:
                OrderDestinationFragment orderDestinationFragment = new OrderDestinationFragment();
                return orderDestinationFragment;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
