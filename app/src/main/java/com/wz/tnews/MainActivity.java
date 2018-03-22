package com.wz.tnews;

import static com.wz.tnews.BaseApplication.mTencent;
import static com.wz.tnews.fragment.BaseMainFragment.OnOpenDrawerListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.polaric.colorful.CActivity;
import org.polaric.colorful.Colorful;

import com.bumptech.glide.Glide;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.wz.tnews.fragment.DiscoverFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends CActivity implements NavigationView
        .OnNavigationItemSelectedListener, OnOpenDrawerListener {
    private FrameLayout fl_container;
    private NavigationView navigationView;
    private IUiListener listener;

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

        LinearLayout llNavHeader = (LinearLayout) mNavigationView.getHeaderView(0);
        llNavHeader.setBackgroundColor(getResources().getColor(Colorful.getThemeDelegate()
                .getPrimaryColor()
                .getColorRes()));
        mTvName = (TextView) llNavHeader.findViewById(R.id.tv_name);
        mImgNav = (ImageView) llNavHeader.findViewById(R.id.img_nav);
        mTvName.setText(BaseApplication.sApp.getPreferences().getString("title", "匿名"));
        String icon = BaseApplication.sApp.getPreferences().getString("icon", null);
        Glide.with(MainActivity.this).load(icon == null ? R.drawable.icon_search : icon)

                .into(mImgNav);
        llNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果session不可用，则登录，否则说明已经登录
                if (!mTencent.isSessionValid()) {
                    mTencent.login(MainActivity.this, "all", listener = new
                            IUiListener() {
                                @Override
                                public void onComplete(Object o) {
                                    Log.i("login", "onComplete: ..." + o);
                                    initOpenIdAndToken(o);
                                    getUserInfo();
                                }

                                @Override
                                public void onError(UiError uiError) {
                                    Log.i("login", "onError: ..." + uiError);
                                }

                                @Override
                                public void onCancel() {
                                    Log.i("login", "onCancel: ...");
                                }
                            });
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResultData(requestCode, resultCode, data, listener);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.i("onNacted", "onNavigationItemSelected: .." + id);
        switch (id) {
            case R.id.nav_clean:
                swipToActivity(CleanActivity.class);
                break;
            case R.id.nav_about:
                swipToActivity(AboutActivity.class);
                break;
            case R.id.nav_setting:
                swipToActivity(SettingActivity.class);
                break;
//            case R.id.nav_collect:
//                swipToActivity(CollectActivity.class);
//                break;
            case R.id.nav_search:
                swipToActivity(SearchActivity.class);
                break;
        }
        return true;
    }

    public void swipToActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        this.startActivity(intent);
        mDrawer.closeDrawer(GravityCompat.START);
    }

    public void onOpenDrawer() {
        if (mDrawer != null) {
            mDrawer.openDrawer(Gravity.LEFT);
        }
    }


    private void initOpenIdAndToken(Object object) {
        JSONObject jb = (JSONObject) object;
        try {
            String openID = jb.getString("openid");  //openid用户唯一标识
            String access_token = jb.getString("access_token");
            String expires = jb.getString("expires_in");
            mTencent.setOpenId(openID);
            mTencent.setAccessToken(access_token, expires);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserInfo() {
        QQToken token = mTencent.getQQToken();
        UserInfo mInfo = new UserInfo(MainActivity.this, token);
        mInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object object) {
                JSONObject jb = (JSONObject) object;
                try {
                    String name = jb.getString("nickname");
                    String figureurl = jb.getString("figureurl_qq_2");  //头像图片的url
                    Log.i("logining", "onComplete: .." + name + ".." + figureurl);
                    BaseApplication.sApp.getPreferences().edit().putString("title", name)
                            .putString("icon", figureurl).commit();
                    mTvName.setText(name);
                    Glide.with(MainActivity.this).load(figureurl).into(mImgNav);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                Log.i("loginin", "onError:/// " + uiError);
            }

            @Override
            public void onCancel() {
                Log.i("loginin", "onCancel:/// ");
            }
        });
    }
}
