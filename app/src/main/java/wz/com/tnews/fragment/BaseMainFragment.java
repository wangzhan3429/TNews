package wz.com.tnews.fragment;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import wz.com.tnews.R;


/**
 * Created by YoKeyword on 16/2/3.
 */
public class BaseMainFragment extends Fragment {


    protected void initToolbarNav(Toolbar toolbar) {
        initToolbarNav(toolbar, false);
    }

    protected void initToolbarNav(Toolbar toolbar, boolean isHome) {
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("wangzhan", "onClick: ..initToolbarNav.."+mOpenDraweListener);
                if (mOpenDraweListener != null) {
                    mOpenDraweListener.onOpenDrawer();
                }
            }
        });

    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Log.i("wangzhan", "onAttach: ...."+(context instanceof NavigationView
                .OnNavigationItemSelectedListener)+"...."+(context instanceof
                OnOpenDrawerListener)+"...");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOpenDraweListener = null;
    }

    public OnOpenDrawerListener mOpenDraweListener;
    public interface OnOpenDrawerListener {
        void onOpenDrawer();
    }

    public onClick click;

    public interface onClick {
        void click();
    }
}
