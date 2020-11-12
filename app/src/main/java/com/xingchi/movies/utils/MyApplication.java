package com.xingchi.movies.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xingchi.movies.R;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    //imageLoader显示加载图片
    private static DisplayImageOptions mLoaderOptions;
    //volley请求队列
    private static RequestQueue mQueue;
    public MyApplication() {
        super();
        Log.i(TAG, "MyApplication: ");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(TAG, "onTerminate: ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        //初始化ImageLoader
        initImageLoader(getApplicationContext());
        //初始化volley队列
        mQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static void initImageLoader(Context context) {
        //初始化一个imageLoaderConfiguration对象
        //图片访问优先级、加密方式
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.
                Builder(context).
                denyCacheImageMultipleSizesInMemory().
                threadPriority(Thread.NORM_PRIORITY - 2).
                diskCacheFileNameGenerator(new Md5FileNameGenerator()).
                tasksProcessingOrder(QueueProcessingType.FIFO).
                build();
        //用imageLoaderConfiguration配置对象来完成ImageLoader的初始化，单例
        ImageLoader.getInstance().init(config);
        //显示图片加载过程中的参数
        mLoaderOptions = new DisplayImageOptions.Builder().
                showImageOnLoading(R.drawable.no_image).//正在加载设置默认的图片
                showImageOnFail(R.drawable.no_image).//加载失败的时候设置默认图片
                showImageForEmptyUri(R.drawable.no_image).//当URI为空的时候设置默认图片
                imageScaleType(ImageScaleType.EXACTLY_STRETCHED).//允许缩放图片
                cacheInMemory(true).//是否缓冲
                cacheOnDisk(true).
                considerExifParams(true).
                build();
    }



    public static RequestQueue getHttpQueue() {
        return mQueue;
    }

    public static DisplayImageOptions getLoaderOptions() {
        return mLoaderOptions;
    }

    public static void addRequest(Request request, Object tag) {
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(request);
    }

    public static void removeRequest(Object tag) {
        mQueue.cancelAll(tag);
    }
}
