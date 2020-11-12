package com.xingchi.movies.fragment;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;

import com.xingchi.movies.R;
import com.xingchi.movies.fragment.child.FirstChildFragment;


public class FirstFragment extends Fragment  {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final String TAG = "FirstFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_fragment_layout, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onCreateView: "+TAG);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this.getChildFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    String [] titles={"正在热映","即将上映","北美票房"};
    /**
     * viewPager适配器
     */
    class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return new FirstChildFragment();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public long getItemId(int position) {
            if(position==0){

            }else if(position==1){

            }else if(position==2){

            }
            return super.getItemId(position);
        }
    }

}
