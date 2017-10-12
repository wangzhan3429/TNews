package wz.com.tnews;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import wz.com.tnews.fragment.DiscoverFragment;

import static wz.com.tnews.fragment.BaseMainFragment.*;

public class MainActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener,OnOpenDrawerListener{
    private FrameLayout fl_container;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        fl_container = (FrameLayout) findViewById(R.id.fl_container);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (findViewById(R.id.fl_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an singleInstance of ExampleFragment
            DiscoverFragment firstFragment = DiscoverFragment.newInstance();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_container, firstFragment).commit();
        }
        initView();
    }

    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private TextView mTvName;
    private ImageView mImgNav;

    private void initView() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_home);

        LinearLayout llNavHeader = (LinearLayout) mNavigationView.getHeaderView(0);
        mTvName = (TextView) llNavHeader.findViewById(R.id.tv_name);
        mImgNav = (ImageView) llNavHeader.findViewById(R.id.img_nav);
        llNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.closeDrawer(GravityCompat.START);// 方向，start是左边，end是右边

                mDrawer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "login", Toast.LENGTH_SHORT).show();
                    }
                }, 250);
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    public void onOpenDrawer() {
        mDrawer.openDrawer(Gravity.LEFT);
    }

}
