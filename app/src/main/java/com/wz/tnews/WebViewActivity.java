package com.wz.tnews;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


/**
 * Created by v_wangzhan on 2017/9/1.
 */

public class WebViewActivity extends AppCompatActivity {
    private String TAG = "WebViewActivity";
     WebView webView;
    private static final String APP_CACHE_DIRNAME = "/webcache"; // web缓存目录

    //    String content = "\"<div><section class=\"entry-content ng-binding ng-scope\"><p> 本文来自于<a href=\"https://link.zhihu.com/?target=https%3A//bugly.qq.com/\" class=\" wrap external\" target=\"_blank\" rel=\"nofollow noreferrer\">腾讯bugly开发者社区<i class=\"icon-external\"></i></a>，非经作者同意，请勿转载，原文地址：<a href=\"https://link.zhihu.com/?target=http%3A//dev.qq.com/topic/5811d3e3ab10c62013697408\" class=\" wrap external\" target=\"_blank\" rel=\"nofollow noreferrer\">Android ListView与RecyclerView对比浅析—缓存机制<i class=\"icon-external\"></i></a></p><blockquote><p>作者：黄宁源</p></blockquote><h2>一，背景</h2><p> RecyclerView是谷歌官方出的一个用于大量数据展示的新控件，可以用来代替传统的ListView，更加强大和灵活。</p><p> 最近，自己负责的业务，也遇到这样的一个问题，关于是否要将ListView替换为RecyclerView？</p><p> 秉承着实事求是的作风，弄清楚RecyclerView是否有足够的吸引力替换掉ListView，我从性能这一角度出发，研究RecyclerView和ListView二者的缓存机制，并得到了一些较有益的”结论”，待我慢慢道来。</p><p> 同时也希望能通过本文，让大家快速了解RecyclerView与ListView在缓存机制上的一些区别，在使用上也更加得心应手吧。</p><p> PS：相关知识：<br> ListView与RecyclerView缓存机制原理大致相似，如下图所示：</p><img src=\"https://pic3.zhimg.com/v2-644b0df72dfdf109524b4530b4aa8736_r.png\" class=\"origin_image zh-lightbox-thumb\" width=\"625\"><br><p> 过程中，离屏的ItemView即被回收至缓存，入屏的ItemView则会优先从缓存中获取，只是ListView与RecyclerView的实现细节有差异.（这只是缓存使用的其中一个场景，还有如刷新等）</p><p> PPS：本文不贴出详细代码，结合源码食用更佳！</p><h2>二. 正文</h2><h4>2.1 缓存机制对比</h4>\n"
//            + "1. 层级不同：\n"
//            + "<p> RecyclerView比ListView多两级缓存，支持多个离ItemView缓存，支持开发者自定义缓存处理逻辑，支持所有RecyclerView共用同一个RecyclerViewPool(缓存池)。</p><p> 具体来说：<br> ListView(两级缓存)：</p><img src=\"https://pic2.zhimg.com/v2-0ea7851996a39115901c2ae3cd5767dd_r.png\" class=\"origin_image zh-lightbox-thumb\" width=\"704\"><br><p> RecyclerView(四级缓存)：</p><img src=\"https://pic4.zhimg.com/v2-746b3372c1f813d990681280fe5e93b3_r.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"703\"><br><p> ListView和RecyclerView缓存机制基本一致：</p><p> 1). mActiveViews和mAttachedScrap功能相似，意义在于快速重用屏幕上可见的列表项ItemView，而不需要重新createView和bindView；</p><p> 2). mScrapView和mCachedViews + mReyclerViewPool功能相似，意义在于缓存离开屏幕的ItemView，目的是让即将进入屏幕的ItemView重用.</p><p> 3). RecyclerView的优势在于a.mCacheViews的使用，可以做到屏幕外的列表项ItemView进入屏幕内时也无须bindView快速重用；b.mRecyclerPool可以供多个RecyclerView共同使用，在特定场景下，如viewpaper+多个列表页下有优势.客观来说，RecyclerView在特定场景下对ListView的缓存机制做了补强和完善。</p>\n"
//            + "2. 缓存不同：\n"
//            + "<p> 1). RecyclerView缓存RecyclerView.ViewHolder，抽象可理解为：<br> View + ViewHolder(避免每次createView时调用findViewById) + flag(标识状态)；<br> 2). ListView缓存View。</p><p> 缓存不同，二者在缓存的使用上也略有差别，具体来说：<br> ListView获取缓存的流程：</p><img src=\"https://pic1.zhimg.com/v2-8745a78e15b09d7652ef9faa28eb1180_r.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"513\"><br><p> RecyclerView获取缓存的流程：</p><img src=\"https://pic2.zhimg.com/v2-a8d1b3f8f1d3b96db61ef34b3934c7b9_r.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"551\"><br><p> 1). RecyclerView中mCacheViews(屏幕外)获取缓存时，是通过匹配pos获取目标位置的缓存，这样做的好处是，当数据源数据不变的情况下，无须重新bindView：</p><img src=\"https://pic1.zhimg.com/v2-9ebfec28c0a8c7a8f2a10f6df3829f74_b.jpg\" class=\"content_image\" width=\"276\"><p><b>注：点击顶部原文链接可查看动态图</b><br></p><p>而同样是离屏缓存，ListView从mScrapViews根据pos获取相应的缓存，但是并没有直接使用，而是重新getView（即必定会重新bindView），相关代码如下：</p><div class=\"highlight\"><pre><code class=\"language-text\"> //AbsListView源码：line2345\n"
//            + "//通过匹配pos从mScrapView中获取缓存\n"
//            + "final View scrapView = mRecycler.getScrapView(position);\n"
//            + "//无论是否成功都直接调用getView,导致必定会调用createView\n"
//            + "final View child = mAdapter.getView(position, scrapView, this);\n"
//            + "if (scrapView != null) {\n"
//            + "if (child != scrapView) {\n"
//            + "mRecycler.addScrapView(scrapView, position);\n"
//            + "} else {\n"
//            + "...\n"
//            + "}\n"
//            + "}\n"
//            + "</code></pre></div><p> 2). ListView中通过pos获取的是view，即pos→view；<br> RecyclerView中通过pos获取的是viewholder，即pos → (view，viewHolder，flag)；<br> 从流程图中可以看出，标志flag的作用是判断view是否需要重新bindView，这也是RecyclerView实现局部刷新的一个核心.</p><h4>2.2 局部刷新</h4><p> 由上文可知，RecyclerView的缓存机制确实更加完善，但还不算质的变化，RecyclerView更大的亮点在于提供了局部刷新的接口，通过局部刷新，就能避免调用许多无用的bindView.</p><img src=\"https://pic1.zhimg.com/v2-b4e4c3ed9e7fe14de6f29913ca934c54_r.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"613\"><b>注：点击顶部原文链接可查看动态图</b><br><p> (RecyclerView和ListView添加，移除Item效果对比)</p><p> 结合RecyclerView的缓存机制，看看局部刷新是如何实现的：<br> 以RecyclerView中notifyItemRemoved(1)为例，最终会调用requestLayout()，使整个RecyclerView重新绘制，过程为：<br> onMeasure()→onLayout()→onDraw()</p><p> 其中，onLayout()为重点，分为三步：</p><ol><li>dispathLayoutStep1()：记录RecyclerView刷新前列表项ItemView的各种信息，如Top,Left,Bottom,Right，用于动画的相关计算；</li><li>dispathLayoutStep2()：真正测量布局大小，位置，核心函数为layoutChildren()；</li><li>dispathLayoutStep3()：计算布局前后各个ItemView的状态，如Remove，Add，Move，Update等，如有必要执行相应的动画.</li></ol><p> 其中，layoutChildren()流程图：</p><img src=\"https://pic1.zhimg.com/v2-5420bec6753c3aa0f7c74d31d38f153c_r.jpg\" class=\"origin_image zh-lightbox-thumb\" width=\"573\"><img src=\"https://pic1.zhimg.com/v2-c8eaf0e6c5fc4ab9167a6070ff249798_b.png\" class=\"content_image\" width=\"354\"><p> 当调用notifyItemRemoved时，会对屏幕内ItemView做预处理，修改ItemView相应的pos以及flag(流程图中红色部分)：</p><img src=\"https://pic3.zhimg.com/v2-372694ae33986c253816422489e79e7a_r.png\" class=\"origin_image zh-lightbox-thumb\" width=\"710\"><br><p> 当调用fill()中RecyclerView.getViewForPosition(pos)时，RecyclerView通过对pos和flag的预处理，使得bindview只调用一次.</p><p> 需要指出，ListView和RecyclerView最大的区别在于数据源改变时的缓存的处理逻辑，ListView是”一锅端”，将所有的mActiveViews都移入了二级缓存mScrapViews，而RecyclerView则是更加灵活地对每个View修改标志位，区分是否重新bindView。</p><h2>三.结论</h2><p> 更多精彩内容欢迎关注<a href=\"https://link.zhihu.com/?target=https%3A//bugly.qq.com/\" class=\" wrap external\" target=\"_blank\" rel=\"nofollow noreferrer\">bugly<i class=\"icon-external\"></i></a>的微信公众账号： </p><img src=\"https://pic3.zhimg.com/3f2c1b1ff77f19100358cedf3fb54616_b.jpg\" class=\"content_image\"><p><a href=\"https://link.zhihu.com/?target=https%3A//bugly.qq.com/\" class=\" wrap external\" target=\"_blank\" rel=\"nofollow noreferrer\">腾讯 Bugly<i class=\"icon-external\"></i></a>是一款专为移动开发者打造的质量监控工具，帮助开发者快速，便捷的定位线上应用崩溃的情况以及解决方案。智能合并功能帮助开发同学把每天上报的数千条 <a href=\"https://link.zhihu.com/?target=https%3A//bugly.qq.com/\" class=\" wrap external\" target=\"_blank\" rel=\"nofollow noreferrer\">Crash<i class=\"icon-external\"></i></a> 根据根因合并分类，每日日报会列出影响用户数最多的崩溃，精准定位功能帮助开发同学定位到出问题的代码行，实时上报可以在发布后快速的了解应用的质量情况，适配最新的 iOS, Android 官方操作系统，鹅厂的工程师都在使用，快来加入我们吧！</p><br>\u200B</section>\n"
//            + "\n"
//            + "</div>\"";
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        String url = getIntent().getStringExtra("url");
        Log.i(TAG, "onCreate: ." + url);
        final ImageView img = (ImageView) findViewById(R.id.image);
        webView = (WebView) findViewById(R.id.webview);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true); // 设置支持javascript脚本
        ws.setAllowFileAccess(true); // 允许访问文件
        ws.setBuiltInZoomControls(true); // 设置显示缩放按钮
        ws.setSupportZoom(true); // 支持缩放 <span style="color:#337fe5;"> /**
        // * 用WebView显示图片，可使用这个参数
        // * 设置网页布局类型：
        // * 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
        // * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
        // */
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        ws.setDefaultTextEncodingName("utf-8"); // 设置文本编码
        ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 设置缓存模式</span>
        ws.setDomStorageEnabled(true);
        // 开启database storage API功能
        ws.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME;
        Log.i("cachePath", cacheDirPath);
        // 设置数据库缓存路径
        ws.setAppCacheEnabled(true);
        ws.setAppCachePath(cacheDirPath);
        Log.i("databasepath", ws.getDatabasePath());
        //添加Javascript调用java对象
        webView.addJavascriptInterface(this, "java2js");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i(TAG, "onPageStarted: ");
//                Glide.with(WebViewActivity.this).load(R.drawable.loading).asGif()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i(TAG, "onPageFinished: ....");
//                img.setVisibility(View.GONE);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.i(TAG, "onLoadResource:.. "+url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.i(TAG, "onProgressChanged: ");
            }
        });
        // 设置打开的网页
        // webView.loadUrl("http://orgcent.com");
        // 使用WebView来显示图片
//        webView.loadDataWithBaseURL(null, content, "text/html; charset=UTF-8", null, null);
        webView.loadUrl(url);


    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
