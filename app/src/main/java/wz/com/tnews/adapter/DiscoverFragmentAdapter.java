package wz.com.tnews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import wz.com.tnews.fragment.PagerChildFragment;


/**
 * Created by YoKeyword on 16/2/5.
 */
public class DiscoverFragmentAdapter extends FragmentPagerAdapter {
    String[] mTitles = new String[]{"推荐", "开源APP", "福利", "Android", "iOS ", " 拓展资源 ", " 前端"};

    public DiscoverFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PagerChildFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
