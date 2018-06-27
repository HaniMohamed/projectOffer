package com.example.hp.offermagnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

/**
 * Created by hp on 09/04/2018.
 */

public class tabpagerAdapter extends FragmentStatePagerAdapter {
    String[]tabArray=new String[]{"Offer","Request","Status"};
    Integer tabnumber=3;
    String searchTxt="";
    public tabpagerAdapter(FragmentManager fm, String txt)
    {
        super(fm);
        searchTxt= txt;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabArray[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                One f=new One();
                Bundle bundle = new Bundle();
                bundle.putString("srchTxt", searchTxt);
                f.setArguments(bundle);
                return f;
            case 1:
                Two r=new Two();
                return r;
            case 2:
                Three s=new Three();
                return s;


        }

        return null;

    }

    @Override
    public int getCount() {

        return tabnumber;
    }
}
