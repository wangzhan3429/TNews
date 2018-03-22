//package com.wz.tnews;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//
//import android.annotation.SuppressLint;
//import android.content.res.AssetManager;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import dalvik.system.DexClassLoader;
//
//public class DynamicActivity extends DyActivity {
//
//
//    private TextView textV;
//    private ImageView imgV;
//    private LinearLayout layout;
//
//
//    protected DexClassLoader classLoader = null;
//    protected File fileRelease = null; //ÊÍ·ÅÄ¿Â¼
//
//    String filesDir;
//    String filePath;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dynamic);
//
//        textV = (TextView)findViewById(R.id.text);
//        imgV = (ImageView)findViewById(R.id.imageview);
//        layout = (LinearLayout)findViewById(R.id.layout);
//
//        filesDir = getCacheDir().getAbsolutePath();
//        filePath = filesDir + File.separator +"MyTest.apk";
//
//        fileRelease = getDir("dex1", 0);
//
//        saveAndLoadCacheDir();
//
//        findViewById(R.id.btn1).setOnClickListener(new OnClickListener(){
//
//            @Override
//            public void onClick(View arg0) {
//                Log.i("Loader", "filePath:"+filePath + "..."+fileRelease.getAbsolutePath());
//                classLoader = new DexClassLoader(filePath, fileRelease.getAbsolutePath(), null, ClassLoader.getSystemClassLoader());
//                Log.i("Loader", "isExist:"+new File(filePath).exists()+"..."+classLoader);
//                //loadResources(filePath);
//                getPluginResources(null);
//                setContent();
//                //printResourceId();
//                //setContent1();
//                //printRField();
//            }});
//
//        /*findViewById(R.id.btn2).setOnClickListener(new OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                String filesDir = getCacheDir().getAbsolutePath();
//                String filePath = filesDir + File.separator +"ResourceLoaderApk2.apk";
//                classLoader = new DexClassLoader(filePath, fileRelease.getAbsolutePath(), null, getClassLoader());
//                loadResources(filePath);
//                setContent();
//            }});*/
//    }
//
//    /**
//     * @param apkName
//     * @return 得到对应插件的Resource对象
//     */
//    private Resources getPluginResources(String apkName) {
//        try {
//            AssetManager assetManager = AssetManager.class.newInstance();
//            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);//反射调用方法addAssetPath(String path)
//            //第二个参数是apk的路径：Environment.getExternalStorageDirectory().getPath()+File.separator+"plugin"+File.separator+"apkplugin.apk"
//            addAssetPath.invoke(assetManager, filePath);
//            //将未安装的Apk文件的添加进AssetManager中，第二个参数为apk文件的路径带apk名
//            Resources superRes = this.getResources();
//            Resources mResources = new Resources(assetManager, superRes.getDisplayMetrics(),
//                    superRes.getConfiguration());
//            return mResources;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * ¶¯Ì¬¼ÓÔØÖ÷Ìâ°üÖÐµÄ×ÊÔ´£¬È»ºóÌæ»»Ã¿¸ö¿Ø¼þ
//     */
//    @SuppressLint("NewApi")
//    private void setContent(){
//        try{
//           /* Class clazz = classLoader.loadClass("com.example.wangzhan.dynamic.UIUtil");
//            Method method = clazz.getMethod("getTextString", Context.class);
//            String str = (String)method.invoke(null, this);
//            Log.i("gettext", "setContent: ..."+str);
//            textV.setText(str);
//            method = clazz.getMethod("getImageDrawable", Context.class);
//            Drawable drawable = (Drawable)method.invoke(null, this);
//            imgV.setBackground(drawable);
//            method = clazz.getMethod("getLayout", Context.class);
//            View view = (View)method.invoke(null, this);
//            layout.addView(view);*/
//
//
//            Class clazzs = classLoader.loadClass("com.example.wangzhan.dynamic.FunImpl");
//            Method md = clazzs.getMethod("sayHi");
//           String strings = (String) md.invoke(clazzs.newInstance());
//            Log.i("wangzhan", "setContent: ..."+strings);
//
//        }catch(Exception e){
//            Log.i("Loader", "error:"+Log.getStackTraceString(e));
//        }
//    }
//
//    /**
//     * ÁíÍâµÄÒ»ÖÖ·½Ê½»ñÈ¡
//     */
//    private void setContent1(){
//        int stringId = getTextStringId();
//        int drawableId = getImgDrawableId();
//        int layoutId = getLayoutId();
//        Log.i("Loader", "stringId:"+stringId+",drawableId:"+drawableId+",layoutId:"+layoutId);
//    }
//
//    @SuppressLint("NewApi")
//    private int getTextStringId(){
//        try{
//            Class clazz = classLoader.loadClass("com.example.wangzhan.dynamic.R$string");
//            Field field = clazz.getField("MyTest");
//            int resId = (int)field.get(null);
//            return resId;
//        }catch(Exception e){
//            Log.i("Loader", "error:"+Log.getStackTraceString(e));
//        }
//        return 0;
//    }
//
//    @SuppressLint("NewApi")
//    private int getImgDrawableId(){
//        try{
//            Class clazz = classLoader.loadClass("com.example.wangzhan.dynamic.R$drawable");
//            Field field = clazz.getField("ic_launcher");
//            int resId = (int)field.get(null);
//            return resId;
//        }catch(Exception e){
//            Log.i("Loader", "error:"+Log.getStackTraceString(e));
//        }
//        return 0;
//    }
//
//    @SuppressLint("NewApi")
//    private int getLayoutId(){
//        try{
//            Class clazz = classLoader.loadClass("com.example.wangzhan.dynamic.R$layout");
//            Field field = clazz.getField("activity_main");
//            int resId = (int)field.get(null);
//            return resId;
//        }catch(Exception e){
//            Log.i("Loader", "error:"+Log.getStackTraceString(e));
//        }
//        return 0;
//    }
//
//    @SuppressLint("NewApi")
//    private void printResourceId(){
//        try{
//            Class clazz = classLoader.loadClass("com.example.wangzhan.dynamic.UIUtil");
//            Method method = clazz.getMethod("getTextStringId", null);
//            Object obj = method.invoke(null, null);
//            Log.i("Loader", "stringId:"+obj);
//            Log.i("Loader", "newId:"+R.string.app_name);
//            method = clazz.getMethod("getImageDrawableId", null);
//            obj = method.invoke(null, null);
//            Log.i("Loader", "drawableId:"+obj);
//            Log.i("Loader", "newId:"+R.drawable.ic_launcher);
//            method = clazz.getMethod("getLayoutId", null);
//            obj = method.invoke(null, null);
//            Log.i("Loader", "layoutId:"+obj);
//            Log.i("Loader", "newId:"+R.layout.activity_main);
//        }catch(Exception e){
//            Log.i("Loader", "error:"+Log.getStackTraceString(e));
//        }
//    }
//
//    private void printRField(){
//        Class clazz = R.id.class;
//        Field[] fields = clazz.getFields();
//        for(Field field : fields){
//            Log.i("Loader", "fields:"+field);
//        }
//        Class clazzs = R.layout.class;
//        Field[] fieldss = clazzs.getFields();
//        for(Field field : fieldss){
//            Log.i("Loader", "fieldss:"+field);
//        }
//    }
//
//
//
//    public void saveAndLoadCacheDir() {
//        InputStream in = null;
//        FileOutputStream os = null;
//        try {
//             in = getAssets().open("MyTest.apk");
//            File filepath = getCacheDir();//getCacheDir
//            File fileOut = new File(filepath.getPath(), "MyTest.apk");
//            os = new FileOutputStream(fileOut);
//            byte[] buffer = new byte[1024];
//            int count = 0;
//            while ((count = in.read(buffer)) > 0) {
//                os.write(buffer, 0, count);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (in!=null)
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            if (os!=null)
//                try {
//                    os.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//        }
//    }
//
//}
