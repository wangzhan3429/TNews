package com.wz.tnews;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import dalvik.system.PathClassLoader;

/**
 * @author wangzhan
 * @version 2018-01-04
 *          加载已将安装的插件
 */

public class InstalledActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.install_layout);
        final List list = findAllPlugin();
        final ImageView img = findViewById(R.id.ins_img);

        findViewById(R.id.bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.size() > 0) {
                    PluginBean bean = (PluginBean) list.get(0);
                    try {
                        //获取对应插件中的上下文,通过它可得到插件的Resource
                        Context plugnContext =
                                InstalledActivity.this.createPackageContext(bean.getPkgName(), CONTEXT_IGNORE_SECURITY |
                                        CONTEXT_INCLUDE_CODE);
                        int resourceId = dynamicLoadApk(bean.getPkgName(), plugnContext);

                        Drawable drawable = plugnContext.getResources().getDrawable(resourceId);
                        Log.i("inatall", "onClick: ..." + resourceId + ".." + drawable);
                        img.setImageDrawable(drawable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    /**
     * 查找手机内所有的插件
     *
     * @return 返回一个插件List
     */
    private List<PluginBean> findAllPlugin() {
        List<PluginBean> plugins = new ArrayList<>();
        PackageManager pm = getPackageManager();
        //通过包管理器查找所有已安装的apk文件
        List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo info : packageInfos) {
            //得到当前apk的包名
            String pkgName = info.packageName;
            //得到当前apk的sharedUserId
            String shareUesrId = info.sharedUserId;
            //判断这个apk是否是我们应用程序的插件
            if (shareUesrId != null && shareUesrId.equals("com.wz.tnews") && !pkgName.equals(this.getPackageName())) {
                String label = pm.getApplicationLabel(info.applicationInfo).toString();//得到插件apk的名称
                PluginBean bean = new PluginBean(label, pkgName);
                Log.i("install", "findAllPlugin: .." + label + ".." + pkgName);
                plugins.add(bean);
            }
        }
        return plugins;
    }

    /**
     * 加载已安装的apk
     *
     * @param packageName   应用的包名
     * @param pluginContext 插件app的上下文
     *
     * @return 对应资源的id
     */
    private int dynamicLoadApk(String packageName, Context plugnContext) {
        try {

            //第一个参数为包含dex的apk或者jar的路径，第二个参数为父加载器
            PathClassLoader pathClassLoader =
                    new PathClassLoader(plugnContext.getPackageResourcePath(), ClassLoader.getSystemClassLoader());
            //        Class<?> clazz = pathClassLoader.loadClass(packageName + ".R$mipmap");//通过使用自身的加载器反射出mipmap类进而使用该类的功能
            //参数：1、类的全名，2、是否初始化类，3、加载时使用的类加载器
            Class<?> clazz = Class.forName(packageName + ".R$mipmap", true, pathClassLoader);
            //使用上述两种方式都可以，这里我们得到R类中的内部类mipmap，通过它得到对应的图片id，进而给我们使用
            Field field = clazz.getDeclaredField("img");
            int resourceId = field.getInt(R.mipmap.class);
            return resourceId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }
}
