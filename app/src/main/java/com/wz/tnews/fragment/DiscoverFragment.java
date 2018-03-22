package com.wz.tnews.fragment;

import com.wz.tnews.R;
import com.wz.tnews.adapter.DiscoverFragmentAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by YoKeyword on 16/2/3.
 */
public class DiscoverFragment extends BaseMainFragment {

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        initView(view);

        return view;
    }

    String[] mTitles = new String[]{"福利", "Android", "iOS ", "休息视频", " 拓展资源 ", " 前端"};

    private void initView(View view) {
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mToolbar.setTitle(R.string.title);
        initToolbarNav(mToolbar, true);

        for (int i = 0; i < mTitles.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[i]));
        }
        final DiscoverFragmentAdapter adapter = new DiscoverFragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("wangzhan", "onPageSelected: ..." + position);
//                ((PagerChildFragment)adapter.getItem(position)).onItemSelected(position);
                PagerChildFragment fragment = PagerChildFragment.newInstance(position);
                fragment.onItemSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Log.i("wangzhan", "onAttach: ...." + (context instanceof NavigationView
                .OnNavigationItemSelectedListener) + "...." + (context instanceof
                OnOpenDrawerListener) + "..." + (context instanceof onClick));
        if (context instanceof OnOpenDrawerListener) {
            mOpenDraweListener = (OnOpenDrawerListener) context;
        } else {
            System.out.println(context.toString() + " must implements OnOpenDrawerListener");
        }
    }
}
